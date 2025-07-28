package io.github.moyusowo.neoartisanapi.api.recipe;

import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.Choice;
import io.github.moyusowo.neoartisanapi.api.util.ServiceUtil;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Smoker smelting recipe interface, extending the base {@link ArtisanRecipe}.
 *
 * <p><b>Recipe features:</b></p>
 * <ul>
 *   <li>Supports custom smelting time (ticks)</li>
 *   <li>Configurable experience output</li>
 *   <li>Overrides vanilla MaterialChoice logic - vanilla MaterialChoice no longer applies!</li>
 * </ul>
 *
 * @see ArtisanRecipe base recipe interface
 */
@ApiStatus.NonExtendable
public interface ArtisanSmokingRecipe extends ArtisanFurnaceLikeRecipe {
    /**
     * Creates a new builder for constructing {@link ArtisanFurnaceRecipe} instances.
     *
     * @return A new builder instance
     */
    @NotNull
    static Builder builder() {
        return ServiceUtil.getService(BuilderFactory.class).builder();
    }

    interface BuilderFactory {
        @NotNull
        Builder builder();
    }

    @NotNull
    default NamespacedKey getKey() {
        return RecipeType.SMOKING;
    }

    /**
     * Builder interface for creating campfire recipes.
     *
     * <p><b>All fields must be set!</b></p>
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
         * Sets the input ingredient for smelting.
         *
         * @param choice The input item choice (must not be null)
         * @return This builder instance
         * @throws IllegalArgumentException If the item is not registered
         */
        @NotNull Builder input(@NotNull Choice choice);

        /**
         * Sets the result item generator.
         *
         * @param resultGenerator The result generator (must not be null)
         * @return This builder instance
         * @see ItemGenerator Generator interface
         */
        @NotNull Builder resultGenerator(@NotNull ItemGenerator resultGenerator);

        /**
         * Sets the cooking time.
         *
         * @param cookTime The number of ticks to cook (must be ≥1)
         * @return This builder instance
         */
        @NotNull Builder cookTime(int cookTime);

        /**
         * Sets the experience reward.
         *
         * @param exp The experience gained (must be ≥0)
         * @return This builder instance
         */
        @NotNull Builder exp(float exp);

        /**
         * Builds an immutable furnace recipe instance.
         *
         * @return The configured recipe instance
         * @throws IllegalArgumentException If required parameters are missing
         */
        @NotNull ArtisanSmokingRecipe build();
    }
}
