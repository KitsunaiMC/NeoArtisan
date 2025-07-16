package io.github.moyusowo.neoartisanapi.api.block.block;

public enum ArtisanBlocks {
    CROP(ArtisanCropBlock.class),
    SIMPLE(ArtisanSimpleBlock.class);

    public final Class<? extends ArtisanBaseBlock> theClass;

    ArtisanBlocks(Class<? extends ArtisanBaseBlock> theClass) {
        this.theClass = theClass;
    }
}
