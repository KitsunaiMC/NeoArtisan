package io.github.moyusowo.neoartisan.recipe.internal;

import io.github.moyusowo.neoartisan.util.ArrayKey;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanRecipe;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public interface RecipeRegistryInternal {
    boolean has(ArrayKey arrayKey);

    @NotNull ArtisanRecipe get(ArrayKey arrayKey);
}
