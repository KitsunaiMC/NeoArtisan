package io.github.moyusowo.neoartisan.block.block.listener;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.storage.internal.ArtisanBlockStorageInternal;
import io.github.moyusowo.neoartisan.block.util.InteractionUtil;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.block.ArtisanBlocks;
import io.github.moyusowo.neoartisanapi.api.block.block.ArtisanCropBlock;
import io.github.moyusowo.neoartisanapi.api.block.blockdata.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.event.ArtisanBlockPlaceEvent;
import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.ThreadLocalRandom;

final class CropBlockListener implements Listener {
    private CropBlockListener() {}

    @InitMethod(priority = InitPriority.LISTENER)
    public static void init() {
        NeoArtisan.registerListener(new CropBlockListener());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(PlayerInteractEvent event) {
        // is interact valid
        if (event.useInteractedBlock() == Event.Result.DENY || event.useItemInHand() == Event.Result.DENY) return;
        // ensure right click block
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null) return;
        // ensure player have an item when click
        if (event.getItem() == null) return;
        // is item ArtisanItem
        if (!NeoArtisanAPI.getItemRegistry().isArtisanItem(event.getItem())) return;
        ArtisanItem artisanItem = NeoArtisanAPI.getItemRegistry().getArtisanItem(event.getItem());
        // ensure ArtisanItem connect to ArtisanBlock
        if (artisanItem.getBlockId() == null) return;
        // check if ArtisanCropBlock
        if (!NeoArtisanAPI.getBlockRegistry().isArtisanBlock(artisanItem.getBlockId())) return;
        if (NeoArtisanAPI.getBlockRegistry().getArtisanBlock(artisanItem.getBlockId()).getType() != ArtisanBlocks.CROP) return;
        // check farmland
        if (event.getClickedBlock().getType() != Material.FARMLAND) return;
        // check click position
        if (event.getBlockFace() != BlockFace.UP) return;
        if (event.getClickedBlock().getRelative(BlockFace.UP).getType() != Material.AIR) return;
        // check sneaking interaction
        if (event.getPlayer().isSneaking() && InteractionUtil.isInteractable(event.getClickedBlock())) return;
        // check permission
        if (!NeoArtisanAPI.getBlockProtection().canPlace(event.getPlayer(), event.getClickedBlock().getRelative(event.getBlockFace()).getLocation())) return;
        event.setCancelled(true);
        // call event
        ArtisanBlockPlaceEvent artisanBlockPlaceEvent = new ArtisanBlockPlaceEvent(
                event.getClickedBlock().getRelative(BlockFace.UP),
                event.getClickedBlock().getRelative(BlockFace.UP).getState(),
                event.getClickedBlock(),
                event.getItem(),
                event.getPlayer(),
                true,
                EquipmentSlot.HAND,
                NeoArtisanAPI.getBlockRegistry().getArtisanBlock(artisanItem.getBlockId()),
                ArtisanBlockData.builder().blockId(artisanItem.getBlockId()).location(event.getClickedBlock().getRelative(BlockFace.UP).getLocation()).stage(0).build()
        );
        artisanBlockPlaceEvent.callEvent();
        if (artisanBlockPlaceEvent.isCancelled()) return;
        Util.place(event.getClickedBlock().getRelative(BlockFace.UP), artisanBlockPlaceEvent.getPlacedArtisanBlockData());
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.getItem().setAmount(event.getItem().getAmount() - 1);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityChangeFarmland(EntityChangeBlockEvent event) {
        if (!NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(event.getBlock().getRelative(BlockFace.UP))) return;
        if (event.getBlock().getType() != Material.FARMLAND) return;
        ArtisanBlockData blockData = NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlockData(event.getBlock().getRelative(BlockFace.UP));
        if (blockData.getArtisanBlock().getType() != ArtisanBlocks.CROP) return;
        event.setCancelled(true);
        event.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
        event.getBlock().setType(Material.DIRT);
        for (ItemStack drop: blockData.getArtisanBlockState().drops()) {
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getRelative(BlockFace.UP).getLocation().add(0.5, 0.5, 0.5), drop);
        }
        ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock().getRelative(BlockFace.UP));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFertilize(BlockFertilizeEvent event) {
//        for (BlockState blockState : event.getBlocks()) {
//            if (NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(blockState.getBlock())) {
//                ArtisanBlockData artisanBlockData = NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlockData(blockState.getBlock());
//                if (artisanBlockData.getArtisanBlock().getType() == ArtisanBlocks.CROP) {
//                    ArtisanBlockData formerData = grownCrop.get(blockState.getBlock());
//                    Util.replace(blockState.getBlock(), formerData);
//                    grownCrop.remove(blockState.getBlock());
//                    event.setCancelled(true);
//                    if (hasNextStage(artisanBlockData)) {
//                        Util.replace(blockState.getBlock(), getNextFertilizeStage(artisanBlockData));
//                        playBoneMealEffects(blockState.getLocation());
//                    }
//                }
//            }
//        }
    }

    public static ArtisanBlockData getNextFertilizeStage(ArtisanBlockData artisanBlockData) {
        ArtisanCropBlock artisanCropBlock = (ArtisanCropBlock) artisanBlockData.getArtisanBlock();
        int grow = artisanCropBlock.generateBoneMealGrowth();
        if (grow + artisanBlockData.stage() >= artisanCropBlock.getTotalStates()) grow = artisanCropBlock.getTotalStates() - 1 - artisanBlockData.stage();
        return ArtisanBlockData.builder()
                .blockId(artisanBlockData.blockId())
                .location(artisanBlockData.getLocation())
                .stage(artisanBlockData.stage() + grow)
                .build();
    }

    private static void playBoneMealEffects(Location loc) {
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
