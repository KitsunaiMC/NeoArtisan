package io.github.moyusowo.neoartisanapi.api.block.state.base;

import io.github.moyusowo.neoartisanapi.api.block.util.PistonMoveBlockReaction;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * 表示自定义方块的一个特定状态，包含视觉表现和实际行为定义。
 * <p>
 * 每个状态对应方块在特定条件下的：
 * <ul>
 *   <li><b>视觉表现</b> - 客户端看到的方块外观</li>
 *   <li><b>实际状态</b> - 服务端使用的逻辑状态</li>
 *   <li><b>掉落物</b> - 被破坏时产生的物品</li>
 *   <li><b>部分交互表现</b> - 活塞推动、是否可然、是否漂浮等</li>
 * </ul>
 * </p>
 *
 */
public interface ArtisanBaseBlockState {
    /**
     * 获取客户端显示的Minecraft Internal BlockState ID
     * <p>
     * 对应NMS中的 {@code BlockState} 数字ID，用于客户端渲染。
     * </p>
     *
     * @return NMS方块状态数字ID
     */
    int appearanceState();

    /**
     * 获取服务端使用的实际Minecraft Internal BlockState ID
     * <p>
     * 用于服务端逻辑计算，可能与视觉状态不同。
     * </p>
     *
     * @return 实际逻辑状态对应的NMS方块状态ID
     */
    int actualState();

    /**
     * 获取该方块状态被破坏时的掉落物
     * <p>
     * 掉落物通过 {@link ItemGenerator} 动态生成，支持条件化掉落。
     * 返回数组可能为空（表示无掉落），但不会为 {@code null}。
     * </p>
     *
     * @return 新生成的物品堆副本（安全修改）
     */
    @NotNull
    Collection<ItemStack> drops();

    /**
     *
     * @return 该状态下方块与活塞交互的类型
     */
    @NotNull
    PistonMoveBlockReaction pistonMoveReaction();

    /**
     *
     * @return 该状态下方块是否能被流体破坏
     */
    boolean isFlowBreaking();

    /**
     *
     * @return 该状态下方块是否能悬空放置
     */
    boolean canSurviveFloating();

    /**
     *
     * @return 该状态下方块的亮度，{@code null} 表示采用原版的亮度
     */
    @Nullable
    Integer getBrightness();

    /**
     *
     * @return 该状态下方块的亮度，{@code null} 表示采用原版的硬度
     */
    @Nullable
    Integer getHardness();

    @NotNull
    ArtisanBlockStates getType();
}
