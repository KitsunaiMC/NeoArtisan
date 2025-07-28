package io.github.moyusowo.neoartisanapi.api.recipe;

import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.Choice;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for furnace-like recipes such as furnace, smoker, blast furnace, and campfire recipes.
 * <p>
 * Defines common properties for all furnace-type recipes.
 * </p>
 */
public interface ArtisanFurnaceLikeRecipe extends ArtisanRecipe {
    /**
     * Gets the smelting ingredient item ID
     *
     * @return non-null item namespace key
     * @implNote Should be consistent with the first element of {@link #getInputs()}
     */
    @NotNull
    Choice getInput();

    /**
     * Gets the standard smelting time required (in ticks)
     *
     * @return positive integer value (1 tick = 0.05 seconds)
     */
    int getCookTime();

    /**
     * Gets the experience value gained when smelting is completed
     *
     * @return positive float value
     */
    float getExp();

    /**
     * Gets the result item generator
     *
     * @return the result item generator
     */
    @NotNull
    default ItemGenerator getResultGenerator() {
        return getResultGenerators().getFirst();
    }
}
