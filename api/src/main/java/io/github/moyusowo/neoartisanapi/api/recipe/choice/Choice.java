package io.github.moyusowo.neoartisanapi.api.recipe.choice;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface Choice {
    Choice EMPTY = itemStack -> itemStack == null || itemStack.isEmpty();

    boolean matches(@Nullable ItemStack itemStack);
}
