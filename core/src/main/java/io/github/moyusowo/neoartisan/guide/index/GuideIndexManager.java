package io.github.moyusowo.neoartisan.guide.index;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.github.moyusowo.neoartisan.guide.item.GuideGUIManager;
import io.github.moyusowo.neoartisan.guide.item.GuideGUIType;
import io.github.moyusowo.neoartisan.registry.internal.GuideRegistryInternal;
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

public final class GuideIndexManager {
    private static final Map<NamespacedKey, GuideIndexManager> managers = new HashMap<>();

    @InitMethod(priority = InitPriority.STARTUP)
    static void init() {
        final Multimap<NamespacedKey, NamespacedKey> idsByCategory = ArrayListMultimap.create();
        for (Material material : Material.values()) {
            if (!material.isLegacy() && material.isItem()) {
                idsByCategory.put(ItemCategories.ORIGINAL, material.getKey());
            }
        }
        Registries.ITEM.getAllIds().forEach(
                id -> idsByCategory.put(Registries.ITEM.getArtisanItem(id).getCategory(), id)
        );
        for (NamespacedKey category : idsByCategory.keySet()) {
            managers.put(category, new GuideIndexManager(category, idsByCategory.get(category)));
        }
    }

    public static void openIndexGuideByCategory(@NotNull final Player player, @NotNull NamespacedKey category, int page) {
        if (managers.containsKey(category)) {
            managers.get(category).openIndex(player, page);
        }
    }

    public static void openIndexGuideByItem(@NotNull final Player player, @NotNull NamespacedKey itemId, int page) {
        if (Registries.ITEM.isArtisanItem(itemId)) {
            openIndexGuideByCategory(player, Registries.ITEM.getArtisanItem(itemId).getCategory(), page);
        } else {
            openIndexGuideByCategory(player, ItemCategories.ORIGINAL, page);
        }
    }

    public static void openIndexGuideByCategoryItemStack(@NotNull final Player player, @NotNull ItemStack itemStack, int page) {
        Optional<NamespacedKey> category = GuideRegistryInternal.getInstance().getCategoryPDC(itemStack);
        category.ifPresent(key -> openIndexGuideByCategory(player, key, page));
    }

    public GuideIndexManager(@NotNull NamespacedKey category, @NotNull Collection<NamespacedKey> itemIds) {
        this.category = category;
        for (NamespacedKey itemId : itemIds) {
            final GuideGUIManager guideGUIManager = new GuideGUIManager(itemId);
            if (!guideGUIManager.isEmpty() || Registries.ITEM.isArtisanItem(itemId)) {
                guides.put(itemId, guideGUIManager);
            }
        }
        int i = 0;
        final List<NamespacedKey> keys = new ArrayList<>();
        for (NamespacedKey key : guides.keySet()) {
            if (i == 36) {
                indexs.add(new GuideIndexHolder(indexs.size(), keys, category));
                keys.clear();
                i = 0;
            }
            keys.add(key);
            i++;
        }
        if (!keys.isEmpty()) {
            indexs.add(new GuideIndexHolder(indexs.size(), keys, category));
            keys.clear();
        }
    }

    public final NamespacedKey category;
    private final Map<NamespacedKey, GuideGUIManager> guides = new HashMap<>();
    private final List<GuideIndexHolder> indexs = new ArrayList<>();

    public void openItemGuide(@NotNull NamespacedKey itemId, @NotNull Player player, @NotNull GuideGUIType type, int page) {
        if (!guides.containsKey(itemId) || guides.get(itemId).isEmpty()) return;
        guides.get(itemId).openInventory(player, type, page);
    }

    public void openIndex(@NotNull Player player, int page) {
        if (page >= 0 && page < indexs.size()) {
            player.openInventory(indexs.get(page).getInventory());
        }
    }
}
