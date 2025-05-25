package io.github.moyusowo.neoartisan.block.util;

import io.github.moyusowo.neoartisan.block.storage.internal.ArtisanBlockStorageInternal;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlock;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.event.ArtisanBlockBreakEvent;
import io.github.moyusowo.neoartisanapi.api.block.event.ArtisanBlockLoseSupportEvent;
import io.github.moyusowo.neoartisanapi.api.block.event.ArtisanBlockPlaceEvent;
import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

import static io.github.moyusowo.neoartisan.block.util.BlockStateUtil.stateById;

public final class BlockEventUtil {

    public static <B extends ArtisanBlock> boolean canNotPlaceBasicCheck(PlayerInteractEvent event, Class<B> artisanBlockClass) {
        if (event.useInteractedBlock() == Event.Result.DENY) return true;
        if (event.useItemInHand() == Event.Result.DENY) return true;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return true;
        if (!NeoArtisanAPI.getItemRegistry().isArtisanItem(event.getItem())) return true;
        ArtisanItem artisanItem = NeoArtisanAPI.getItemRegistry().getArtisanItem(event.getItem());
        if (artisanItem.getBlockId() == null) return true;
        if (!NeoArtisanAPI.getBlockRegistry().isArtisanBlock(artisanItem.getBlockId())) return true;
        if (!artisanBlockClass.isInstance(NeoArtisanAPI.getBlockRegistry().getArtisanBlock(artisanItem.getBlockId()))) return true;
        return false;
    }

