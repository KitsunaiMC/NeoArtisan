package io.github.moyusowo.neoartisanapi.api.recipe;

import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.Choice;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * Core abstract interface of the custom recipe system, defining the common contract for all recipe types.
 *
 * <p><b>Design philosophy:</b></p>
 * <ul>
 *   <li><b>Immutability</b> - All implementation classes should be immutable</li>
 *   <li><b>Generative design</b> - Result items are dynamically created through generators</li>
 * </ul>
 *
 * @see ArtisanShapedRecipe shaped crafting recipe
 * @see ArtisanShapelessRecipe shapeless crafting recipe
 * @see ArtisanFurnaceRecipe furnace recipe
 * @see ArtisanSmokingRecipe smoker recipe
 * @see ArtisanCampfireRecipe campfire recipe
 * @see ArtisanBlastingRecipe blast furnace recipe
 * @implNote You can implement your own recipe types
 */
public interface ArtisanRecipe {
    /**
     * Gets the globally unique identifier of the recipe
     */
    @NotNull
    NamespacedKey getKey();

    /**
     * Gets the recipe type
     *
     * @see RecipeType
     */
    @NotNull
    NamespacedKey getType();

    /**
     * Gets the immutable list of input materials required for the recipe
     *
     * @see Choice
     */
    @Unmodifiable
    @NotNull
    List<Choice> getInputs();

    /**
     * Gets the immutable list of result item generators
     *
     * @see ItemGenerator
     */
    @Unmodifiable
    @NotNull
    List<ItemGenerator> getResultGenerators();

    /**
     * Checks if the given item matrix matches this recipe
     *
     * @param matrix the item matrix to check (cannot be null)
     * @return true if the matrix matches this recipe, false otherwise
     */
    boolean matches(ItemStack @NotNull [] matrix);
}
