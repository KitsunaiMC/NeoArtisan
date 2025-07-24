package io.github.moyusowo.neoartisan.item.property;

import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class FoodProperty {
    public static final FoodProperty EMPTY = new FoodProperty(null, null, false, Map.of(), null);

    private final Integer nutrition;
    private final Float saturation;
    private final boolean canAlwaysEat;
    private final Map<PotionEffect, Float> effectChances;
    private final Float consumeSeconds;

    private FoodProperty(Integer nutrition, Float saturation, boolean canAlwaysEat, @NotNull Map<PotionEffect, Float> effectChances, Float consumeSeconds) {
        this.nutrition = nutrition;
        this.saturation = saturation;
        this.canAlwaysEat = canAlwaysEat;
        this.effectChances = new HashMap<>(effectChances);
        this.consumeSeconds = consumeSeconds;
    }

    public static FoodProperty create(int nutrition, float saturation, boolean canAlwaysEat) {
        return new FoodProperty(nutrition, saturation, canAlwaysEat, Map.of(), null);
    }

    public static FoodProperty create(int nutrition, float saturation, boolean canAlwaysEat, @NotNull Map<PotionEffect, Float> effectChance, float consumeSeconds) {
        return new FoodProperty(nutrition, saturation, canAlwaysEat, effectChance, consumeSeconds);
    }

    @Unmodifiable
    @NotNull
    public Optional<Map<PotionEffect, Float>> getEffectChances() {
        if (!effectChances.isEmpty()) return Optional.of(Collections.unmodifiableMap(effectChances));
        return Optional.empty();
    }

    @NotNull
    public Optional<Float> getConsumeSeconds() {
        if (consumeSeconds != null) return Optional.of(consumeSeconds);
        return Optional.empty();
    }

    public int nutrition() {
        if (nutrition == null) throw new IllegalStateException("You can not call EMPTY property!");
        return nutrition;
    }

    public float saturation() {
        if (saturation == null) throw new IllegalStateException("You can not call EMPTY property!");
        return saturation;
    }

    public boolean canAlwaysEat() {
        return canAlwaysEat;
    }
}
