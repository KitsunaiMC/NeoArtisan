package io.github.moyusowo.neoartisanapi.api.recipe;

import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import org.bukkit.NamespacedKey;

import java.util.Collection;

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
 * @see ArtisanShapedRecipe
 * @see ArtisanShapelessRecipe
 * @see ArtisanFurnaceRecipe
 * @since 1.0.2
 */
@SuppressWarnings("unused")
public interface RecipeRegistry {

    void register(ArtisanRecipe recipe);

    boolean hasRecipe(NamespacedKey key);

    ArtisanRecipe getRecipe(NamespacedKey key);

}
