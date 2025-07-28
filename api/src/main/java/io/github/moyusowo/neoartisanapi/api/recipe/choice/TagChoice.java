package io.github.moyusowo.neoartisanapi.api.recipe.choice;

import io.github.moyusowo.neoartisanapi.api.registry.Registries;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Recipe choice that matches items by tag.
 * <p>
 * This choice matches ItemStacks whose items belong to the specified tag.
 * </p>
 */
public record TagChoice(@NotNull String tag) implements Choice {
    @Override
    public boolean matches(@Nullable ItemStack itemStack) {
        NamespacedKey itemId = Registries.ITEM.getRegistryId(itemStack);
        return Registries.ITEM.getIdByTag(tag).contains(itemId);
    }

    @Override
    public boolean matches(@Nullable NamespacedKey itemId) {
        return Registries.ITEM.getIdByTag(tag).contains(itemId);
    }
}
