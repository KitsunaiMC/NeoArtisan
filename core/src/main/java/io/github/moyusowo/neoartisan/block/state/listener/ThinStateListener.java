package io.github.moyusowo.neoartisan.block.state.listener;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBlockStates;
import io.github.moyusowo.neoartisanapi.api.block.storage.Storages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;

final class ThinStateListener implements Listener {
    private ThinStateListener() {}

    @InitMethod(priority = InitPriority.LISTENER)
    public static void init() {
        NeoArtisan.registerListener(new ThinStateListener());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onStepOn(BlockRedstoneEvent event) {
        if (!Storages.BLOCK.isArtisanBlock(event.getBlock())) return;
        ArtisanBlockData artisanBlockData = Storages.BLOCK.getArtisanBlockData(event.getBlock());
        if (artisanBlockData.getArtisanBlockState().getType() == ArtisanBlockStates.THIN) {
            event.setNewCurrent(0);
        }
    }
}
