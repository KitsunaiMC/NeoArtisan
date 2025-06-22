package io.github.moyusowo.neoartisanapi.api.block.base;

import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * 表示自定义方块的一个特定状态，包含视觉表现和实际行为定义。
 * <p>
 * 每个状态对应方块在特定条件下的：
 * <ul>
 *   <li><b>视觉表现</b> - 客户端看到的方块外观</li>
 *   <li><b>实际状态</b> - 服务端使用的逻辑状态</li>
 *   <li><b>掉落物</b> - 被破坏时产生的物品</li>
 * </ul>
 * 所有实现必须继承 {@link ArtisanBlockStateBase} 而非直接实现此接口。
 * </p>
 *
 * @see ArtisanBlockStateBase 必须继承的基础实现
 * @since 1.0.0
 */
public interface ArtisanBlockState {

    /**
     * 获取客户端显示的方块状态ID（内部使用）
     * <p>
     * 对应NMS中的 {@code BlockState} 数字ID，用于客户端渲染。
     * </p>
     *
     * @return NMS方块状态数字ID
     * @apiNote 此值仅用于网络数据包处理
     */
    @ApiStatus.Internal
    int appearanceState();

    /**
     * 获取服务端使用的实际方块状态ID（内部使用）
     * <p>
     * 用于服务端逻辑计算，可能与视觉状态不同。
     * </p>
     *
     * @return 实际逻辑状态对应的NMS方块状态ID
     */
    @ApiStatus.Internal
    int actualState();

    /**
     * 获取该方块状态被破坏时的掉落物
     * <p>
     * 掉落物通过 {@link ItemGenerator} 动态生成，支持条件化掉落。
     * 返回数组可能为空（表示无掉落），但不会为 {@code null}。
     * </p>
     *
     * @return 新生成的物品堆数组副本（安全修改）
     * @implSpec 实现必须保证每次调用返回新数组
     */
    @NotNull ItemStack[] drops();

    @ApiStatus.Internal
    interface BaseBuilder {}

}