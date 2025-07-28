package io.github.moyusowo.neoartisanapi.api.recipe;

import org.bukkit.NamespacedKey;

/**
 * Defines recipe types that identify different crafting methods.
 *
 * <p><b>Available types:</b></p>
 * <ul>
 *   <li>{@link #SHAPED} - Shaped crafting (requires specific arrangement)</li>
 *   <li>{@link #SHAPELESS} - Shapeless crafting (ingredients only)</li>
 *   <li>{@link #FURNACE} - Furnace smelting</li>
 *   <li>{@link #CAMPFIRE} - Campfire cooking</li>
 *   <li>{@link #SMOKING} - Smoker cooking</li>
 *   <li>{@link #BLASTING} - Blast furnace smelting</li>
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
    public static final NamespacedKey SHAPED = NamespacedKey.minecraft("crafting_shaped"),
        SHAPELESS = NamespacedKey.minecraft("crafting_shapeless"),
        FURNACE = NamespacedKey.minecraft("smelting"),
        CAMPFIRE = NamespacedKey.minecraft("campfire_cooking"),
        SMOKING = NamespacedKey.minecraft("smoking"),
        BLASTING = NamespacedKey.minecraft("blasting");
}
