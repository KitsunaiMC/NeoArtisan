package io.github.moyusowo.neoartisan.item.listener;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

final class BlockPlaceListener implements Listener {
    @InitMethod(priority = InitPriority.LISTENER)
    static void init() { NeoArtisan.registerListener(new BlockPlaceListener()); }

    private BlockPlaceListener() {}

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) return;
        if (!NeoArtisanAPI.getItemRegistry().isArtisanItem(event.getItemInHand())) return;
        event.setCancelled(true);
    }

}
