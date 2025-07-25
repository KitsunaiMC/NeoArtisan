package io.github.moyusowo.neoartisan.block.state.listener;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.state.ArtisanLeavesState;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBlockStates;
import io.github.moyusowo.neoartisanapi.api.block.storage.Storages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFertilizeEvent;
import org.bukkit.event.block.BlockIgniteEvent;

final class LeavesStateListener implements Listener {
    private LeavesStateListener() {}

    @InitMethod(priority = InitPriority.LISTENER)
    public static void init() {
        NeoArtisan.registerListener(new LeavesStateListener());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onIgnite(BlockIgniteEvent event) {
        if (!Storages.BLOCK.isArtisanBlock(event.getBlock())) return;
        ArtisanBlockData artisanBlockData = Storages.BLOCK.getArtisanBlockData(event.getBlock());
        if (artisanBlockData.getArtisanBlockState() instanceof ArtisanLeavesState artisanLeavesState) {
            if (!artisanLeavesState.canBurn()) event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBurn(BlockBurnEvent event) {
        if (!Storages.BLOCK.isArtisanBlock(event.getBlock())) return;
        ArtisanBlockData artisanBlockData = Storages.BLOCK.getArtisanBlockData(event.getBlock());
        if (artisanBlockData.getArtisanBlockState() instanceof ArtisanLeavesState artisanLeavesState) {
            if (!artisanLeavesState.canBurn()) event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFFertilize(BlockFertilizeEvent event) {
        if (!Storages.BLOCK.isArtisanBlock(event.getBlock())) return;
        ArtisanBlockData artisanBlockData = Storages.BLOCK.getArtisanBlockData(event.getBlock());
        if (artisanBlockData.getArtisanBlockState().getType() == ArtisanBlockStates.LEAVES) {
            event.setCancelled(true);
        }
    }
}
