package io.github.moyusowo.neoartisan.util;

import org.bukkit.NamespacedKey;

import java.util.Arrays;

public class ArrayKey {

    private final NamespacedKey[] array;

    private ArrayKey(NamespacedKey[] array) {
        this.array = array.clone();
    }

    public static ArrayKey from(NamespacedKey[] array) {
        return new ArrayKey(array);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        return Arrays.equals(array, ((ArrayKey) o).array);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(array);
    }

    @Override
    public String toString() {
        return Arrays.toString(array);
    }

}
