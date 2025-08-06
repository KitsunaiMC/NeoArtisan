package io.github.moyusowo.neoartisan.block.block.listener;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.storage.internal.ArtisanBlockStorageInternal;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.storage.Storages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BaseBlockListener implements Listener {
    private BaseBlockListener() {}

    @InitMethod(priority = InitPriority.LISTENER)
    public static void init() { NeoArtisan.registerListener(new BaseBlockListener()); }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onArtisanBlockReplaced(BlockPlaceEvent event) {
        if (Storages.BLOCK.isArtisanBlock(event.getBlock())) {
            ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock());
        }
    }
}
