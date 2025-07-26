package io.github.moyusowo.neoartisanapi.api.recipe;

import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.Choice;
import io.github.moyusowo.neoartisanapi.api.util.BuilderFactoryUtil;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * 无序合成配方接口，继承自基础 {@link ArtisanRecipe}。
 *
 * <p><b>配方特性说明：</b></p>
 * <ul>
 *   <li>不限制材料摆放顺序</li>
 *   <li>自动处理材料堆叠数量</li>
 *   <li>支持批量材料添加</li>
 * </ul>
 *
 * <p><b>典型示例：</b></p>
 * <pre>{@code
 * ArtisanShapelessRecipe.builder()
 *     .key(new NamespacedKey("myplugin", "magic_powder"))
 *     .add(Material.REDSTONE)
 *     .add(Material.GLOWSTONE_DUST, 2)
 *     .resultGenerator(() -> new ItemStack(Material.GLOWSTONE))
 *     .build();
 * }</pre>
 *
 * @see ArtisanRecipe 基础配方接口
 */
@ApiStatus.NonExtendable
public interface ArtisanShapelessRecipe extends ArtisanRecipe {
    @NotNull
    static Builder builder() {
        return BuilderFactoryUtil.getBuilder(BuilderFactory.class).builder();
    }

    @NotNull
    default NamespacedKey getKey() {
        return RecipeType.SHAPELESS;
    }

    @NotNull
    default ItemGenerator getResultGenerator() {
        return getResultGenerators().getFirst();
    }

    /**
     * 建造器工厂接口
     * <p>
     * 负责创建全新的 {@link Builder} 实例，确保每次构建过程独立。
     * 实现必须保证每次调用 {@link #builder()} 都返回<strong>未使用过</strong>的构建器。
     * </p>
     *
     * <p><b>实现要求：</b></p>
     * <ol>
     *   <li>禁止返回单例或共享实例</li>
     *   <li>构建器初始状态必须完全重置</li>
     * </ol>
     */
    interface BuilderFactory {
        @NotNull
        Builder builder();
    }

    /**
     * 无序配方建造器接口
     *
     * <p><b>构建流程：</b></p>
     * <ol>
     *   <li>必须设置 {@link #key(NamespacedKey)}</li>
     *   <li>必须添加至少1个材料</li>
     *   <li>必须设置 {@link #resultGenerator(ItemGenerator)}</li>
     * </ol>
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
         * 添加单个材料
         *
         * @param choice 材料选择（非null）
         * @return 当前建造器实例
         * @see #add(Choice, int) 指定数量的版本
         */
        @NotNull Builder add(@NotNull Choice choice);

        /**
         * 批量添加多个材料
         *
         * @param choices 材料选择数组（非null）
         * @return 当前建造器实例
         */
        @NotNull Builder add(@NotNull Choice... choices);

        /**
         * 添加指定数量的材料
         *
         * @param choice 材料选择（非null）
         * @param count 材料数量（≥1）
         * @return 当前建造器实例
         * @throws IllegalArgumentException 如果数量无效
         */
        @NotNull Builder add(@NotNull Choice choice, int count);

        /**
         * 设置结果物品生成器
         *
         * @param resultGenerator 生成器实例（非null）
         * @return 当前建造器实例
         * @see ItemGenerator 生成器接口
         */
        @NotNull Builder resultGenerator(@NotNull ItemGenerator resultGenerator);

        /**
         * 构建不可变的无序配方实例
         *
         * @return 配置完成的配方实例
         * @throws IllegalStateException 如果缺少必要参数
         */
        @NotNull ArtisanShapelessRecipe build();
    }
}
