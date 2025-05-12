package io.github.moyusowo.neoartisanapi.api.block.crop;

import io.github.moyusowo.neoartisanapi.api.attribute.AttributeRegistry;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;

public interface ArtisanCropStorage {

    static ArtisanCropStorage getArtisanCropStorageManager() {
        return Bukkit.getServicesManager().load(ArtisanCropStorage.class);
    }

    CurrentCropStage getArtisanCropStage(World world, int x, int y, int z);

    CurrentCropStage getArtisanCropStage(Block block);

    boolean isArtisanCrop(Block block);

    boolean isArtisanCrop(World world, int x, int y, int z);
}
