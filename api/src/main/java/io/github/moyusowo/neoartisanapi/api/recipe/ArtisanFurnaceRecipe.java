package io.github.moyusowo.neoartisanapi.api.recipe;

import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public interface ArtisanFurnaceRecipe extends ArtisanRecipe {

    static Builder builder() {
        return Bukkit.getServicesManager().load(ArtisanFurnaceRecipe.Builder.class);
    }

    @NotNull NamespacedKey getInput();

    int getCookTime();

    float getExp();

    interface Builder {

        @NotNull Builder key(NamespacedKey key);

        @NotNull Builder inputItemId(NamespacedKey inputItemId);

        @NotNull Builder resultGenerator(ItemGenerator resultGenerator);

        @NotNull Builder cookTime(int cookTime);

        @NotNull Builder exp(float exp);

        @NotNull ArtisanFurnaceRecipe build();
    }
}
