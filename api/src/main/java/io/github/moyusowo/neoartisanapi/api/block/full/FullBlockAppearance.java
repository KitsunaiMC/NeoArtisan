package io.github.moyusowo.neoartisanapi.api.block.full;

import org.jetbrains.annotations.NotNull;

/**
 * 完整自定义方块外观配置（利用原版音符盒的状态）
 * <p>
 * <b>原版状态映射策略：</b>
 * <ul>
 *   <li>原版树叶始终使用instrument=harp的状态，其他为未使用状态</li>
 *   <li>半透明/透明自定义方块可安全占用其他状态作为不同方块状态</li>
 * </ul>
 *
 * @implNote 实际客户端显示需通过资源包覆盖
 * @since 2.0.0
 */
public final class FullBlockAppearance {

    public final @NotNull NoteBlockAppearance noteBlockAppearance;
    public final int note;
    public final boolean powered;

    /**
     * 外观使用的方块状态
     */
    public FullBlockAppearance(@NotNull NoteBlockAppearance noteBlockAppearance, int note, boolean powered) {
        this.noteBlockAppearance = noteBlockAppearance;
        this.note = note;
        this.powered = powered;
    }

    /**
     * 可用的音符盒材质
     */
    @SuppressWarnings("unused")
    public enum NoteBlockAppearance {
        HAT,
        BASEDRUM,
        SNARE,
        BASS,
        FLUTE,
        BELL,
        GUITAR,
        CHIME,
        XYLOPHONE,
        IRON_XYLOPHONE,
        COW_BELL,
        DIDGERIDOO,
        BIT,
        BANJO,
        PLING,
        ZOMBIE,
        SKELETON,
        CREEPER,
        DRAGON,
        WITHER_SKELETON,
        PIGLIN,
        CUSTOM_HEAD
    }
}
