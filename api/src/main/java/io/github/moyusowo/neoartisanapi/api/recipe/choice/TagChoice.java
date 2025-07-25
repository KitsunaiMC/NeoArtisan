package io.github.moyusowo.neoartisanapi.api.recipe.choice;

import io.github.moyusowo.neoartisanapi.api.registry.Registries;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record TagChoice(@NotNull String tag) implements Choice {
    @Override
    public boolean matches(@Nullable ItemStack itemStack) {
        NamespacedKey itemId = Registries.ITEM.getRegistryId(itemStack);
        return Registries.ITEM.getIdByTag(tag).contains(itemId);
    }
}
