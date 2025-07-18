package io.github.moyusowo.neoartisan.block.state.listener;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.state.ArtisanLeavesState;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBlockStates;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;

final class LeavesStateListener implements Listener {
    private LeavesStateListener() {}

    @InitMethod(priority = InitPriority.LISTENER)
    public static void init() {
        NeoArtisan.registerListener(new LeavesStateListener());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onIgnite(BlockIgniteEvent event) {
        if (!NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(event.getBlock())) return;
        ArtisanBlockData artisanBlockData = NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlockData(event.getBlock());
        if (artisanBlockData.getArtisanBlockState() instanceof ArtisanLeavesState artisanLeavesState) {
            if (!artisanLeavesState.canBurn()) event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBurn(BlockBurnEvent event) {
        if (!NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(event.getBlock())) return;
        ArtisanBlockData artisanBlockData = NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlockData(event.getBlock());
        if (artisanBlockData.getArtisanBlockState() instanceof ArtisanLeavesState artisanLeavesState) {
            if (!artisanLeavesState.canBurn()) event.setCancelled(true);
        }
    }
}
