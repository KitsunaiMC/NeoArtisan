package io.github.moyusowo.neoartisan.recipe.internal;

import io.github.moyusowo.neoartisan.util.data.ArrayKey;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanRecipe;
import org.jetbrains.annotations.NotNull;

public interface RecipeRegistryInternal {
    boolean has(ArrayKey arrayKey);

    @NotNull ArtisanRecipe get(ArrayKey arrayKey);
}
