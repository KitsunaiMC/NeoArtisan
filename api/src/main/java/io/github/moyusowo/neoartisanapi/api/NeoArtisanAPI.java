package io.github.moyusowo.neoartisanapi.api;

import io.github.moyusowo.neoartisanapi.api.attribute.GlobalAttributeRegistry;
import io.github.moyusowo.neoartisanapi.api.attribute.ItemStackAttributeRegistry;
import io.github.moyusowo.neoartisanapi.api.attribute.PlayerAttributeRegistry;
import io.github.moyusowo.neoartisanapi.api.block.base.BlockRegistry;
import io.github.moyusowo.neoartisanapi.api.block.storage.ArtisanBlockStorage;
import io.github.moyusowo.neoartisanapi.api.item.ItemRegistry;
import io.github.moyusowo.neoartisanapi.api.persistence.EmptyPersistentDataContainer;
import io.github.moyusowo.neoartisanapi.api.recipe.RecipeRegistry;
import org.bukkit.Bukkit;

@SuppressWarnings("unused")
public final class NeoArtisanAPI {

    public static GlobalAttributeRegistry getGlobalAttributeRegistry() {
        return Bukkit.getServicesManager().load(GlobalAttributeRegistry.class);
    }

    public static ItemStackAttributeRegistry getItemStackAttributeRegistry() {
        return Bukkit.getServicesManager().load(ItemStackAttributeRegistry.class);
    }

    public static PlayerAttributeRegistry getPlayerAttributeRegistry() {
        return Bukkit.getServicesManager().load(PlayerAttributeRegistry.class);
    }

    public static BlockRegistry getBlockRegistry() {
        return Bukkit.getServicesManager().load(BlockRegistry.class);
    }

    public static ItemRegistry getItemRegistry() {
        return Bukkit.getServicesManager().load(ItemRegistry.class);
    }

    public static RecipeRegistry getRecipeRegistry() {
        return Bukkit.getServicesManager().load(RecipeRegistry.class);
    }

    public static ArtisanBlockStorage getArtisanBlockStorage() {
        return Bukkit.getServicesManager().load(ArtisanBlockStorage.class);
    }

    public static EmptyPersistentDataContainer emptyPersistentDataContainer() {
        return Bukkit.getServicesManager().load(EmptyPersistentDataContainer.class);
    }

}
