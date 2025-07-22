package io.github.moyusowo.neoartisanapi.api.recipe;

import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * 自定义合成配方注册表API，提供标准化的配方创建接口。
 *
 * <p>支持三种配方类型：</p>
 * <ul>
 *   <li><b>有序合成(Shaped)</b> - 精确匹配物品排列位置</li>
 *   <li><b>无序合成(Shapeless)</b> - 仅需材料无需考虑排列</li>
 *   <li><b>熔炉烧炼(Furnace)</b> - 在熔炉中烧炼的配方</li>
 * </ul>
 *
 * <p>通过 {@link NeoArtisanAPI#getRecipeRegistry()} ()} 获取实例。</p>
 *
 * @see ArtisanRecipe
 * @see ArtisanShapedRecipe
 * @see ArtisanShapelessRecipe
 * @see ArtisanFurnaceRecipe
 */
public interface RecipeRegistry {

    /**
     * 注册自定义合成配方到中央注册表
     *
     * <p><b>注册约束：</b></p>
     * <ul>
     *   <li>配方ID必须全局唯一</li>
     *   <li>重复注册相同ID将抛出异常</li>
     *   <li>必须在插件启用阶段完成注册</li>
     * </ul>
     *
     * @param recipe 要注册的配方实例（非null）
     * @throws IllegalArgumentException 使用了相同的配方ID
     */
    void register(@NotNull ArtisanRecipe recipe);

    /**
     * 检查指定ID的配方是否已注册
     *
     * @param key 要检查的配方ID
     * @return 如果存在对应配方返回true
     * @apiNote 该方法线程安全，可在任意阶段调用
     */
    boolean hasRecipe(@Nullable NamespacedKey key);

    /**
     * 获取已注册的配方实例
     *
     * @param key 配方ID（非null）
     * @return 对应的配方实例
     * @throws IllegalArgumentException 如果key未在任何命名空间注册
     * @implNote 返回的配方实例是不可变对象
     */
    @NotNull ArtisanRecipe getRecipe(@NotNull NamespacedKey key);

    /**
     * 根据配方的输入数组，获取已注册的配方实例
     *
     * <p>数组内的每个位置的物品ID，包括数组长度，都需要和已注册配方内的 {@link ArtisanRecipe#getInputs()} 一致</p>
     *
     * @param items 配方物品输入矩阵（非null且无null）
     * @param recipeType 配方类型（非null）
     * @return 对应的配方实例（不可变）
     * @see RecipeType 已有的配方类型，配方类型可用自己的命名空间。
     */
    @NotNull
    Optional<ArtisanRecipe> getRecipe(@NotNull NamespacedKey[] items, @NotNull NamespacedKey recipeType);

}
