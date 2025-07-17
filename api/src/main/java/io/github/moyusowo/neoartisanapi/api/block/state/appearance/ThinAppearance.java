package io.github.moyusowo.neoartisanapi.api.block.state.appearance;

import org.jetbrains.annotations.NotNull;

public record ThinAppearance(
        @NotNull ThinMaterial appearance,
        int power
) {

    public ThinAppearance {
        if (power <= 1 || power > 15) throw new IllegalArgumentException();
    }

    public enum ThinMaterial {
        LIGHT_WEIGHTED_PRESSURE_PLATE,
        HEAVY_WEIGHTED_PRESSURE_PLATE
    }
}
