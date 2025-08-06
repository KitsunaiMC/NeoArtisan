package io.github.moyusowo.neoartisanapi.api.registry;

import io.github.moyusowo.neoartisanapi.api.recipe.guide.GuideGUIGenerator;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@ApiStatus.NonExtendable
public interface GuideRegistry {
    void setGuideGenerator(@NotNull NamespacedKey recipeType, @NotNull GuideGUIGenerator generator);

    void registerCategory(@NotNull NamespacedKey category, @NotNull Supplier<ItemStack> iconItemStack);

    void registerCategory(@NotNull NamespacedKey category, @NotNull ItemStack iconItemStack);

    void registerCategory(@NotNull NamespacedKey category, @NotNull NamespacedKey iconItemId);

    void registerGuideBook(@NotNull Plugin plugin, @NotNull Supplier<ItemStack> bookItemStack, @NotNull Set<NamespacedKey> categories);

    void registerGuideBook(@NotNull Plugin plugin, @NotNull ItemStack bookItemStack, @NotNull Set<NamespacedKey> categories);

    void registerGuideBook(@NotNull Plugin plugin, @NotNull NamespacedKey bookItemId, @NotNull Set<NamespacedKey> categories);

    void openGuideBook(@NotNull Plugin plugin, @NotNull Player player);

    @NotNull
    ItemStack getGuideBook(@NotNull Plugin plugin);
}
