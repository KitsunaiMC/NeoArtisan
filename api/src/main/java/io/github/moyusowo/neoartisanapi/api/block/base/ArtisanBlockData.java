package io.github.moyusowo.neoartisanapi.api.block.base;

import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCrop;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;

@SuppressWarnings("unused")
public interface ArtisanBlockData {

    NamespacedKey blockId();

    int stage();

    ArtisanBlock getArtisanBlock();

    ArtisanBlockState getArtisanBlockState();

    PersistentDataContainer getPersistentDataContainer();
}
