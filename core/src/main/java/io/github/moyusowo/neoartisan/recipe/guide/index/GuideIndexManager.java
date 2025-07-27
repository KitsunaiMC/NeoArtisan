package io.github.moyusowo.neoartisan.recipe.guide.index;

import io.github.moyusowo.neoartisan.recipe.guide.item.GuideGUIManager;
import io.github.moyusowo.neoartisan.recipe.guide.item.GuideGUIType;
import io.github.moyusowo.neoartisanapi.api.registry.Registries;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class GuideIndexManager {
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
