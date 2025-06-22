package io.github.moyusowo.neoartisan.item.property;

import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ArmorProperty(@NotNull Integer armor, @NotNull Integer armorToughness, @Nullable EquipmentSlot slot) {
    public static final ArmorProperty EMPTY = new ArmorProperty(null, null, null);
}
