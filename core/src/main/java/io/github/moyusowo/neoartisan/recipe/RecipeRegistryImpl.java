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
        registerFromFile();
        NeoArtisan.logger().info("成功从文件注册 " + shapedRegistry.size() + " 个自定义有序配方");
        NeoArtisan.logger().info("成功从文件注册 " + shapelessRegistry.size() + " 个自定义无序配方");
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

    public void registerFromFile() {
        File[] files = ReadUtil.readAllFiles();
        if (files != null) {
            for (File file : files) {
                if (ReadUtil.isYmlFile(file)) {
                    readYml(YamlConfiguration.loadConfiguration(file));
                }
            }
        }
    }

    private void readYml(YamlConfiguration yml) {
        String recipeType = ReadUtil.getRecipeType(yml);
        if (recipeType.equals("shaped")) readShaped(yml);
        else if (recipeType.equals("shapeless")) readShapeless(yml);
    }

    private void readShaped(YamlConfiguration yml) {
        List<String> shape = ReadUtil.getShaped(yml);
        ArtisanShapedRecipeImpl r = new ArtisanShapedRecipeImpl(shape.get(0), shape.get(1), shape.get(2));
        for (Map.Entry<Character, String> entry : ReadUtil.getShapedMappings(yml).entrySet()) {
            r.add(entry.getKey(), Util.stringToNamespaceKey(entry.getValue()));
        }
        r.setResult(Util.stringToNamespaceKey(ReadUtil.getResult(yml)), ReadUtil.getCount(yml));
        r.build();
    }

    private void readShapeless(YamlConfiguration yml) {
        List<String> items = ReadUtil.getShapelessItems(yml);
        ArtisanShapelessRecipeImpl r = new ArtisanShapelessRecipeImpl(Util.stringToNamespaceKey(ReadUtil.getResult(yml)), ReadUtil.getCount(yml));
        for (String item : items) {
            r.add(Util.stringToNamespaceKey(item));
        }
        r.build();
    }

    void register(ArrayKey identifier, ArtisanShapedRecipe r) {
        shapedRegistry.put(identifier, r);
    }

    void register(ArrayKey identifier, ArtisanShapelessRecipe r) {
        shapelessRegistry.put(identifier, r);
    }

}
