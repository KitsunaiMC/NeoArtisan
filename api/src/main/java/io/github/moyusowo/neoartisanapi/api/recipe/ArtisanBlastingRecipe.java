package io.github.moyusowo.neoartisanapi.api.recipe;

import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.util.BuilderFactoryUtil;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * 高炉烧炼配方接口，继承自基础 {@link ArtisanRecipe}。
 *
 * <p><b>配方特性说明：</b></p>
 * <ul>
 *   <li>支持自定义烧炼时间（tick）</li>
 *   <li>可配置经验值产出</li>
 *   <li>相当于覆写了原版的MaterialChoice逻辑，原版的MaterialChoice不再适用！</li>
 * </ul>
 *
 * @see ArtisanRecipe 基础配方接口
 */
public interface ArtisanBlastingRecipe extends ArtisanRecipe {
    @NotNull
    static Builder builder() {
        return BuilderFactoryUtil.getBuilder(BuilderFactory.class).builder();
    }

    interface BuilderFactory {
        @NotNull
        Builder builder();
    }

    /**
     * 获取烧炼原料物品ID
     *
     * @return 非null的物品命名空间键
     * @implNote 应与 {@link #getInputs()} 的首元素一致
     */
    @NotNull
    NamespacedKey getInput();

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

    /**
     * 营火配方建造器接口
     *
     * <p><b>必须设置全部项！</b></p>
     */
    interface Builder {

        /**
         * 设置配方唯一标识符
         *
         * @param key 符合命名空间规范的键（非null）
         * @return 当前建造器实例
         */
        @NotNull Builder key(NamespacedKey key);

        /**
         * 设置烧炼原料物品ID
         *
         * @param inputItemId 原料物品ID（非null）
         * @return 当前建造器实例
         * @throws IllegalArgumentException 如果物品未注册
         */
        @NotNull Builder inputItemId(NamespacedKey inputItemId);

        /**
         * 设置烧炼结果生成器
         *
         * @param resultGenerator 生成器实例（非null）
         * @return 当前建造器实例
         * @see ItemGenerator 生成器接口
         */
        @NotNull Builder resultGenerator(ItemGenerator resultGenerator);

        /**
         * 设置标准烧炼时间
         *
         * @param cookTime 烧炼ticks数（≥1）
         * @return 当前建造器实例
         */
        @NotNull Builder cookTime(int cookTime);

        /**
         * 设置烧炼经验值
         *
         * @param exp 获得经验值（≥0）
         * @return 当前建造器实例
         */
        @NotNull Builder exp(float exp);

        /**
         * 构建不可变的熔炉配方实例
         *
         * @return 配置完成的配方实例
         * @throws IllegalCallerException 如果缺少必要参数
         * @implSpec 实现必须保证烧炼时间与经验值的精度
         */
        @NotNull ArtisanBlastingRecipe build();
    }
}
