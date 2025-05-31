package io.github.moyusowo.neoartisanapi.api.recipe;

/**
 * 定义配方类型的枚举，标识不同合成方式。
 *
 * <p><b>可用类型：</b></p>
 * <ul>
 *   <li>{@link #SHAPED} - 有序合成（需按特定排列）</li>
 *   <li>{@link #SHAPELESS} - 无序合成（仅需材料）</li>
 *   <li>{@link #FURNACE} - 熔炉烧炼</li>
 * </ul>
 *
 * @see ArtisanShapedRecipe
 * @see ArtisanShapelessRecipe
 * @see ArtisanFurnaceRecipe
 * @since 1.0.2
 */
public enum RecipeType {
    SHAPED,
    SHAPELESS,
    FURNACE
}
