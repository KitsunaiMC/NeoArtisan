package io.github.moyusowo.neoartisan.recipe;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.RegisterManager;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisan.util.ArrayKey;
import io.github.moyusowo.neoartisan.util.terminate.TerminateMethod;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanShapedRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanShapelessRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.RecipeRegistry;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

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
        registerListener();
        Bukkit.getServicesManager().register(
                RecipeRegistry.class,
                RecipeRegistryImpl.getInstance(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    final ConcurrentHashMap<ArrayKey, ArtisanShapedRecipe> shapedRegistry;
    final ConcurrentHashMap<ArrayKey, ArtisanShapelessRecipe> shapelessRegistry;

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
            NeoArtisan.logger().info(RegisterManager.eTips);
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
            NeoArtisan.logger().info(RegisterManager.eTips);
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
            NeoArtisan.logger().info(RegisterManager.eTips);
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

    @TerminateMethod
    static void resetRecipe() {
        Bukkit.resetRecipes();
    }

}
