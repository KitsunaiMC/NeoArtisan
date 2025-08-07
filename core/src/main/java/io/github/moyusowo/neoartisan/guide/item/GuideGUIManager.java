package io.github.moyusowo.neoartisan.guide.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.github.moyusowo.neoartisan.registry.internal.GuideRegistryInternal;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.Choice;
import io.github.moyusowo.neoartisanapi.api.recipe.guide.GuideGUIGenerator;
import io.github.moyusowo.neoartisanapi.api.registry.Registries;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class GuideGUIManager {
    private static final Map<NamespacedKey, GuideGUIManager> managers = new HashMap<>();

    @InitMethod(priority = InitPriority.STARTUP)
    static void init() {
        for (Material material : Material.values()) {
            if (!material.isLegacy() && material.isItem()) {
                managers.put(material.getKey(), new GuideGUIManager(material.getKey()));
            }
        }
        for (NamespacedKey itemId : Registries.ITEM.getAllIds()) {
            managers.put(itemId, new GuideGUIManager(itemId));
        }
    }

    public static void openItemGuide(@NotNull NamespacedKey itemId, @NotNull final Player player, @NotNull GuideGUIType type, int page) {
        if (managers.containsKey(itemId)) {
            managers.get(itemId).openInventory(player, type, page);
        }
    }

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
        final Multimap<NamespacedKey, ArtisanRecipe> allRecipesByType = Registries.RECIPE.getAllRecipesByType();
        allRecipesByType.forEach(
                (type, recipe) -> {
                    if (recipe.getKey().getNamespace().equals(NamespacedKey.MINECRAFT)) return;
                    final Optional<GuideGUIGenerator> optional = GuideRegistryInternal.getInstance().getGuide(recipe.getType());
                    final Optional<GuideGUIType> typeOptional = hasItemInRecipe(recipe, itemId);
                    if (optional.isPresent() && typeOptional.isPresent()) {
                        this.inventorys.put(typeOptional.get(), new GuideGUIHolder(itemId, typeOptional.get(), this.inventorys.size(), optional.get(), recipe, Component.text("配方指南")));
                    }
                }
        );
    }

    public void openInventory(final Player player, final GuideGUIType type, int page) {
        if (page >= 0 && page < inventorys.get(type).size()) {
            player.openInventory(inventorys.get(type).get(page).getInventory());
        }
    }

    public boolean isEmpty() {
        return inventorys.isEmpty();
    }
}
