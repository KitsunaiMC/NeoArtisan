package io.github.moyusowo.neoartisanapi.api.block.storage;

import io.github.moyusowo.neoartisanapi.api.block.ArtisanBlockState;
import io.github.moyusowo.neoartisanapi.api.block.crop.CurrentCropStage;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.ApiStatus;

public interface ArtisanBlockStorage {

    ArtisanBlockState getArtisanBlock(World world, int x, int y, int z);

    ArtisanBlockState getArtisanBlock(Block block);

    boolean isArtisanBlock(Block block);

    boolean isArtisanBlock(World world, int x, int y, int z);

    @ApiStatus.Experimental
    default boolean isArtisanCrop(Block block) {
        return this.isArtisanBlock(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    @ApiStatus.Experimental
    default boolean isArtisanCrop(World world, int x, int y, int z) {
        return this.getArtisanBlock(world, x, y, z) instanceof CurrentCropStage;
    }
}
