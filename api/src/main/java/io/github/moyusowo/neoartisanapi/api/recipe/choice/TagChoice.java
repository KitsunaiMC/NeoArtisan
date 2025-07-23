package io.github.moyusowo.neoartisanapi.api.recipe.choice;

import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record TagChoice(@NotNull String tag) implements Choice {
    @Override
    public boolean matches(@Nullable ItemStack itemStack) {
        NamespacedKey itemId = NeoArtisanAPI.getItemRegistry().getRegistryId(itemStack);
        return NeoArtisanAPI.getItemRegistry().getIdByTag(tag).contains(itemId);
    }
}
