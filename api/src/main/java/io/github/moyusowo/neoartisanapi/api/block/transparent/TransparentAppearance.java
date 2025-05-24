package io.github.moyusowo.neoartisanapi.api.block.transparent;

import org.jetbrains.annotations.NotNull;

public final class TransparentAppearance {

    public final @NotNull LeavesAppearance leavesAppearance;
    public final int distance;
    public final boolean persistent;
    public final boolean waterlogged;

    public TransparentAppearance(@NotNull LeavesAppearance leavesAppearance, int distance, boolean persistent, boolean waterlogged) {
        if (distance == 7 && persistent) throw new IllegalArgumentException();
        this.leavesAppearance = leavesAppearance;
        this.distance = distance;
        this.persistent = persistent;
        this.waterlogged = waterlogged;
    }

    public enum LeavesAppearance {
        OAK_LEAVES,
        ACACIA_LEAVES,
        JUNGLE_LEAVES,
        BIRCH_LEAVES,
        MANGROVE_LEAVES,
        CHERRY_LEAVES,
        DARK_OAK_LEAVES,
        AZALEA_LEAVES,
        FLOWERING_AZALEA_LEAVES,
        SPRUCE_LEAVES,
        PALE_OAK_LEAVES
    }

}
