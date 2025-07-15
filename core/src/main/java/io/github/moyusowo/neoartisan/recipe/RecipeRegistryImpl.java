package io.github.moyusowo.neoartisan.recipe;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.RegisterManager;
import io.github.moyusowo.neoartisan.recipe.internal.RecipeRegistryInternal;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisan.util.ArrayKey;
import io.github.moyusowo.neoartisan.util.terminate.TerminateMethod;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.recipe.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.*;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

final class RecipeRegistryImpl implements Listener, RecipeRegistry, RecipeRegistryInternal {

    private static RecipeRegistryImpl instance;

    public static RecipeRegistryImpl getInstance() {
        return instance;
    }

    @InitMethod(priority = InitPriority.REGISTRY_LOAD)
    static void init() {
        new RecipeRegistryImpl();
    }

    private RecipeRegistryImpl() {
        instance = this;
        toKey = new ConcurrentHashMap<>();
        recipeRegistry = new ConcurrentHashMap<>();
        NeoArtisan.registerListener(instance);
        Bukkit.getServicesManager().register(
                RecipeRegistry.class,
                RecipeRegistryImpl.getInstance(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    @InitMethod(priority = InitPriority.INTERNAL_REGISTER)
    static void registerMinecraftFurnaceRecipe() {
        List<NamespacedKey> remove = new ArrayList<>();
        var it = Bukkit.recipeIterator();
        while (it.hasNext()) {
            Recipe recipe = it.next();
            if (recipe instanceof FurnaceRecipe furnaceRecipe) {
                if (furnaceRecipe.getInputChoice() instanceof RecipeChoice.MaterialChoice materialChoice) {
                    for (Material material : materialChoice.getChoices()) {
                        instance.internalRegister(
                                ArtisanFurnaceRecipe.factory().builder()
                                        .key(NamespacedKey.minecraft(furnaceRecipe.getKey().getKey() + "_originalfurnace_" + UUID.randomUUID()))
                                        .inputItemId(material.getKey())
                                        .resultGenerator(
                                                ItemGenerator.simpleGenerator(
                                                        furnaceRecipe.getResult().getType().getKey(),
                                                        furnaceRecipe.getResult().getAmount()
                                                )
                                        )
                                        .cookTime(furnaceRecipe.getCookingTime())
                                        .exp(furnaceRecipe.getExperience())
                                        .build()
                        );
                    }
                    remove.add(furnaceRecipe.getKey());
                }
            } else if (recipe instanceof CampfireRecipe campfireRecipe) {
                if (campfireRecipe.getInputChoice() instanceof RecipeChoice.MaterialChoice materialChoice) {
                    for (Material material : materialChoice.getChoices()) {
                        instance.internalRegister(
                                ArtisanCampfireRecipe.factory().builder()
                                        .key(NamespacedKey.minecraft(campfireRecipe.getKey().getKey() + "_originalcampfire_" + UUID.randomUUID() + "_" + UUID.randomUUID()))
                                        .inputItemId(material.getKey())
                                        .resultGenerator(
                                                ItemGenerator.simpleGenerator(
                                                        campfireRecipe.getResult().getType().getKey(),
                                                        campfireRecipe.getResult().getAmount()
                                                )
                                        )
                                        .cookTime(campfireRecipe.getCookingTime())
                                        .exp(campfireRecipe.getExperience())
                                        .build()
                        );
                    }
                    remove.add(campfireRecipe.getKey());
                }
            } else if (recipe instanceof BlastingRecipe blastingRecipe) {
                if (blastingRecipe.getInputChoice() instanceof RecipeChoice.MaterialChoice materialChoice) {
                    for (Material material : materialChoice.getChoices()) {
                        instance.internalRegister(
                                ArtisanBlastingRecipe.factory().builder()
                                        .key(NamespacedKey.minecraft(blastingRecipe.getKey().getKey() + "_originalblasting_" + UUID.randomUUID() + "_" + UUID.randomUUID()))
                                        .inputItemId(material.getKey())
                                        .resultGenerator(
                                                ItemGenerator.simpleGenerator(
                                                        blastingRecipe.getResult().getType().getKey(),
                                                        blastingRecipe.getResult().getAmount()
                                                )
                                        )
                                        .cookTime(blastingRecipe.getCookingTime())
                                        .exp(blastingRecipe.getExperience())
                                        .build()
                        );
                    }
                    remove.add(blastingRecipe.getKey());
                }
            } else if (recipe instanceof SmokingRecipe smokingRecipe) {
                if (smokingRecipe.getInputChoice() instanceof RecipeChoice.MaterialChoice materialChoice) {
                    for (Material material : materialChoice.getChoices()) {
                        instance.internalRegister(
                                ArtisanSmokingRecipe.factory().builder()
                                        .key(NamespacedKey.minecraft(smokingRecipe.getKey().getKey() + "_originalsmoking_" + UUID.randomUUID() + "_" + UUID.randomUUID()))
                                        .inputItemId(material.getKey())
                                        .resultGenerator(
                                                ItemGenerator.simpleGenerator(
                                                        smokingRecipe.getResult().getType().getKey(),
                                                        smokingRecipe.getResult().getAmount()
                                                )
                                        )
                                        .cookTime(smokingRecipe.getCookingTime())
                                        .exp(smokingRecipe.getExperience())
                                        .build()
                        );
                    }
                    remove.add(smokingRecipe.getKey());
                }
            }
        }
        remove.forEach(Bukkit::removeRecipe);
    }

    private final ConcurrentHashMap<NamespacedKey, ArrayKey> toKey;
    private final ConcurrentHashMap<ArrayKey, ArtisanRecipe> recipeRegistry;

    @Override
    public void register(@NotNull ArtisanRecipe recipe) {
        if (RegisterManager.isOpen()) {
            ArrayKey arrayKey = ArrayKey.from(recipe.getInputs(), recipe.getType());
            toKey.put(recipe.getKey(), arrayKey);
            recipeRegistry.put(arrayKey, recipe);
            switch (recipe.getType()) {
                case SHAPED -> NeoArtisan.logger().info("successfully register shaped recipe: " + recipe.getKey().asString());
                case SHAPELESS -> NeoArtisan.logger().info("successfully register shapeless recipe: " + recipe.getKey().asString());
                case FURNACE -> NeoArtisan.logger().info("successfully register furnace recipe: " + recipe.getKey().asString());
                case CAMPFIRE -> NeoArtisan.logger().info("successfully register campfire recipe: " + recipe.getKey().asString());
                case SMOKING -> NeoArtisan.logger().info("successfully register smoking recipe: " + recipe.getKey().asString());
                case BLASTING -> NeoArtisan.logger().info("successfully register blasting recipe: " + recipe.getKey().asString());
            }
        } else {
            throw RegisterManager.REGISTRY_CLOSED;
        }
    }

    public void internalRegister(@NotNull ArtisanRecipe recipe) {
        if (RegisterManager.isOpen()) {
            ArrayKey arrayKey = ArrayKey.from(recipe.getInputs(), recipe.getType());
            toKey.put(recipe.getKey(), arrayKey);
            recipeRegistry.put(arrayKey, recipe);
            if (NeoArtisan.isDebugMode()) {
                switch (recipe.getType()) {
                    case SHAPED -> NeoArtisan.logger().info("successfully register shaped recipe: " + recipe.getKey().asString());
                    case SHAPELESS -> NeoArtisan.logger().info("successfully register shapeless recipe: " + recipe.getKey().asString());
                    case FURNACE -> NeoArtisan.logger().info("successfully register furnace recipe: " + recipe.getKey().asString());
                    case CAMPFIRE -> NeoArtisan.logger().info("successfully register campfire recipe: " + recipe.getKey().asString());
                    case SMOKING -> NeoArtisan.logger().info("successfully register smoking recipe: " + recipe.getKey().asString());
                    case BLASTING -> NeoArtisan.logger().info("successfully register blasting recipe: " + recipe.getKey().asString());
                }
            }
        } else {
            throw RegisterManager.REGISTRY_CLOSED;
        }
    }

    @Override
    public boolean hasRecipe(NamespacedKey key) {
        return toKey.containsKey(key);
    }

    @Override
    public @NotNull ArtisanRecipe getRecipe(@NotNull NamespacedKey key) {
        return recipeRegistry.get(toKey.get(key));
    }

    @TerminateMethod
    static void resetRecipe() {
        Bukkit.resetRecipes();
        NeoArtisan.logger().info("Successfully reset recipes.");
    }

    @Override
    public boolean has(ArrayKey arrayKey) {
        return recipeRegistry.containsKey(arrayKey);
    }

    @Override
    public @NotNull ArtisanRecipe get(ArrayKey arrayKey) {
        return recipeRegistry.get(arrayKey);
    }
}