    public static <D extends ArtisanBlockData> void onPlaceBasicLogic(PlayerInteractEvent event, Block blockWillPlace, Block blockPlaceAgainst, D newArtisanBlockData) throws Exception {
        event.setCancelled(true);
        ArtisanItem artisanItem = NeoArtisanAPI.getItemRegistry().getArtisanItem(event.getItem());
        ArtisanBlockPlaceEvent artisanBlockPlaceEvent = new ArtisanBlockPlaceEvent(
                blockWillPlace,
                blockWillPlace.getState(),
                blockPlaceAgainst,
                event.getItem(),
                event.getPlayer(),
                true,
                EquipmentSlot.HAND,
                NeoArtisanAPI.getBlockRegistry().getArtisanBlock(artisanItem.getBlockId())
        );
        Bukkit.getPluginManager().callEvent(artisanBlockPlaceEvent);
        if (artisanBlockPlaceEvent.isCancelled()) return;
        place(blockWillPlace, newArtisanBlockData);
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.getItem().setAmount(event.getItem().getAmount() - 1);
        }
    }

    public static <D extends ArtisanBlockData> boolean isNotTypedArtisanBlock(Block block, Class<D> artisanBlockDataClass) {
        if (!NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(block)) return true;
        if (!artisanBlockDataClass.isInstance(NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlock(block))) return true;
        return false;
    }

    public static void onBreakBasicLogic(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        ArtisanBlockData artisanBlockData = NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlock(event.getBlock());
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock());
            return;
        }
        event.setCancelled(true);
        ArtisanBlockBreakEvent artisanBlockBreakEvent = new ArtisanBlockBreakEvent(
                event.getBlock(),
                event.getPlayer(),
                artisanBlockData.getArtisanBlock()
        );
        artisanBlockBreakEvent.setExpToDrop(event.getExpToDrop());
        artisanBlockBreakEvent.callEvent();
        if (artisanBlockBreakEvent.isCancelled()) return;
        if (artisanBlockBreakEvent.getExpToDrop() > 0) {
            ExperienceOrb orb = (ExperienceOrb) event.getBlock().getWorld().spawnEntity(
                    event.getBlock().getLocation(),
                    EntityType.EXPERIENCE_ORB
            );
            orb.setExperience(artisanBlockBreakEvent.getExpToDrop());
        }
        event.getBlock().setType(Material.AIR);
        if (artisanBlockBreakEvent.isDropItems()) {
            for (ItemStack drop : artisanBlockData.getArtisanBlockState().drops()) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
            }
        }
        ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock());
    }

    public static void onBelowBlockBreakBasicLogic(BlockBreakEvent event) {
        ArtisanBlockData artisanBlockData = NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlock(event.getBlock().getRelative(BlockFace.UP));
        if (event.isCancelled()) return;
        event.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
        ArtisanBlockLoseSupportEvent artisanBlockLoseSupportEvent = new ArtisanBlockLoseSupportEvent(
                event.getBlock(),
                artisanBlockData.getArtisanBlock()
        );
        artisanBlockLoseSupportEvent.callEvent();
        if (artisanBlockLoseSupportEvent.getExpToDrop() > 0) {
            ExperienceOrb orb = (ExperienceOrb) event.getBlock().getWorld().spawnEntity(
                    event.getBlock().getLocation(),
                    EntityType.EXPERIENCE_ORB
            );
            orb.setExperience(artisanBlockLoseSupportEvent.getExpToDrop());
        }
        if (artisanBlockLoseSupportEvent.isDropItems()) {
            for (ItemStack drop : artisanBlockData.getArtisanBlockState().drops()) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getRelative(BlockFace.UP).getLocation(), drop);
            }
        }
        ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock().getRelative(BlockFace.UP));
    }

    public static <D extends ArtisanBlockData> void onBelowBlockPistonBreakOrMoveBasicLogic(BlockPistonExtendEvent event, Class<D> artisanBlockDataClass) {
        if (event.isCancelled()) return;
        for (Block block : event.getBlocks()) {
            if (isNotTypedArtisanBlock(block.getRelative(BlockFace.UP), artisanBlockDataClass)) return;
            ArtisanBlockData artisanBlockData = NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlock(event.getBlock().getRelative(BlockFace.UP));
            block.getRelative(BlockFace.UP).setType(Material.AIR);
            ArtisanBlockLoseSupportEvent artisanBlockLoseSupportEvent = new ArtisanBlockLoseSupportEvent(
                    event.getBlock(),
                    artisanBlockData.getArtisanBlock()
            );
            artisanBlockLoseSupportEvent.callEvent();
            if (artisanBlockLoseSupportEvent.getExpToDrop() > 0) {
                ExperienceOrb orb = (ExperienceOrb) event.getBlock().getWorld().spawnEntity(
                        event.getBlock().getLocation(),
                        EntityType.EXPERIENCE_ORB
                );
                orb.setExperience(artisanBlockLoseSupportEvent.getExpToDrop());
            }
            if (artisanBlockLoseSupportEvent.isDropItems()) {
                for (ItemStack drop : artisanBlockData.getArtisanBlockState().drops()) {
                    event.getBlock().getWorld().dropItemNaturally(event.getBlock().getRelative(BlockFace.UP).getLocation(), drop);
                }
            }
            ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(block.getRelative(BlockFace.UP));
        }
    }

    public static void onWaterOrPistonBreakBasicLogic(BlockBreakBlockEvent event) {
        ArtisanBlockData artisanBlockData = NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlock(event.getBlock());
        while (!event.getDrops().isEmpty()) event.getDrops().removeFirst();
        for (ItemStack drop : artisanBlockData.getArtisanBlockState().drops()) {
            event.getDrops().add(drop);
        }
        ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock());
    }

    public static <D extends ArtisanBlockData> void place(Block bukkitBlock, D artisanBlockData) throws Exception {
        CraftWorld craftWorld = (CraftWorld) bukkitBlock.getWorld();
        Level nmsWorld = craftWorld.getHandle();
        BlockPos pos = new BlockPos(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ());
        ArtisanBlockStorageInternal.getInternal().placeArtisanBlock(nmsWorld, pos, artisanBlockData);
        nmsWorld.setBlock(pos, stateById(artisanBlockData.getArtisanBlockState().actualState()), 3);
    }

    public static <D extends ArtisanBlockData> void replace(Block bukkitBlock, D artisanBlockData) {
        CraftWorld craftWorld = (CraftWorld) bukkitBlock.getWorld();
        Level nmsWorld = craftWorld.getHandle();
        BlockPos pos = new BlockPos(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ());
        ArtisanBlockStorageInternal.getInternal().replaceArtisanBlock(nmsWorld, pos, artisanBlockData);
        nmsWorld.setBlock(pos, stateById(artisanBlockData.getArtisanBlockState().actualState()), 3);
    }

    public static void playBoneMealEffects(Location loc) {
        World world = loc.getWorld();
        world.spawnParticle(
                Particle.HAPPY_VILLAGER,
                loc.clone().add(0.5, 0.5, 0.5),
                20, 0.3, 0.3, 0.3, 0.5
        );
        for (int i = 0; i < 5; i++) {
            world.spawnParticle(
                    Particle.HAPPY_VILLAGER,
                    loc.clone().add(
                            0.5 + ThreadLocalRandom.current().nextGaussian() * 0.3,
                            0.1,
                            0.5 + ThreadLocalRandom.current().nextGaussian() * 0.3
                    ),
                    2, 0, 0.1, 0, 0.2
            );
        }
        world.playSound(loc, Sound.ITEM_BONE_MEAL_USE, 1.0f, 1.2f);
    }

}
