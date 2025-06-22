package io.github.moyusowo.neoartisan.item.property;

import org.jetbrains.annotations.NotNull;

public record WeaponProperty(@NotNull Float speed, @NotNull Float knockback, @NotNull Float damage) {
    public static final WeaponProperty EMPTY = new WeaponProperty(null, null, null);
}
