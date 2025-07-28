package io.github.moyusowo.neoartisanapi.api.recipe.guide;

import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanRecipe;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Interface for generating recipe guide GUIs.
 * <p>
 * This interface is used by the recipe book to handle specific recipes.
 * When creating a GUI, the recipe book provides a recipe and asks for a GUI
 * that describes the recipe. The returned buttons will be placed at
 * specified positions in the GUI.
 * </p>
 */
public interface GuideGUIGenerator {
    /**
     * Represents a button in the recipe guide GUI
     */
    record Button(@NotNull ItemStack itemStack, int slot) {
        @NotNull
        public ItemStack itemStack() {
            return itemStack.clone();
        }
    }

    /**
     * Gets the button for navigating to the previous page
     *
     * @return the previous page button
     */
    @NotNull
    Button getLastPageButton();

    /**
     * Gets the button for navigating to the next page
     *
     * @return the next page button
     */
    @NotNull
    Button getNextPageButton();

    /**
     * Gets the button for returning
     *
     * @return the return button
     */
    @NotNull
    Button getReturnButton();

    /**
     * Converts a recipe to an inventory GUI
     *
     * @param recipe the recipe to display (cannot be null)
     * @param holder the inventory holder (cannot be null)
     * @param title the GUI title (cannot be null)
     * @return the inventory representing the recipe
     */
    @NotNull
    Inventory recipeToInventory(@NotNull ArtisanRecipe recipe, @NotNull InventoryHolder holder, @NotNull Component title);
}
