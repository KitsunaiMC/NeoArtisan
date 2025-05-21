package io.github.moyusowo.neoartisanapi.api.block.storage;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCropData;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.ApiStatus;

public interface ArtisanBlockStorage {

    ArtisanBlockData getArtisanBlock(World world, int x, int y, int z);

    ArtisanBlockData getArtisanBlock(Block block);

    boolean isArtisanBlock(Block block);

    boolean isArtisanBlock(World world, int x, int y, int z);

    @ApiStatus.Experimental
    default boolean isArtisanCrop(Block block) {
        return this.isArtisanCrop(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    @ApiStatus.Experimental
    default boolean isArtisanCrop(World world, int x, int y, int z) {
        return this.getArtisanBlock(world, x, y, z) instanceof ArtisanCropData;
    }
}
