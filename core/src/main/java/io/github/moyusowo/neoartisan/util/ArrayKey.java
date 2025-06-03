package io.github.moyusowo.neoartisan.util;

import io.github.moyusowo.neoartisanapi.api.recipe.RecipeType;
import org.bukkit.NamespacedKey;

import java.util.Arrays;
import java.util.Objects;

@SuppressWarnings("unused")
public class ArrayKey {

    private final NamespacedKey[] array;
    private final RecipeType recipeType;

    private ArrayKey(NamespacedKey[] array, RecipeType recipeType) {
        this.array = array.clone();
        this.recipeType = recipeType;
    }

    public static ArrayKey from(NamespacedKey[] array, RecipeType recipeType) {
        return new ArrayKey(array, recipeType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Arrays.equals(array, ((ArrayKey) o).array) && recipeType.equals(((ArrayKey) o).recipeType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.deepHashCode(array), recipeType);
    }

    @Override
    public String toString() {
        return Arrays.toString(array) + ", " + recipeType;
    }

}
