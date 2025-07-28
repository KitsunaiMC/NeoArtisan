package io.github.moyusowo.neoartisanapi.api.recipe.choice;

import io.github.moyusowo.neoartisanapi.api.registry.Registries;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Recipe choice that matches a specific item by its registry ID.
 * <p>
 * This choice matches ItemStacks that correspond to the specified item ID.
 * </p>
 */
public record ItemChoice(@NotNull NamespacedKey itemId) implements Choice {
    @Override
    public boolean matches(@Nullable ItemStack itemStack) {
        return Registries.ITEM.getRegistryId(itemStack).equals(itemId);
    }

    @Override
    public boolean matches(@Nullable NamespacedKey itemId) {
        return this.itemId.equals(itemId);
    }
}
