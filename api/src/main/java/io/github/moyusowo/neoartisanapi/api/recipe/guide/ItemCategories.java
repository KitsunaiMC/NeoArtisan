package io.github.moyusowo.neoartisanapi.api.recipe.guide;

import org.bukkit.NamespacedKey;

/**
 * Default item categories for the recipe book.
 * <p>
 * Provides predefined categories for organizing items and recipes in the recipe book.
 * </p>
 */
public final class ItemCategories {
    private ItemCategories() {}

    /**
     * Original/vanilla items category
     */
    public static final NamespacedKey ORIGINAL = NamespacedKey.minecraft("category/original");

    public static final NamespacedKey COMBAT = NamespacedKey.minecraft("category/combat");

    public static final NamespacedKey DECORATIONS = NamespacedKey.minecraft("category/decorations");

    public static final NamespacedKey FOOD = NamespacedKey.minecraft("category/food");

    public static final NamespacedKey MISC = NamespacedKey.minecraft("category/misc");

    public static final NamespacedKey TOOLS = NamespacedKey.minecraft("category/tools");
}
