package io.github.moyusowo.neoartisan.recipe;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.ArrayKey;
import io.github.moyusowo.neoartisanapi.api.item.ItemRegistry;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanShapedRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanShapelessRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.RecipeRegistry;
import io.github.moyusowo.neoartisan.util.Util;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Furnace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

final class RecipeRegistryImpl implements Listener, RecipeRegistry {

    private static final String EMPTY_KEY = "empty_item_registry_id";
    public static final NamespacedKey EMPTY = new NamespacedKey(NeoArtisan.instance(), EMPTY_KEY);

    private static RecipeRegistryImpl instance;

    public static RecipeRegistryImpl getInstance() {
        return instance;
    }

    public static void init() {
        new RecipeRegistryImpl();
    }

    private RecipeRegistryImpl() {
        instance = this;
        shapedRegistry = new ConcurrentHashMap<>();
        shapelessRegistry = new ConcurrentHashMap<>();
        registerListener();
    }

    final ConcurrentHashMap<ArrayKey, ArtisanShapedRecipe> shapedRegistry;
    final ConcurrentHashMap<ArrayKey, ArtisanShapelessRecipe> shapelessRegistry;

    @Override
    @NotNull
    public ArtisanShapedRecipe createShapedRecipe(@NotNull String line1, @NotNull String line2, @NotNull String line3) {
        return new ArtisanShapedRecipeImpl(line1, line2, line3);
    }

    @Override
    @NotNull
    public ArtisanShapelessRecipe createShapelessRecipe() {
        return new ArtisanShapelessRecipeImpl();
    }

    @Override
    @NotNull
    public ArtisanShapelessRecipe createShapelessRecipe(NamespacedKey result, int count) {
        return new ArtisanShapelessRecipeImpl(result, count);
    }

    public void registerListener() {
        NeoArtisan.registerListener(instance);
    }

    void register(ArrayKey identifier, ArtisanShapedRecipe r) {
        shapedRegistry.put(identifier, r);
    }

    void register(ArrayKey identifier, ArtisanShapelessRecipe r) {
        shapelessRegistry.put(identifier, r);
    }

}
