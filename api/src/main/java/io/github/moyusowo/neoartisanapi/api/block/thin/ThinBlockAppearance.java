package io.github.moyusowo.neoartisanapi.api.block.thin;

public final class ThinBlockAppearance {

    public final PressurePlateAppearance appearance;
    public final int power;

    public ThinBlockAppearance(PressurePlateAppearance appearance, int power) {
        if (power <= 1 || power > 15) throw new IllegalArgumentException();
        this.appearance = appearance;
        this.power = power;
    }

    public enum PressurePlateAppearance {
        LIGHT_WEIGHTED_PRESSURE_PLATE,
        HEAVY_WEIGHTED_PRESSURE_PLATE
    }
}
