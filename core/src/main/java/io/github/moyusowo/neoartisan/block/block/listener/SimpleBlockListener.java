package io.github.moyusowo.neoartisan.block.block.listener;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.util.BoundingBoxUtil;
import io.github.moyusowo.neoartisan.block.util.InteractionUtil;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.block.base.ArtisanBlocks;
import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.event.common.ArtisanBlockPlaceEvent;
import io.github.moyusowo.neoartisanapi.api.block.protection.Protections;
import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import io.github.moyusowo.neoartisanapi.api.registry.Registries;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

final class SimpleBlockListener implements Listener {
    private SimpleBlockListener() {}

    @InitMethod(priority = InitPriority.LISTENER)
    public static void init() {
        NeoArtisan.registerListener(new SimpleBlockListener());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(PlayerInteractEvent event) {
        // is interact valid
        if (event.useInteractedBlock() == Event.Result.DENY || event.useItemInHand() == Event.Result.DENY) return;
        // ensure right click block
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || event.getClickedBlock() == null) return;
        // ensure player have a item when click
        if (event.getItem() == null) return;
        // ensure the block is air
        if (event.getClickedBlock().getRelative(event.getBlockFace()).getType() != Material.AIR) return;
        // is item ArtisanItem
        if (!Registries.ITEM.isArtisanItem(event.getItem())) return;
        ArtisanItem artisanItem = Registries.ITEM.getArtisanItem(event.getItem());
        // ensure ArtisanItem connect to ArtisanBlock
        if (artisanItem.getBlockId() == null) return;
        // check if ArtisanSimpleBlock
        if (!Registries.BLOCK.isArtisanBlock(artisanItem.getBlockId())) return;
        if (Registries.BLOCK.getArtisanBlock(artisanItem.getBlockId()).getType() != ArtisanBlocks.SIMPLE) return;
        // check sneaking interaction
        if (event.getPlayer().isSneaking() && InteractionUtil.isInteractable(event.getClickedBlock())) return;
        // check overlap
        if (BoundingBoxUtil.overlap(event.getClickedBlock().getRelative(event.getBlockFace()))) return;
        // check permission
        if (!Protections.BLOCK.canPlace(event.getPlayer(), event.getClickedBlock().getRelative(event.getBlockFace()).getLocation())) return;
        event.setCancelled(true);
        // call event
        ArtisanBlockPlaceEvent artisanBlockPlaceEvent = new ArtisanBlockPlaceEvent(
                event.getClickedBlock().getRelative(event.getBlockFace()),
                event.getClickedBlock().getRelative(event.getBlockFace()).getState(),
                event.getClickedBlock(),
                event.getItem(),
                event.getPlayer(),
                true,
                EquipmentSlot.HAND,
                Registries.BLOCK.getArtisanBlock(artisanItem.getBlockId()),
                ArtisanBlockData.builder().blockId(artisanItem.getBlockId()).location(event.getClickedBlock().getRelative(event.getBlockFace()).getLocation()).stage(0).build()
        );
        artisanBlockPlaceEvent.callEvent();
        if (artisanBlockPlaceEvent.isCancelled()) return;
        Util.place(event.getClickedBlock().getRelative(event.getBlockFace()), artisanBlockPlaceEvent.getPlacedArtisanBlockData());
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            event.getItem().setAmount(event.getItem().getAmount() - 1);
        }
    }
}
