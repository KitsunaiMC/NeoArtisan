package io.github.moyusowo.neoartisan.recipe.guide.item;

import com.google.common.collect.ArrayListMultimap;
import io.github.moyusowo.neoartisan.registry.internal.RecipeRegistryInternal;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.Choice;
import io.github.moyusowo.neoartisanapi.api.recipe.guide.GuideGUIGenerator;
import io.github.moyusowo.neoartisanapi.api.registry.Registries;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class GuideGUIManager {
    public final NamespacedKey itemId;
    private final ArrayListMultimap<GuideGUIType, GuideGUIHolder> inventorys;

    @NotNull
    private static Optional<GuideGUIType> hasItemInRecipe(@NotNull ArtisanRecipe recipe, @NotNull NamespacedKey itemId) {
        for (Choice choice : recipe.getInputs()) {
            if (choice.matches(itemId)) {
                return Optional.of(GuideGUIType.FROM);
            }
        }
        for (ItemGenerator generator : recipe.getResultGenerators()) {
            if (generator.registryId().equals(itemId)) {
                return Optional.of(GuideGUIType.TO);
            }
        }
        return Optional.empty();
    }

    public GuideGUIManager(@NotNull NamespacedKey itemId) {
        this.itemId = itemId;
        this.inventorys = ArrayListMultimap.create();
        Collection<ArtisanRecipe> allRecipes = Registries.RECIPE.getAllRecipes();
        for (ArtisanRecipe recipe : allRecipes) {
            if (recipe.getKey().getNamespace().equals(NamespacedKey.MINECRAFT)) continue;
            final Optional<GuideGUIGenerator> optional = ((RecipeRegistryInternal) Registries.RECIPE).getGuide(recipe.getType());
            final Optional<GuideGUIType> typeOptional = hasItemInRecipe(recipe, itemId);
            if (optional.isPresent() && typeOptional.isPresent()) {
                this.inventorys.put(typeOptional.get(), new GuideGUIHolder(itemId, typeOptional.get(), this.inventorys.size(), optional.get(), recipe, Component.text("配方指南")));
            }
        }
    }

    public void openInventory(final Player player, final GuideGUIType type, int page) {
        if (page >= 0 && page < inventorys.get(type).size()) {
            player.closeInventory();
            player.openInventory(inventorys.get(type).get(page).getInventory());
        }
    }

    public boolean isEmpty() {
        return inventorys.isEmpty();
    }
}
