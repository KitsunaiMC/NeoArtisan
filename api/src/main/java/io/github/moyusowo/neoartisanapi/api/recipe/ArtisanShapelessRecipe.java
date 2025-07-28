package io.github.moyusowo.neoartisanapi.api.recipe;

import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.Choice;
import io.github.moyusowo.neoartisanapi.api.util.ServiceUtil;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Shapeless crafting recipe interface, extending the base {@link ArtisanRecipe}.
 *
 * <p><b>Recipe features:</b></p>
 * <ul>
 *   <li>Does not require specific arrangement of ingredients</li>
 *   <li>Automatically handles ingredient stack amounts</li>
 *   <li>Supports batch addition of ingredients</li>
 * </ul>
 *
 * @see ArtisanRecipe base recipe interface
 */
@ApiStatus.NonExtendable
public interface ArtisanShapelessRecipe extends ArtisanRecipe {
    /**
     * Creates a new builder for constructing {@link ArtisanShapelessRecipe} instances.
     *
     * @return A new builder instance
     */
    @NotNull
    static Builder builder() {
        return ServiceUtil.getService(BuilderFactory.class).builder();
    }

    @NotNull
    default NamespacedKey getKey() {
        return RecipeType.SHAPELESS;
    }

    /**
     * Gets the result item generator for this recipe.
     *
     * @return The result item generator
     */
    @NotNull
    default ItemGenerator getResultGenerator() {
        return getResultGenerators().getFirst();
    }

    interface BuilderFactory {
        @NotNull
        Builder builder();
    }

    /**
     * Shapeless recipe builder interface
     *
     * <p><b>Build process:</b></p>
     * <ol>
     *   <li>Must set {@link #key(NamespacedKey)}</li>
     *   <li>Must add at least 1 ingredient</li>
     *   <li>Must set {@link #resultGenerator(ItemGenerator)}</li>
     * </ol>
     */
    interface Builder {
        /**
         * Sets the recipe identifier.
         *
         * @param key The namespace key for this recipe (must not be null)
         * @return This builder instance
         */
        @NotNull Builder key(@NotNull NamespacedKey key);

        /**
         * Adds a single ingredient.
         *
         * @param choice The ingredient choice (must not be null)
         * @return This builder instance
         * @see #add(Choice, int) Version with specified amount
         */
        @NotNull Builder add(@NotNull Choice choice);

        /**
         * Adds multiple ingredients in batch.
         *
         * @param choices Array of ingredient choices (must not be null)
         * @return This builder instance
         */
        @NotNull Builder add(@NotNull Choice... choices);

        /**
         * Adds an ingredient with a specified amount.
         *
         * @param choice The ingredient choice (must not be null)
         * @param count The ingredient amount (must be ≥1)
         * @return This builder instance
         * @throws IllegalArgumentException If the amount is invalid
         */
        @NotNull Builder add(@NotNull Choice choice, int count);

        /**
         * Sets the result item generator.
         *
         * @param resultGenerator The result generator (must not be null)
         * @return This builder instance
         * @see ItemGenerator Generator interface
         */
        @NotNull Builder resultGenerator(@NotNull ItemGenerator resultGenerator);

        /**
         * Builds an immutable shapeless recipe instance.
         *
         * @return The configured recipe instance
         * @throws IllegalStateException If required parameters are missing
         */
        @NotNull ArtisanShapelessRecipe build();
    }
}
