package io.github.moyusowo.neoartisanapi.api.block.blockstate;

public enum ArtisanBlockStates {
    COMMON(ArtisanCommonState.class),
    CROP(ArtisanCropState.class),
    LEAVES(ArtisanLeavesState.class),
    SKULL(ArtisanSkullState.class),
    THIN(ArtisanThinState.class);

    public final Class<? extends ArtisanBaseBlockState> theClass;

    ArtisanBlockStates(Class<? extends ArtisanBaseBlockState> theClass) {
        this.theClass = theClass;
    }
}
