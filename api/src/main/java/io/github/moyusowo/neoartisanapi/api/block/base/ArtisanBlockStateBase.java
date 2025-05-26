package io.github.moyusowo.neoartisanapi.api.block.base;

import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * {@link ArtisanBlockState} 的基础抽象实现，提供状态核心逻辑。
 * <p>
 * <b>实现规范：</b>
 * <ol>
 *   <li>所有具体状态必须继承此类</li>
 *   <li>通过 {@link #ArtisanBlockStateBase(int, int, ItemGenerator[])} 构造</li>
 *   <li>{@code appearanceState} 和 {@code actualState} 必须有效对应已注册的NMS状态ID</li>
 *   <li>状态实例应当是不可变的</li>
 * </ol>
 *
 * @since 1.0.0
 */
public abstract class ArtisanBlockStateBase implements ArtisanBlockState {

    private final int appearanceState, actualState;
    private final ItemGenerator[] generators;

    protected ArtisanBlockStateBase(int appearanceState, int actualState, ItemGenerator[] generators) {
        this.appearanceState = appearanceState;
        this.actualState = actualState;
        this.generators = Arrays.copyOf(generators, generators.length);
    }

    @Override
    public int appearanceState() {
        return this.appearanceState;
    }

    @Override
    public int actualState() {
        return this.actualState;
    }

    @Override
    public @NotNull ItemStack[] drops() {
        ItemStack[] drops = new ItemStack[generators.length];
        for (int i = 0; i < drops.length; i++) {
            drops[i] = generators[i].generate();
        }
        return drops;
    }

}
