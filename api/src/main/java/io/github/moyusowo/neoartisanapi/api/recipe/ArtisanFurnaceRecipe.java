package io.github.moyusowo.neoartisanapi.api.recipe;

import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.Choice;
import io.github.moyusowo.neoartisanapi.api.util.BuilderFactoryUtil;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * 熔炉烧炼配方接口，继承自基础 {@link ArtisanRecipe}。
 *
 * <p><b>配方特性说明：</b></p>
 * <ul>
 *   <li>支持自定义烧炼时间（tick）</li>
 *   <li>可配置经验值产出</li>
 *   <li>相当于覆写了原版的MaterialChoice逻辑，原版的MaterialChoice不再适用！</li>
 * </ul>
 *
 * <p><b>典型示例：</b></p>
 * <pre>{@code
 * ArtisanFurnaceRecipe.builder()
 *     .key(new NamespacedKey("myplugin", "cooked_iron"))
 *     .inputItemId(Material.IRON_ORE.getKey())
 *     .cookTime(200)  // 原版煤炭烧炼时间为200tick
 *     .exp(0.7f)      // 产出经验值
 *     .resultGenerator(() -> new ItemStack(Material.IRON_INGOT))
 *     .build();
 * }</pre>
 *
 * @see ArtisanRecipe 基础配方接口
 */
@ApiStatus.NonExtendable
public interface ArtisanFurnaceRecipe extends ArtisanFurnaceLikeRecipe {
    @NotNull
    static Builder builder() {
        return BuilderFactoryUtil.getBuilder(BuilderFactory.class).builder();
    }

    interface BuilderFactory {
        @NotNull
        Builder builder();
    }

    @NotNull
    default NamespacedKey getKey() {
        return RecipeType.FURNACE;
    }

    /**
     * 熔炉配方建造器接口
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
        @NotNull Builder key(@NotNull NamespacedKey key);

        /**
         * 设置烧炼原料物品ID
         *
         * @param choice 原料物品选择器（非null）
         * @return 当前建造器实例
         * @throws IllegalArgumentException 如果物品未注册
         */
        @NotNull Builder input(@NotNull Choice choice);

        /**
         * 设置烧炼结果生成器
         *
         * @param resultGenerator 生成器实例（非null）
         * @return 当前建造器实例
         * @see ItemGenerator 生成器接口
         */
        @NotNull Builder resultGenerator(@NotNull ItemGenerator resultGenerator);

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
        @NotNull ArtisanFurnaceRecipe build();
    }
}
