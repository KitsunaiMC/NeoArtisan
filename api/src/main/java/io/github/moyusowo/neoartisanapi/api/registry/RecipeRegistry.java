package io.github.moyusowo.neoartisanapi.api.registry;

import com.google.common.collect.Multimap;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.RecipeType;
import io.github.moyusowo.neoartisanapi.api.recipe.guide.GuideGUIGenerator;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.function.Supplier;

/**
 * Custom crafting recipe registry API, providing standardized recipe creation interface.
 *
 * <p>Get the instance through {@link Registries#RECIPE}.</p>
 *
 * @see ArtisanRecipe
 */
@ApiStatus.NonExtendable
public interface RecipeRegistry {

    /**
     * Registers a custom crafting recipe to the central registry (any recipe implementing {@link ArtisanRecipe} can be registered).
     *
     * <p><b>Registration constraints:</b></p>
     * <ul>
     *   <li>Recipe ID must be globally unique</li>
     *   <li>Registering the same ID twice will throw an exception</li>
     *   <li>Registration must be completed during plugin enable phase</li>
     * </ul>
     *
     * @param recipe The recipe instance to register (must not be null)
     * @throws IllegalArgumentException If the same recipe ID is used
     */
    void register(@NotNull ArtisanRecipe recipe);

    /**
     * Checks if a recipe with the specified ID is registered.
     *
     * @param key The recipe ID to check
     * @return true if a corresponding recipe exists, false otherwise
     * @apiNote This method is thread-safe and can be called at any phase
     */
    boolean hasRecipe(@Nullable NamespacedKey key);

    /**
     * Gets a registered recipe instance.
     *
     * @param key The recipe ID (must not be null)
     * @return The corresponding recipe instance
     * @throws IllegalArgumentException If the key is not registered in any namespace
     * @implNote The returned recipe instance is an immutable object
     */
    @NotNull ArtisanRecipe getRecipe(@NotNull NamespacedKey key);

    /**
     * Gets all registered recipes of the same type.
     *
     * @param recipeType The recipe type (must not be null)
     * @return The corresponding recipe instances (immutable)
     * @see RecipeType Available recipe types, recipe types can use custom namespaces
     */
    @Unmodifiable
    @NotNull
    Collection<ArtisanRecipe> getRecipesByType(@NotNull NamespacedKey recipeType);

    /**
     * Gets all registered recipes.
     *
     * @return All recipe instances (immutable)
     */
    @Unmodifiable
    @NotNull
    Collection<ArtisanRecipe> getAllRecipes();

    /**
     * Gets all registered recipes grouped by type.
     *
     * @return A multimap of recipe types to recipe instances (immutable)
     */
    @Unmodifiable
    @NotNull
    Multimap<NamespacedKey, ArtisanRecipe> getAllRecipesByType();

    /**
     * Sets (replaces) the guide GUI generator for a recipe type.
     *
     * @param recipeType The recipe type (must not be null)
     * @param generator The guide GUI generator (must not be null)
     */
    void setGuide(@NotNull NamespacedKey recipeType, @NotNull GuideGUIGenerator generator);

    /**
     * register a category item for an item category in the recipe book.
     *
     * <p>the ItemStack will be used as the icon of the category in the recipe book.</p>
     *
     * @param category The category key (must not be null)
     * @param itemStackSupplier The item stack supplier (must not be null)
     */
    void setCategory(@NotNull NamespacedKey category, @NotNull Supplier<ItemStack> itemStackSupplier);

}
