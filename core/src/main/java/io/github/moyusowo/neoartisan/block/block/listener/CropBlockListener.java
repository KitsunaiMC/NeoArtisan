package io.github.moyusowo.neoartisan.block.block.listener;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.storage.internal.ArtisanBlockStorageInternal;
import io.github.moyusowo.neoartisan.block.util.InteractionUtil;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.block.base.ArtisanBlocks;
import io.github.moyusowo.neoartisanapi.api.block.block.ArtisanCropBlock;
import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.event.common.ArtisanBlockPlaceEvent;
import io.github.moyusowo.neoartisanapi.api.block.protection.Protections;
import io.github.moyusowo.neoartisanapi.api.block.storage.Storages;
import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import io.github.moyusowo.neoartisanapi.api.registry.Registries;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

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
        if (!Registries.ITEM.isArtisanItem(event.getItem())) return;
        ArtisanItem artisanItem = Registries.ITEM.getArtisanItem(event.getItem());
        // ensure ArtisanItem connect to ArtisanBlock
        if (artisanItem.getBlockId() == null) return;
        // check if ArtisanCropBlock
        if (!Registries.BLOCK.isArtisanBlock(artisanItem.getBlockId())) return;
        if (Registries.BLOCK.getArtisanBlock(artisanItem.getBlockId()).getType() != ArtisanBlocks.CROP) return;
        // check farmland
        if (event.getClickedBlock().getType() != Material.FARMLAND) return;
        // check click position
        if (event.getBlockFace() != BlockFace.UP) return;
        if (event.getClickedBlock().getRelative(BlockFace.UP).getType() != Material.AIR) return;
        // check sneaking interaction
        if (event.getPlayer().isSneaking() && InteractionUtil.isInteractable(event.getClickedBlock())) return;
        // check permission
        if (!Protections.BLOCK.canPlace(event.getPlayer(), event.getClickedBlock().getRelative(event.getBlockFace()).getLocation())) return;
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
                Registries.BLOCK.getArtisanBlock(artisanItem.getBlockId()),
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
        if (!Storages.BLOCK.isArtisanBlock(event.getBlock().getRelative(BlockFace.UP))) return;
        if (event.getBlock().getType() != Material.FARMLAND) return;
        ArtisanBlockData blockData = Storages.BLOCK.getArtisanBlockData(event.getBlock().getRelative(BlockFace.UP));
        if (blockData.getArtisanBlock().getType() != ArtisanBlocks.CROP) return;
        event.setCancelled(true);
        event.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
        event.getBlock().setType(Material.DIRT);
        for (ItemStack drop: blockData.getArtisanBlockState().drops()) {
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getRelative(BlockFace.UP).getLocation().add(0.5, 0.5, 0.5), drop);
        }
        ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock().getRelative(BlockFace.UP));
        if (event.getEntity() instanceof Player player) {
            Bukkit.getScheduler().runTask(NeoArtisan.instance(), () -> player.setVelocity(new Vector(0, 0.1, 0)));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onFertilize(PlayerInteractEvent event) {
        // is interact valid
        if (event.useInteractedBlock() == Event.Result.DENY || event.useItemInHand() == Event.Result.DENY) return;
        // ensure right click block
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null) return;
        // ensure player have an item when click
        if (event.getItem() == null) return;
        // is item boneMeal
        if (!Registries.ITEM.getRegistryId(event.getItem()).equals(Material.BONE_MEAL.getKey())) return;
        // check if ArtisanCropBlock
        if (!Storages.BLOCK.isArtisanBlock(event.getClickedBlock())) return;
        if (Storages.BLOCK.getArtisanBlockData(event.getClickedBlock()).getArtisanBlock().getType() != ArtisanBlocks.CROP) return;
        // check permission
        if (!Protections.BLOCK.canPlace(event.getPlayer(), event.getClickedBlock().getRelative(event.getBlockFace()).getLocation())) return;
        event.setCancelled(true);
        ArtisanBlockData artisanBlockData = Storages.BLOCK.getArtisanBlockData(event.getClickedBlock());
        if (Util.hasNextStage(artisanBlockData)) {
            Util.replace(event.getClickedBlock(), getNextFertilizeStage(artisanBlockData));
            playBoneMealEffects(event.getClickedBlock().getLocation());
        }
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.getItem().setAmount(event.getItem().getAmount() - 1);
        }
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
