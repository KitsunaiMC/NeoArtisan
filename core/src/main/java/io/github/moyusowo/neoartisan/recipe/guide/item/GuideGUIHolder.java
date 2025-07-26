package io.github.moyusowo.neoartisan.recipe.guide.item;

import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.guide.GuideGUIGenerator;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public final class GuideGUIHolder implements InventoryHolder {
    private final Inventory inventory;
    public final NamespacedKey itemId;
    public final GuideGUIType type;
    public final int page;
    public final int nextPageSlot, lastPageSlot, returnPageSlot;

    public GuideGUIHolder(@NotNull NamespacedKey itemId, @NotNull GuideGUIType type, int page, @NotNull GuideGUIGenerator generator, @NotNull ArtisanRecipe recipe, @NotNull Component component) {
        this.itemId = itemId;
        this.type = type;
        this.page = page;
        this.inventory = generator.recipeToInventory(recipe, this, component);
        this.nextPageSlot = generator.getNextPageButton().slot();
        this.lastPageSlot = generator.getLastPageButton().slot();
        this.returnPageSlot = generator.getReturnButton().slot();
        inventory.setItem(generator.getLastPageButton().slot(), generator.getLastPageButton().itemStack());
        inventory.setItem(generator.getNextPageButton().slot(), generator.getNextPageButton().itemStack());
        inventory.setItem(generator.getReturnButton().slot(), generator.getReturnButton().itemStack());
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
