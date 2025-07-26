package io.github.moyusowo.neoartisanapi.api.recipe.choice;

import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public final class EmptyChoice implements Choice {
    EmptyChoice() {}

    @Override
    public boolean matches(@Nullable ItemStack itemStack) {
        return itemStack == null || itemStack.isEmpty();
    }

    @Override
    public boolean matches(@Nullable NamespacedKey itemId) {
        return itemId == null || itemId == ArtisanItem.EMPTY;
    }

    @Override
    public String toString() {
        return "EmptyChoice";
    }
}
