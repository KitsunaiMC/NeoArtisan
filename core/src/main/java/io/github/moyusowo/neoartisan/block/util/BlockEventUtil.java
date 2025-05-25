package io.github.moyusowo.neoartisan.block.util;

import io.github.moyusowo.neoartisan.block.storage.internal.ArtisanBlockStorageInternal;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlock;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.event.ArtisanBlockPlaceEvent;
import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.concurrent.ThreadLocalRandom;

import static io.github.moyusowo.neoartisan.block.util.BlockStateUtil.stateById;

public final class BlockEventUtil {

    public static <B extends ArtisanBlock> boolean canNotPlaceBasicCheck(PlayerInteractEvent event, Class<B> blockClass) {
        if (event.useInteractedBlock() == Event.Result.DENY) return true;
        if (event.useItemInHand() == Event.Result.DENY) return true;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return true;
        if (!NeoArtisanAPI.getItemRegistry().isArtisanItem(event.getItem())) return true;
        ArtisanItem artisanItem = NeoArtisanAPI.getItemRegistry().getArtisanItem(event.getItem());
        if (artisanItem.getBlockId() == null) return true;
        if (!NeoArtisanAPI.getBlockRegistry().isArtisanBlock(artisanItem.getBlockId())) return true;
        if (!blockClass.isInstance(NeoArtisanAPI.getBlockRegistry().getArtisanBlock(artisanItem.getBlockId()))) return true;
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
