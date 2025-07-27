package io.github.moyusowo.neoartisan.recipe.guide.category;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.github.moyusowo.neoartisan.recipe.guide.index.GuideIndexManager;
import io.github.moyusowo.neoartisan.recipe.guide.item.GuideGUIType;
import io.github.moyusowo.neoartisan.registry.internal.RecipeRegistryInternal;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.recipe.guide.ItemCategories;
import io.github.moyusowo.neoartisanapi.api.registry.Registries;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class GuideCategoryManager {
    private GuideCategoryManager() {}

    private static final Map<NamespacedKey, Integer> categoryToId = new HashMap<>();
    private static final List<GuideIndexManager> indexs = new ArrayList<>();
    private static final List<GuideCategoryHolder> holders = new ArrayList<>();

    @InitMethod(priority = InitPriority.STARTUP)
    public static void init() {
        final Multimap<NamespacedKey, NamespacedKey> idsByCategory = ArrayListMultimap.create();
        for (Material material : Material.values()) {
            if (!material.isLegacy() && material.isItem()) {
                idsByCategory.put(ItemCategories.ORIGINAL, material.getKey());
            }
        }
        Registries.ITEM.getAllIds().forEach(
                id -> idsByCategory.put(Registries.ITEM.getArtisanItem(id).getCategory(), id)
        );
        int i = 0;
        final List<ItemStack> itemStacks = new ArrayList<>();
        for (NamespacedKey category : idsByCategory.keySet()) {
            if (i == 36) {
                holders.add(new GuideCategoryHolder(holders.size(), itemStacks));
                itemStacks.clear();
                i = 0;
            }
            Optional<ItemStack> optional = ((RecipeRegistryInternal) Registries.RECIPE).getCategory(category);
            if (optional.isPresent()) {
                itemStacks.add(optional.get().clone());
                categoryToId.put(category, indexs.size());
                indexs.add(new GuideIndexManager(category, idsByCategory.get(category)));
                i++;
            }
        }
        if (!itemStacks.isEmpty()) {
            holders.add(new GuideCategoryHolder(holders.size(), itemStacks));
            itemStacks.clear();
        }
    }

    public static void openCategoryGuide(@NotNull final Player player, int page) {
        if (page >= 0 && page < holders.size()) {
            player.openInventory(holders.get(page).getInventory());
        }
    }

    private static void openIndexGuide(@NotNull final Player player, @NotNull NamespacedKey category, int page) {
        if (!categoryToId.containsKey(category)) return;
        indexs.get(categoryToId.get(category)).openIndex(player, page);
    }

    public static void openIndexGuide(@NotNull final Player player, @NotNull NamespacedKey key, int page, boolean isId) {
        if (!isId) openIndexGuide(player, key, page);
        else {
            final NamespacedKey category;
            if (Registries.ITEM.isArtisanItem(key)) category = Registries.ITEM.getArtisanItem(key).getCategory();
            else category = ItemCategories.ORIGINAL;
            if (!categoryToId.containsKey(category)) return;
            indexs.get(categoryToId.get(category)).openIndex(player, page);
        }
    }

    public static void openIndexGuide(@NotNull final Player player, int slot, int page) {
        indexs.get(slot).openIndex(player, page);
    }

    public static void openItemGuide(@NotNull NamespacedKey itemId, @NotNull final Player player, @NotNull GuideGUIType type, int page) {
        final NamespacedKey category;
        if (Registries.ITEM.isArtisanItem(itemId)) category = Registries.ITEM.getArtisanItem(itemId).getCategory();
        else category = ItemCategories.ORIGINAL;
        if (!categoryToId.containsKey(category)) return;
        indexs.get(categoryToId.get(category)).openItemGuide(itemId, player, type, page);
    }
}
