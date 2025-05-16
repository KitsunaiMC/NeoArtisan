package io.github.moyusowo.neoartisanapi.api.block.crop;

import org.bukkit.World;
import org.bukkit.block.Block;

public interface ArtisanCropStorage {

    CurrentCropStage getArtisanCropStage(World world, int x, int y, int z);

    CurrentCropStage getArtisanCropStage(Block block);

    boolean isArtisanCrop(Block block);

    boolean isArtisanCrop(World world, int x, int y, int z);
}
