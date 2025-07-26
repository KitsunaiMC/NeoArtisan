package io.github.moyusowo.neoartisanapi.api.recipe.guide;

import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanRecipe;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface GuideGUIGenerator {
    record Button(@NotNull ItemStack itemStack, int slot) {
        @NotNull
        public ItemStack itemStack() {
            return itemStack.clone();
        }
    }

    @NotNull
    Button getLastPageButton();

    @NotNull
    Button getNextPageButton();

    @NotNull
    Button getReturnButton();

    @NotNull
    Inventory recipeToInventory(@NotNull ArtisanRecipe recipe, @NotNull InventoryHolder holder, @NotNull Component title);
}
