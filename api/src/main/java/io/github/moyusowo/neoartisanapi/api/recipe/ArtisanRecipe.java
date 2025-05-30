package io.github.moyusowo.neoartisanapi.api.recipe;

import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * 自定义配方系统的核心抽象接口，定义所有配方类型的公共契约。
 *
 * <p><b>设计哲学：</b></p>
 * <ul>
 *   <li><b>不变性</b> - 所有实现类应当是不可变的</li>
 *   <li><b>生成式设计</b> - 结果物品通过生成器动态创建</li>
 * </ul>
 *
 * @see ArtisanShapedRecipe 有序合成配方
 * @see ArtisanShapelessRecipe 无序合成配方
 * @see ArtisanFurnaceRecipe 熔炉配方
 * @since 1.0.2
 */
public interface ArtisanRecipe {

    /**
     * 获取配方的全局唯一标识符
     *
     * @return 非null的命名空间键
     * @implSpec 实现应当缓存此值而非每次重新生成
     */
    @NotNull NamespacedKey getKey();

    /**
     * 获取配方所需的输入材料数组
     *
     * <p><b>注意事项：</b></p>
     * <ul>
     *   <li>即使只有一个输入格的配方也可以返回正确的结果</li>
     *   <li>空槽位应当用{@link io.github.moyusowo.neoartisanapi.api.item.ArtisanItem#EMPTY}表示空气</li>
     * </ul>
     *
     * @return 非null的材料键数组（可能包含空元素但数组本身非null）
     * @see io.github.moyusowo.neoartisanapi.api.item.ItemRegistry 材料键的合法来源
     */
    @NotNull NamespacedKey[] getInputs();

    /**
     * 获取结果物品生成器
     *
     * @return 非null的物品生成器实例
     * @see ItemGenerator
     */
    @NotNull ItemGenerator getResultGenerator();

}
