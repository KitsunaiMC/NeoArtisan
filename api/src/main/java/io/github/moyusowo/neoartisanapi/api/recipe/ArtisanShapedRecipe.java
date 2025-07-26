package io.github.moyusowo.neoartisanapi.api.recipe;

import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.Choice;
import io.github.moyusowo.neoartisanapi.api.util.BuilderFactoryUtil;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * 有序合成配方接口，继承自基础 {@link ArtisanRecipe}。
 *
 * <p><b>配方模式说明：</b></p>
 * <pre>
 * 示例配方布局：
 * .set(" A ", " B ", "   ")
 * .add('A', Material.IRON_INGOT)
 * .add('B', Material.STICK)
 * 对应工作台布局：
 * [ 空 ][铁锭][ 空 ]
 * [ 空 ][木棍][ 空 ]
 * [ 空 ][ 空 ][ 空 ]
 * </pre>
 *
 * @see ArtisanRecipe 基础配方接口
 */
@ApiStatus.NonExtendable
public interface ArtisanShapedRecipe extends ArtisanRecipe {
    @NotNull
    static Builder builder() {
        return BuilderFactoryUtil.getBuilder(BuilderFactory.class).builder();
    }

    @NotNull
    default NamespacedKey getKey() {
        return RecipeType.SHAPED;
    }

    @NotNull
    default ItemGenerator getResultGenerator() {
        return getResultGenerators().getFirst();
    }

    @NotNull
    Choice[][] getInputMatrix();

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
     * 有序配方建造器接口，采用流式API设计。
     *
     * <p><b>使用规范：</b></p>
     * <ol>
     *    <li>必须调用 {@link #key(NamespacedKey)} 设置唯一配方命名空间键</li>
     *   <li>必须调用 {@link #set} 设置布局</li>
     *   <li>必须调用 {@link #add(char, Choice)} 绑定材料</li>
     *   <li>必须调用 {@link #resultGenerator(ItemGenerator)} 设置结果生成器</li>
     *   <li>必须调用 {@link #build()} 完成构建</li>
     * </ol>
     */
    interface Builder {

        /**
         * 设置配方唯一标识符
         *
         * @param key 符合命名空间规范的键（非null）
         * @return 当前建造器实例（链式调用）
         * @see ArtisanRecipe#getKey() 命名规范
         */
        @NotNull Builder key(@NotNull NamespacedKey key);

        /**
         * 设置三行配方布局
         *
         * @param line1 第一行模式（非null，长度必须为3）
         * @param line2 第二行模式（非null，长度必须为3）
         * @param line3 第三行模式（非null，长度必须为3）
         * @return 当前建造器实例
         * @throws IllegalArgumentException 如果行长度无效或包含未定义字符
         */
        @NotNull Builder set(@NotNull String line1, @NotNull String line2, @NotNull String line3);

        /**
         * 设置两行配方布局（第三行为空）
         *
         * @param line1 第一行模式（非null）
         * @param line2 第二行模式（非null）
         * @return 当前建造器实例
         */
        @NotNull Builder set(@NotNull String line1, @NotNull String line2);

        /**
         * 设置单行配方布局（第二、三行为空）
         *
         * @param line1 第一行模式（非null）
         * @return 当前建造器实例
         */
        @NotNull Builder set(@NotNull String line1);

        /**
         * 绑定材料字符映射
         *
         * @param c 布局中的占位字符
         * @param choice 对应的材料选择
         * @return 当前建造器实例
         */
        @NotNull Builder add(char c, @NotNull Choice choice);

        /**
         * 设置结果物品生成器
         *
         * @param resultGenerator 生成器实例（非null）
         * @return 当前建造器实例
         * @see ItemGenerator 生成器接口
         */
        @NotNull Builder resultGenerator(@NotNull ItemGenerator resultGenerator);

        /**
         * 构建不可变的有序配方实例
         *
         * @return 配置完成的配方实例
         * @throws IllegalCallerException 如果缺少必要参数
         * @implSpec 实现必须保证返回对象的不可变性
         */
        @NotNull ArtisanShapedRecipe build();
    }

}
