package io.github.moyusowo.neoartisanapi.api.recipe;

import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public interface ArtisanRecipe {

    @NotNull NamespacedKey getKey();

    @NotNull NamespacedKey[] getInputs();

    @NotNull ItemGenerator getResultGenerator();

}
