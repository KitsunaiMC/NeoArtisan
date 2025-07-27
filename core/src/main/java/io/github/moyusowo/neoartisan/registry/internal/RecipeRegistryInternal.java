package io.github.moyusowo.neoartisan.registry.internal;

import io.github.moyusowo.neoartisanapi.api.recipe.guide.GuideGUIGenerator;
import io.github.moyusowo.neoartisanapi.api.registry.RecipeRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface RecipeRegistryInternal extends RecipeRegistry {
    @NotNull
    Optional<GuideGUIGenerator> getGuide(@NotNull NamespacedKey recipeType);

    @NotNull
    Optional<ItemStack> getCategory(@NotNull NamespacedKey category);
}
