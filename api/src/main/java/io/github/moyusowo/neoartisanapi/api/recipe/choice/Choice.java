package io.github.moyusowo.neoartisanapi.api.recipe.choice;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * Item matching interface used for recipe matching with ItemStacks.
 */
@ApiStatus.NonExtendable
public interface Choice {
    /**
     * Empty choice instance that matches null and empty
     */
    Choice EMPTY = new EmptyChoice();

    /**
     * Checks if the given item stack matches this choice
     *
     * @param itemStack the item stack to check (can be null)
     * @return true if the item stack matches, false otherwise
     */
    boolean matches(@Nullable ItemStack itemStack);

    /**
     * Checks if the given item ID matches this choice
     *
     * @param itemId the item ID to check (can be null)
     * @return true if the item ID matches, false otherwise
     */
    boolean matches(@Nullable NamespacedKey itemId);
}
