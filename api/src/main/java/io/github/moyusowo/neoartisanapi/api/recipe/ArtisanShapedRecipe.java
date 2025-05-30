package io.github.moyusowo.neoartisanapi.api.recipe;

import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public interface ArtisanShapedRecipe extends ArtisanRecipe {

    static Builder builder() {
        return Bukkit.getServicesManager().load(Builder.class);
    }


    interface Builder {

        @NotNull Builder key(NamespacedKey key);

        @NotNull Builder set(@NotNull String line1, @NotNull String line2, @NotNull String line3);

        @NotNull Builder set(@NotNull String line1, @NotNull String line2);

        @NotNull Builder set(@NotNull String line1);

        @NotNull Builder add(char c, @NotNull NamespacedKey itemId);

        @NotNull Builder resultGenerator(ItemGenerator resultGenerator);

        @NotNull ArtisanShapedRecipe build();
    }

}
