package io.github.moyusowo.neoartisanapi.api.recipe.choice;

import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ItemChoice(@NotNull NamespacedKey itemId) implements Choice {
    @Override
    public boolean matches(@Nullable ItemStack itemStack) {
        return NeoArtisanAPI.getItemRegistry().getRegistryId(itemStack).equals(itemId);
    }
}
