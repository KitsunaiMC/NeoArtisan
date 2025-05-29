package io.github.moyusowo.neoartisan.recipe;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.RegisterManager;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisan.util.ArrayKey;
import io.github.moyusowo.neoartisan.util.terminate.TerminateMethod;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanFurnaceRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanShapedRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanShapelessRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.RecipeRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

final class RecipeRegistryImpl implements Listener, RecipeRegistry {

    private static RecipeRegistryImpl instance;

    public static RecipeRegistryImpl getInstance() {
        return instance;
    }

    @InitMethod(priority = InitPriority.REGISTRY_LOAD)
    public static void init() {
        new RecipeRegistryImpl();
    }

    private RecipeRegistryImpl() {
        instance = this;
        shapedRegistry = new ConcurrentHashMap<>();
        shapelessRegistry = new ConcurrentHashMap<>();
        furnaceRegistry = new ConcurrentHashMap<>();
        registerMinecraftFurnaceRecipe();
        registerListener();
        Bukkit.getServicesManager().register(
                RecipeRegistry.class,
                RecipeRegistryImpl.getInstance(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private void registerMinecraftFurnaceRecipe() {
        List<NamespacedKey> remove = new ArrayList<>();
        var it = Bukkit.recipeIterator();
        while (it.hasNext()) {
            Recipe recipe = it.next();
            if (recipe instanceof FurnaceRecipe furnaceRecipe) {
                if (furnaceRecipe.getInputChoice() instanceof RecipeChoice.MaterialChoice materialChoice) {
                    for (Material material : materialChoice.getChoices()) {
                        NeoArtisan.logger().info(material.getKey().asString() + " " + furnaceRecipe.getResult().getType().getKey().asString() + " " + furnaceRecipe.getResult().getAmount() + " " + furnaceRecipe.getCookingTime() + " " + furnaceRecipe.getExperience());
                        register(material.getKey(), new ArtisanFurnaceRecipeImpl(material.getKey(), furnaceRecipe.getResult().getType().getKey(), furnaceRecipe.getResult().getAmount(), furnaceRecipe.getCookingTime(), furnaceRecipe.getExperience()));
                    }
                    remove.add(furnaceRecipe.getKey());
                }
            }
        }
        remove.forEach(Bukkit::removeRecipe);
    }

    final ConcurrentHashMap<ArrayKey, ArtisanShapedRecipe> shapedRegistry;
    final ConcurrentHashMap<ArrayKey, ArtisanShapelessRecipe> shapelessRegistry;
    final ConcurrentHashMap<NamespacedKey, ArtisanFurnaceRecipe> furnaceRegistry;

    @Override
    @NotNull
    public ArtisanShapedRecipe createShapedRecipe(@NotNull String line1, @NotNull String line2, @NotNull String line3) {
        try {
            if (RegisterManager.isOpen()) {
                return new ArtisanShapedRecipeImpl(line1, line2, line3);
            } else {
                throw RegisterManager.RegisterException.exception();
            }
        } catch (RegisterManager.RegisterException e) {
            NeoArtisan.logger().severe(RegisterManager.eTips);
        }
        return null;
    }

    @Override
    @NotNull
    public ArtisanShapelessRecipe createShapelessRecipe() {
        try {
            if (RegisterManager.isOpen()) {
                return new ArtisanShapelessRecipeImpl();
            } else {
                throw RegisterManager.RegisterException.exception();
            }
        } catch (RegisterManager.RegisterException e) {
            NeoArtisan.logger().severe(RegisterManager.eTips);
        }
        return null;
    }

    @Override
    @NotNull
    public ArtisanShapelessRecipe createShapelessRecipe(NamespacedKey result, int count) {
        try {
            if (RegisterManager.isOpen()) {
                return new ArtisanShapelessRecipeImpl(result, count);
            } else {
                throw RegisterManager.RegisterException.exception();
            }
        } catch (RegisterManager.RegisterException e) {
            NeoArtisan.logger().severe(RegisterManager.eTips);
        }
        return null;
    }

    @Override
    public @NotNull ArtisanFurnaceRecipe createFurnaceRecipe(@NotNull NamespacedKey input, @NotNull NamespacedKey result, int count, int cookTime, int exp) {
        try {
            if (RegisterManager.isOpen()) {
                return new ArtisanFurnaceRecipeImpl(input, result, count, cookTime, exp);
            } else {
                throw RegisterManager.RegisterException.exception();
            }
        } catch (RegisterManager.RegisterException e) {
            NeoArtisan.logger().severe(RegisterManager.eTips);
        }
        return null;
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

    void register(NamespacedKey identifier, ArtisanFurnaceRecipe r) {
        furnaceRegistry.put(identifier, r);
    }

    @TerminateMethod
    static void resetRecipe() {
        Bukkit.resetRecipes();
    }

}
