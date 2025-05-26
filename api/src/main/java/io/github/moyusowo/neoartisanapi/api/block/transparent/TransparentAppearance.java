package io.github.moyusowo.neoartisanapi.api.block.transparent;

import org.jetbrains.annotations.NotNull;

/**
 * 半透明/透明自定义方块外观配置（利用原版树叶的状态）
 * <p>
 * <b>原版状态映射策略：</b>
 * <ul>
 *   <li>原版树叶始终使用persistent=true,distance=7的状态，其他为未使用状态</li>
 *   <li>半透明/透明自定义方块可安全占用其他状态作为不同方块状态</li>
 *   <li>传入persistent=true,distance=7将抛出异常（避免与原版冲突）</li>
 * </ul>
 *
 * @implNote 实际客户端显示需通过资源包覆盖
 */
public final class TransparentAppearance {

    public final @NotNull LeavesAppearance leavesAppearance;
    public final int distance;
    public final boolean persistent;
    public final boolean waterlogged;

    /**
     * 外观使用的方块状态
     */
    public TransparentAppearance(@NotNull LeavesAppearance leavesAppearance, int distance, boolean persistent, boolean waterlogged) {
        if (distance == 7 && persistent) throw new IllegalArgumentException();
        this.leavesAppearance = leavesAppearance;
        this.distance = distance;
        this.persistent = persistent;
        this.waterlogged = waterlogged;
    }

    /**
     * 可用的树叶材质
     */
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
