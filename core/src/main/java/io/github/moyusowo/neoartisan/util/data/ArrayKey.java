package io.github.moyusowo.neoartisan.util.data;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public class ArrayKey {

    private final NamespacedKey[] array;
    private final NamespacedKey recipeType;

    private ArrayKey(NamespacedKey[] array, NamespacedKey recipeType) {
        this.array = array.clone();
        this.recipeType = recipeType;
    }

    @NotNull
    public static ArrayKey from(@NotNull NamespacedKey[] array, @NotNull NamespacedKey recipeType) {
        return new ArrayKey(array, recipeType);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return keysEquals(((ArrayKey) o).array, array) && recipeType.equals(((ArrayKey) o).recipeType);
    }

    private static boolean keysEquals(NamespacedKey[] a1, NamespacedKey[] a2) {
        if (a1.length != a2.length) return false;
        for (int i = 0; i < a1.length; i++) {
            if (!a1[i].equals(a2[i])) {
                return false;
            }
        }
        return true;
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
