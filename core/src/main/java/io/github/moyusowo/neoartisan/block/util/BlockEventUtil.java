package io.github.moyusowo.neoartisan.block.util;

import io.github.moyusowo.neoartisan.NeoArtisan;
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
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
        if ((!event.getPlayer().isSneaking()) && InteractionUtil.isInteractable(event.getClickedBlock())) return true;
        return false;
    }

    public static void onPlaceBasicLogic(PlayerInteractEvent event, Block blockWillPlace, Block blockPlaceAgainst, ArtisanBlockData newArtisanBlockData) throws Exception {
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
                NeoArtisanAPI.getBlockRegistry().getArtisanBlock(artisanItem.getBlockId()),
                newArtisanBlockData
        );
        artisanBlockPlaceEvent.callEvent();
        if (artisanBlockPlaceEvent.isCancelled()) return;
        place(blockWillPlace, artisanBlockPlaceEvent.getPlacedArtisanBlockData());
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.getItem().setAmount(event.getItem().getAmount() - 1);
        }
    }

    public static <D extends ArtisanBlockData> boolean isNotTypedArtisanBlock(Block block, Class<D> artisanBlockDataClass) {
        if (!NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(block)) return true;
        if (!artisanBlockDataClass.isInstance(NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlockData(block))) return true;
        return false;
    }

    public static void onBreakBasicLogic(BlockBreakEvent event) {
        if (event.isCancelled()) return;
        ArtisanBlockData artisanBlockData = NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlockData(event.getBlock());
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
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            if (artisanBlockBreakEvent.isDropItems()) {
                for (ItemStack drop : artisanBlockData.getArtisanBlockState().drops()) {
                    event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
                }
            }
        }
        ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock());
        if (artisanBlockData.getArtisanBlock().getPlaceSoundProperty() != null) {
            event.getBlock().getWorld().playSound(
                    event.getBlock().getLocation().toBlockLocation(),
                    artisanBlockData.getArtisanBlock().getPlaceSoundProperty().key,
                    artisanBlockData.getArtisanBlock().getPlaceSoundProperty().volume,
                    artisanBlockData.getArtisanBlock().getPlaceSoundProperty().pitch
            );
        }
    }

    public static void onBelowBlockBreakBasicLogic(BlockBreakEvent event) {
        ArtisanBlockData artisanBlockData = NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlockData(event.getBlock().getRelative(BlockFace.UP));
        if (event.isCancelled()) return;
        event.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
        ArtisanBlockLoseSupportEvent artisanBlockLoseSupportEvent = new ArtisanBlockLoseSupportEvent(
                event.getBlock().getRelative(BlockFace.UP),
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

    public static <D extends ArtisanBlockData> void onBelowBlockPistonBreakOrMove(BlockPistonExtendEvent event, Class<D> artisanBlockDataClass) {
        if (event.isCancelled()) return;
        for (Block block : event.getBlocks()) {
            if (isNotTypedArtisanBlock(block.getRelative(BlockFace.UP), artisanBlockDataClass)) return;
            ArtisanBlockData artisanBlockData = NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlockData(event.getBlock().getRelative(BlockFace.UP));
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
        ArtisanBlockData artisanBlockData = NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlockData(event.getBlock());
        while (!event.getDrops().isEmpty()) event.getDrops().removeFirst();
        for (ItemStack drop : artisanBlockData.getArtisanBlockState().drops()) {
            event.getDrops().add(drop);
        }
        ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock());
    }

    public static <D extends ArtisanBlockData> void onBlockExplode(BlockExplodeEvent event, Class<D> artisanBlockDataClass) {
        NeoArtisan.logger().info("onBlockExplode");
        if (event.isCancelled()) return;
        final List<Block> artisanBlocks = new ArrayList<>();
        final Iterator<Block> iterator = event.blockList().iterator();
        while (iterator.hasNext()) {
            Block block = iterator.next();
            if (isNotTypedArtisanBlock(block, artisanBlockDataClass)) continue;
            artisanBlocks.add(block);
            iterator.remove();
        }
        for (Block artisanBlock : artisanBlocks) {
            ArtisanBlockData artisanBlockData = NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlockData(artisanBlock);
            if (ThreadLocalRandom.current().nextDouble() < event.getYield()) {
                for (ItemStack drop : artisanBlockData.getArtisanBlockState().drops()) {
                    artisanBlock.getWorld().dropItemNaturally(artisanBlock.getLocation(), drop);
                }
            }
            artisanBlock.setType(Material.AIR);
            ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(artisanBlock);
        }
    }

    public static <D extends ArtisanBlockData> void onEntityExplode(EntityExplodeEvent event, Class<D> artisanBlockDataClass) {
        if (event.isCancelled()) return;
        final List<Block> artisanBlocks = new ArrayList<>();
        final Iterator<Block> iterator = event.blockList().iterator();
        while (iterator.hasNext()) {
            Block block = iterator.next();
            if (isNotTypedArtisanBlock(block, artisanBlockDataClass)) continue;
            artisanBlocks.add(block);
            iterator.remove();
        }
        for (Block artisanBlock : artisanBlocks) {
            ArtisanBlockData artisanBlockData = NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlockData(artisanBlock);
            if (ThreadLocalRandom.current().nextDouble() < event.getYield()) {
                for (ItemStack drop : artisanBlockData.getArtisanBlockState().drops()) {
                    artisanBlock.getWorld().dropItemNaturally(artisanBlock.getLocation(), drop);
                }
            }
            artisanBlock.setType(Material.AIR);
            ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(artisanBlock);
        }
    }

    public static <D extends ArtisanBlockData> void onEntityChangeBlock(EntityChangeBlockEvent event, Class<D> artisanBlockDataClass) {
        if (event.isCancelled()) return;
        if (isNotTypedArtisanBlock(event.getBlock(), artisanBlockDataClass)) return;
        if (event.getEntityType() == EntityType.ENDERMAN) {
            event.setCancelled(true);
        } else if (event.getEntityType() == EntityType.WITHER) {
            event.getBlock().setType(Material.AIR);
            ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock());
        }
    }

    public static void place(Block bukkitBlock, ArtisanBlockData artisanBlockData) throws Exception {
        CraftWorld craftWorld = (CraftWorld) bukkitBlock.getWorld();
        Level nmsWorld = craftWorld.getHandle();
        BlockPos pos = new BlockPos(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ());
        ArtisanBlockStorageInternal.getInternal().placeArtisanBlock(nmsWorld, pos, artisanBlockData);
        nmsWorld.setBlock(pos, stateById(artisanBlockData.getArtisanBlockState().actualState()), 3);
        if (artisanBlockData.getArtisanBlock().getPlaceSoundProperty() != null) {
            bukkitBlock.getWorld().playSound(
                    bukkitBlock.getLocation().toBlockLocation(),
                    artisanBlockData.getArtisanBlock().getPlaceSoundProperty().key,
                    artisanBlockData.getArtisanBlock().getPlaceSoundProperty().volume,
                    artisanBlockData.getArtisanBlock().getPlaceSoundProperty().pitch
            );
        }
    }

    public static void replace(Block bukkitBlock, ArtisanBlockData artisanBlockData) {
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
