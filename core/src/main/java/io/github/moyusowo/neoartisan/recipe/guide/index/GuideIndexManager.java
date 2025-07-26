package io.github.moyusowo.neoartisan.recipe.guide.index;

import io.github.moyusowo.neoartisan.recipe.guide.item.GuideGUIManager;
import io.github.moyusowo.neoartisan.recipe.guide.item.GuideGUIType;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.registry.Registries;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GuideIndexManager {
    private GuideIndexManager() {}

    private static final Map<NamespacedKey, GuideGUIManager> guides = new HashMap<>();
    private static final List<GuideIndexHolder> indexs = new ArrayList<>();

    @InitMethod(priority = InitPriority.STARTUP)
    public static void registerGuides() {
        for (Material material : Material.values()) {
            if (!material.isLegacy() && material.isItem()) {
                final GuideGUIManager guideGUIManager = new GuideGUIManager(material.getKey());
                if (!guideGUIManager.isEmpty()) {
                    guides.put(
                            material.getKey(),
                            guideGUIManager
                    );
                }
            }
        }
        Registries.ITEM.getAllIds().forEach(
                id -> {
                    final GuideGUIManager guideGUIManager = new GuideGUIManager(id);
                    if (!guideGUIManager.isEmpty()) {
                        guides.put(id, new GuideGUIManager(id));
                    }
                }
        );
        int i = 0;
        final List<NamespacedKey> keys = new ArrayList<>();
        for (NamespacedKey key : guides.keySet()) {
            if (i == 36) {
                indexs.add(new GuideIndexHolder(indexs.size(), keys));
                keys.clear();
                i = 0;
            }
            keys.add(key);
            i++;
        }
        if (!keys.isEmpty()) {
            indexs.add(new GuideIndexHolder(indexs.size(), keys));
            keys.clear();
        }
    }

    public static void openItemGuide(@NotNull NamespacedKey itemId, @NotNull Player player, @NotNull GuideGUIType type, int page) {
        if (!guides.containsKey(itemId) || guides.get(itemId).isEmpty()) return;
        guides.get(itemId).openInventory(player, type, page);
    }

    public static void openIndex(@NotNull Player player, int page) {
        if (page >= 0 && page < indexs.size()) {
            player.openInventory(indexs.get(page).getInventory());
        }
    }
}
