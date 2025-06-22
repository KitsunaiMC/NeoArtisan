package io.github.moyusowo.neoartisan.item.property;

import org.jetbrains.annotations.NotNull;

public record FoodProperty(@NotNull Integer nutrition, @NotNull Float saturation, boolean canAlwaysEat) {
    public static final FoodProperty EMPTY = new FoodProperty(null, null, false);
}
