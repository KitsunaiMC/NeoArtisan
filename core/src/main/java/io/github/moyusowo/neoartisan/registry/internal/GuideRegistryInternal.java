package io.github.moyusowo.neoartisan.registry.internal;

import io.github.moyusowo.neoartisanapi.api.recipe.guide.GuideGUIGenerator;
import io.github.moyusowo.neoartisanapi.api.registry.GuideRegistry;
import io.github.moyusowo.neoartisanapi.api.util.ServiceUtil;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface GuideRegistryInternal extends GuideRegistry {
    static GuideRegistryInternal getInstance() {
        return (GuideRegistryInternal) ServiceUtil.getService(GuideRegistry.class);
    }

    @NotNull
    Optional<GuideGUIGenerator> getGuide(@NotNull NamespacedKey recipeType);

    @NotNull
    Optional<ItemStack> getCategory(@NotNull NamespacedKey category);

    @NotNull
    Optional<NamespacedKey> getCategoryPDC(@NotNull ItemStack itemStack);

    @NotNull
    Optional<String> getBookPDC(@NotNull ItemStack itemStack);

    void returnToGuideBook(@NotNull Player player);

    void openGuideBook(@NotNull String name, @NotNull Player player);

    @NotNull
    Optional<String> getPlayerCurrentBook(@NotNull Player player);
}
