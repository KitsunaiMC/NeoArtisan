package io.github.moyusowo.neoartisanapi.api.block.base;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ArtisanBlock {

    @NotNull NamespacedKey getBlockId();

    @NotNull ArtisanBlockState getState(int n);

    int getTotalStates();

}

