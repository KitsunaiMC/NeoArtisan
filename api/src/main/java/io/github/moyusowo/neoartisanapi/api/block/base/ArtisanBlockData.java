package io.github.moyusowo.neoartisanapi.api.block.base;

import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCrop;
import io.github.moyusowo.neoartisanapi.api.block.gui.ArtisanBlockGUI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public interface ArtisanBlockData {

    @Nullable ArtisanBlockGUI getGUI();

    Location getLocation();

    NamespacedKey blockId();

    int stage();

    ArtisanBlock getArtisanBlock();

    ArtisanBlockState getArtisanBlockState();

    PersistentDataContainer getPersistentDataContainer();
}
