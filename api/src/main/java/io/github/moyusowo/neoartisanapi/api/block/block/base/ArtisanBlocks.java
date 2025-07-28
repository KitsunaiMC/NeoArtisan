package io.github.moyusowo.neoartisanapi.api.block.block.base;

import io.github.moyusowo.neoartisanapi.api.block.block.ArtisanCropBlock;
import io.github.moyusowo.neoartisanapi.api.block.block.ArtisanSimpleBlock;

/**
 * Enum representing types of custom blocks.
 */
public enum ArtisanBlocks {
    CROP(ArtisanCropBlock.class),
    SIMPLE(ArtisanSimpleBlock.class);

    public final Class<? extends ArtisanBaseBlock> theClass;

    ArtisanBlocks(Class<? extends ArtisanBaseBlock> theClass) {
        this.theClass = theClass;
    }
}
