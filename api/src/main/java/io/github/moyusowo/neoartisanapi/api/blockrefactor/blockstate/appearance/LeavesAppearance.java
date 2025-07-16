package io.github.moyusowo.neoartisanapi.api.blockrefactor.blockstate.appearance;

import org.jetbrains.annotations.NotNull;

public record LeavesAppearance(
        @NotNull LeavesMaterial leavesAppearance,
        int distance,
        boolean persistent,
        boolean waterlogged
) {
    /**
     * 外观使用的方块状态
     */
    public LeavesAppearance {
        if (distance == 7 && persistent) throw new IllegalArgumentException();
    }

    /**
     * 可用的树叶材质
     */
    public enum LeavesMaterial {
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
