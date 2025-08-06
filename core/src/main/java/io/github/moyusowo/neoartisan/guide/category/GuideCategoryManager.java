package io.github.moyusowo.neoartisan.guide.category;

import io.github.moyusowo.neoartisan.registry.internal.GuideRegistryInternal;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public final class GuideCategoryManager {
    private final Map<NamespacedKey, Integer> categoryToId = new HashMap<>();
    private final List<GuideCategoryHolder> holders = new ArrayList<>();

    public GuideCategoryManager(Collection<NamespacedKey> categories) {
        int i = 0;
        List<ItemStack> itemStacks = new ArrayList<>();
        for (NamespacedKey category : categories) {
            if (i % 36 == 0 && i != 0) {
                holders.add(new GuideCategoryHolder(holders.size(), itemStacks));
                itemStacks = new ArrayList<>();
            }
            Optional<ItemStack> optional = GuideRegistryInternal.getInstance().getCategory(category);
            if (optional.isPresent()) {
                itemStacks.add(optional.get().clone());
                categoryToId.put(category, i);
                i++;
            }
        }
        if (!itemStacks.isEmpty()) {
            holders.add(new GuideCategoryHolder(holders.size(), itemStacks));
            itemStacks.clear();
        }
    }

    public void openCategoryGuide(@NotNull final Player player, int page) {
        if (page >= 0 && page < holders.size()) {
            player.openInventory(holders.get(page).getInventory());
        }
    }
}
