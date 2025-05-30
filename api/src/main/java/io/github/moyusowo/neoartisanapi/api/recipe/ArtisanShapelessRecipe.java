package io.github.moyusowo.neoartisanapi.api.recipe;

import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public interface ArtisanShapelessRecipe extends ArtisanRecipe {

    static Builder builder() {
        return Bukkit.getServicesManager().load(Builder.class);
    }

    interface Builder {

        @NotNull Builder key(NamespacedKey key);

        @NotNull Builder add(@NotNull NamespacedKey itemId);

        @NotNull Builder add(@NotNull NamespacedKey... itemIds);

        @NotNull Builder add(@NotNull NamespacedKey itemId, int count);

        @NotNull Builder resultGenerator(ItemGenerator resultGenerator);

        @NotNull ArtisanShapelessRecipe build();
    }
}
