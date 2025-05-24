package io.github.moyusowo.neoartisanapi.api.block.base;

import io.github.moyusowo.neoartisanapi.api.block.gui.ArtisanBlockGUI;
import io.github.moyusowo.neoartisanapi.api.block.gui.GUICreator;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ArtisanBlock {

    @Nullable ArtisanBlockGUI createGUI(Location location);

    @NotNull NamespacedKey getBlockId();

    @NotNull ArtisanBlockState getState(int n);

    int getTotalStates();

}

