package io.github.moyusowo.neoartisanapi.api.recipe;

import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.Choice;
import io.github.moyusowo.neoartisanapi.api.util.ServiceUtil;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;


/**
 * Shaped crafting recipe interface, extending the base {@link ArtisanRecipe}.
 *
 * @see ArtisanRecipe base recipe interface
 */
@ApiStatus.NonExtendable
public interface ArtisanShapedRecipe extends ArtisanRecipe {
    /**
     * Creates a new builder for constructing {@link ArtisanShapedRecipe} instances.
     *
     * @return A new builder instance
     */
    @NotNull
    static Builder builder() {
        return ServiceUtil.getService(BuilderFactory.class).builder();
    }

    @NotNull
    default NamespacedKey getKey() {
        return RecipeType.SHAPED;
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

    /**
     * Gets the input ingredient matrix for this recipe.
     *
     * @return A 2D array representing the recipe layout
     */
    @NotNull
    Choice[][] getInputMatrix();

    interface BuilderFactory {
        @NotNull
        Builder builder();
    }

    /**
     * Shaped recipe builder interface with fluent API design.
     *
     * <p><b>Usage guidelines:</b></p>
     * <ol>
     *   <li>Must call {@link #key(NamespacedKey)} to set the unique recipe namespace key</li>
     *   <li>Must call {@link #set} to define the pattern</li>
     *   <li>Must call {@link #add(char, Choice)} to bind ingredients</li>
     *   <li>Must call {@link #resultGenerator(ItemGenerator)} to set the result generator</li>
     *   <li>Must call {@link #build()} to complete construction</li>
     * </ol>
     */
    interface Builder {
        /**
         * Sets the recipe identifier.
         *
         * @param key The namespace key for this recipe (must not be null)
         * @return This builder instance (for chaining)
         * @see ArtisanRecipe#getKey() Naming conventions
         */
        @NotNull Builder key(@NotNull NamespacedKey key);

        /**
         * Sets the three-line recipe pattern.
         *
         * @param line1 First line pattern (must not be null, length must be 3)
         * @param line2 Second line pattern (must not be null, length must be 3)
         * @param line3 Third line pattern (must not be null, length must be 3)
         * @return This builder instance
         * @throws IllegalArgumentException If line lengths are invalid or contain undefined characters
         */
        @NotNull Builder set(@NotNull String line1, @NotNull String line2, @NotNull String line3);

        /**
         * Sets the two-line recipe pattern (third line is empty).
         *
         * @param line1 First line pattern (must not be null)
         * @param line2 Second line pattern (must not be null)
         * @return This builder instance
         */
        @NotNull Builder set(@NotNull String line1, @NotNull String line2);

        /**
         * Sets the single-line recipe pattern (second and third lines are empty).
         *
         * @param line1 First line pattern (must not be null)
         * @return This builder instance
         */
        @NotNull Builder set(@NotNull String line1);

        /**
         * Binds a character to an ingredient choice.
         *
         * @param c The placeholder character in the pattern
         * @param choice The corresponding ingredient choice
         * @return This builder instance
         */
        @NotNull Builder add(char c, @NotNull Choice choice);

        /**
         * Sets the result item generator.
         *
         * @param resultGenerator The result generator (must not be null)
         * @return This builder instance
         * @see ItemGenerator Generator interface
         */
        @NotNull Builder resultGenerator(@NotNull ItemGenerator resultGenerator);

        /**
         * Builds an immutable shaped recipe instance.
         *
         * @return The configured recipe instance
         * @throws IllegalCallerException If required parameters are missing
         */
        @NotNull ArtisanShapedRecipe build();
    }

}
