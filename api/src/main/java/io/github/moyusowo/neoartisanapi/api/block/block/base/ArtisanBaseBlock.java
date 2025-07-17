package io.github.moyusowo.neoartisanapi.api.block.block.base;

import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.block.util.SoundProperty;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 自定义方块的基础接口，定义所有自定义方块的类型，如作物、机器等。
 *
 * <p>本接口只沟通方块状态之间的转变，或方块类型整体的行为。</p>
 *
 * <p>
 * 每个自定义方块实例应当是不可变的，所有状态变更应通过 {@link ArtisanBlockData} 处理。
 * </p>
 *
 * @see ArtisanBlockData 在世界中的方块数据
 */
public interface ArtisanBaseBlock {
    /**
     * 获取此自定义方块的唯一标识符
     * <p>
     * 该ID用于持久化存储和跨模块引用，应当全局唯一且恒定不变。
     * </p>
     *
     * @return 不可变的命名空间键
     */
    @NotNull
    NamespacedKey getBlockId();

    /**
     * 获取指定状态值的方块状态实例
     *
     * <p>
     * 状态值通常是连续整数，用于表示方块的视觉或逻辑变化阶段
     * （如作物生长阶段、机械工作状态等）。
     * </p>
     *
     * @param n 状态索引（0 <= n < {@link #getTotalStates()}）
     * @return 对应的不可变状态实例
     */
    @NotNull
    ArtisanBaseBlockState getState(int n);

    /**
     * 获取此方块支持的总状态数
     * <p>
     * 状态数决定了方块的视觉/逻辑变化粒度。例如：
     * <ul>
     *   <li>普通方块：通常为1（单一状态）</li>
     *   <li>生长作物：等于生长阶段数（如小麦为8）</li>
     *   <li>状态机方块：等于所有可能状态组合数</li>
     * </ul>
     * </p>
     *
     * @return 正整数，表示方块状态总数
     * @implNote 该值在生命周期内必须恒定不变
     */
    int getTotalStates();

    /**
     * 以字符串形式获取自定义方块放置方块时音效的键
     *
     * @return 对应自定义方块被放置时音效的命名空间键，如果没有则为空
     */
    @Nullable
    SoundProperty getPlaceSoundProperty();

    @NotNull
    ArtisanBlocks getType();
}
