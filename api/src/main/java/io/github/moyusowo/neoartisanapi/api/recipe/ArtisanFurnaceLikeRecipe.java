package io.github.moyusowo.neoartisanapi.api.recipe;

import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.Choice;
import org.jetbrains.annotations.NotNull;

public interface ArtisanFurnaceLikeRecipe extends ArtisanRecipe {
    /**
     * 获取烧炼原料物品ID
     *
     * @return 非null的物品命名空间键
     * @implNote 应与 {@link #getInputs()} 的首元素一致
     */
    @NotNull
    Choice getInput();

    /**
     * 获取标准烧炼所需时间（tick）
     *
     * @return 正整数值（1 tick = 0.05秒）
     */
    int getCookTime();

    /**
     * 获取烧炼完成时获得的经验值
     *
     * @return 正浮点数值
     */
    float getExp();

    @NotNull
    default ItemGenerator getResultGenerator() {
        return getResultGenerators().getFirst();
    }
}
