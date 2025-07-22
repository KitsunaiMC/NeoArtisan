package io.github.moyusowo.neoartisanapi.api.recipe;

import org.bukkit.NamespacedKey;

/**
 * 定义配方类型的枚举，标识不同合成方式。
 *
 * <p><b>可用类型：</b></p>
 * <ul>
 *   <li>{@link #SHAPED} - 有序合成（需按特定排列）</li>
 *   <li>{@link #SHAPELESS} - 无序合成（仅需材料）</li>
 *   <li>{@link #FURNACE} - 熔炉烧炼</li>
 *   <li>{@link #CAMPFIRE} - 营火烧炼</li>
 *   <li>{@link #SMOKING} - 烟熏炉烧炼</li>
 *   <li>{@link #BLASTING} - 高炉烧炼</li>
 * </ul>
 *
 * @see ArtisanShapedRecipe
 * @see ArtisanShapelessRecipe
 * @see ArtisanFurnaceRecipe
 * @see ArtisanCampfireRecipe
 * @see ArtisanSmokingRecipe
 * @see ArtisanBlastingRecipe
 */
public final class RecipeType {
    public static final NamespacedKey SHAPED = NamespacedKey.minecraft("shaped"),
        SHAPELESS = NamespacedKey.minecraft("shapeless"),
        FURNACE = NamespacedKey.minecraft("furnace"),
        CAMPFIRE = NamespacedKey.minecraft("campfire"),
        SMOKING = NamespacedKey.minecraft("smoking"),
        BLASTING = NamespacedKey.minecraft("blasting");
}
