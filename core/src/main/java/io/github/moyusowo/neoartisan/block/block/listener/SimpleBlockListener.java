package io.github.moyusowo.neoartisan.block.block.listener;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.util.BoundingBoxUtil;
import io.github.moyusowo.neoartisan.block.util.InteractionUtil;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.block.ArtisanBlocks;
import io.github.moyusowo.neoartisanapi.api.block.blockdata.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.event.ArtisanBlockPlaceEvent;
import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import org.bukkit.GameMode;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public final class SimpleBlockListener implements Listener {
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
        // is item ArtisanItem
        if (!NeoArtisanAPI.getItemRegistry().isArtisanItem(event.getItem())) return;
        ArtisanItem artisanItem = NeoArtisanAPI.getItemRegistry().getArtisanItem(event.getItem());
        // ensure ArtisanItem connect to ArtisanBlock
        if (artisanItem.getBlockId() == null) return;
        // check if ArtisanSimpleBlock
        if (!NeoArtisanAPI.getBlockRegistry().isArtisanBlock(artisanItem.getBlockId())) return;
        if (NeoArtisanAPI.getBlockRegistry().getArtisanBlock(artisanItem.getBlockId()).getType() != ArtisanBlocks.SIMPLE) return;
        // check sneaking interaction
        if (event.getPlayer().isSneaking() && InteractionUtil.isInteractable(event.getClickedBlock())) return;
        // check overlap
        if (BoundingBoxUtil.overlap(event.getClickedBlock().getRelative(event.getBlockFace()))) return;
        // check permission
        if (!NeoArtisanAPI.getBlockProtection().canPlace(event.getPlayer(), event.getClickedBlock().getRelative(event.getBlockFace()).getLocation())) return;
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
                NeoArtisanAPI.getBlockRegistry().getArtisanBlock(artisanItem.getBlockId()),
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
