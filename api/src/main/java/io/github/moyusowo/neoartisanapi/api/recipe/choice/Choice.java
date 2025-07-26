package io.github.moyusowo.neoartisanapi.api.recipe.choice;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.NonExtendable
public interface Choice {
    Choice EMPTY = new EmptyChoice();

    boolean matches(@Nullable ItemStack itemStack);

    boolean matches(@Nullable NamespacedKey itemId);
}
