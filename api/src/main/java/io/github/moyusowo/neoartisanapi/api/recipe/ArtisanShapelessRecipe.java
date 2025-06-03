package io.github.moyusowo.neoartisanapi.api.recipe;

import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
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
 * @since 1.0.2
 */
@SuppressWarnings("unused")
public interface ArtisanShapelessRecipe extends ArtisanRecipe {

    /**
     * 获取无序配方建造器实例
     *
     * @return 非null的建造器实例
     * @throws IllegalStateException 如果建造器服务未注册
     */
    static Builder builder() {
        return Bukkit.getServicesManager().load(Builder.class);
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
        @NotNull Builder key(NamespacedKey key);

        /**
         * 添加单个材料（数量默认为1）
         *
         * @param itemId 材料ID（非null）
         * @return 当前建造器实例
         * @see #add(NamespacedKey, int) 指定数量的版本
         */
        @NotNull Builder add(@NotNull NamespacedKey itemId);

        /**
         * 批量添加多个材料（每个数量默认为1）
         *
         * @param itemIds 材料ID数组（非null，可空数组）
         * @return 当前建造器实例
         * @throws IllegalArgumentException 如果数组为null
         */
        @NotNull Builder add(@NotNull NamespacedKey... itemIds);

        /**
         * 添加指定数量的材料
         *
         * @param itemId 材料ID（非null）
         * @param count 材料数量（≥1）
         * @return 当前建造器实例
         * @throws IllegalArgumentException 如果数量无效
         */
        @NotNull Builder add(@NotNull NamespacedKey itemId, int count);

        /**
         * 设置结果物品生成器
         *
         * @param resultGenerator 生成器实例（非null）
         * @return 当前建造器实例
         * @see ItemGenerator 生成器接口
         */
        @NotNull Builder resultGenerator(ItemGenerator resultGenerator);

        /**
         * 构建不可变的无序配方实例
         *
         * @return 配置完成的配方实例
         * @throws IllegalStateException 如果缺少必要参数
         */
        @NotNull ArtisanShapelessRecipe build();
    }
}
