package io.github.moyusowo.neoartisan.recipe;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.RegisterManager;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisan.util.terminate.TerminateMethod;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.recipe.*;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.Choice;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.ItemChoice;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.MultiChoice;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.*;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

final class RecipeRegistryImpl implements Listener, RecipeRegistry {

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
        recipeByType = ArrayListMultimap.create();
        recipeByKey = new HashMap<>();
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
                    final List<Choice> itemChoices = new ArrayList<>();
                    for (Material material : materialChoice.getChoices()) {
                        itemChoices.add(new ItemChoice(material.getKey()));
                    }
                    instance.internalRegister(
                            ArtisanFurnaceRecipe.builder()
                                    .key(furnaceRecipe.getKey())
                                    .input(new MultiChoice(itemChoices))
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
                    remove.add(furnaceRecipe.getKey());
                }
            } else if (recipe instanceof CampfireRecipe campfireRecipe) {
                if (campfireRecipe.getInputChoice() instanceof RecipeChoice.MaterialChoice materialChoice) {
                    final List<Choice> itemChoices = new ArrayList<>();
                    for (Material material : materialChoice.getChoices()) {
                        itemChoices.add(new ItemChoice(material.getKey()));
                    }
                    instance.internalRegister(
                            ArtisanFurnaceRecipe.builder()
                                    .key(campfireRecipe.getKey())
                                    .input(new MultiChoice(itemChoices))
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
                    remove.add(campfireRecipe.getKey());
                }
            } else if (recipe instanceof BlastingRecipe blastingRecipe) {
                if (blastingRecipe.getInputChoice() instanceof RecipeChoice.MaterialChoice materialChoice) {
                    final List<Choice> itemChoices = new ArrayList<>();
                    for (Material material : materialChoice.getChoices()) {
                        itemChoices.add(new ItemChoice(material.getKey()));
                    }
                    instance.internalRegister(
                            ArtisanFurnaceRecipe.builder()
                                    .key(blastingRecipe.getKey())
                                    .input(new MultiChoice(itemChoices))
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
                    remove.add(blastingRecipe.getKey());
                }
            } else if (recipe instanceof SmokingRecipe smokingRecipe) {
                if (smokingRecipe.getInputChoice() instanceof RecipeChoice.MaterialChoice materialChoice) {
                    final List<Choice> itemChoices = new ArrayList<>();
                    for (Material material : materialChoice.getChoices()) {
                        itemChoices.add(new ItemChoice(material.getKey()));
                    }
                    instance.internalRegister(
                            ArtisanFurnaceRecipe.builder()
                                    .key(smokingRecipe.getKey())
                                    .input(new MultiChoice(itemChoices))
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
                    remove.add(smokingRecipe.getKey());
                }
            }
        }
        remove.forEach(Bukkit::removeRecipe);
    }

    private final Map<NamespacedKey, ArtisanRecipe> recipeByKey;
    private final Multimap<NamespacedKey, ArtisanRecipe> recipeByType;

    @Override
    public void register(@NotNull ArtisanRecipe recipe) {
        if (RegisterManager.isOpen()) {
            recipeByKey.put(recipe.getKey(), recipe);
            recipeByType.put(recipe.getType(), recipe);
            NeoArtisan.logger().info("successfully register recipe " + recipe.getType().asString() + ": " + recipe.getKey().asString());
        } else {
            throw RegisterManager.REGISTRY_CLOSED;
        }
    }

    public void internalRegister(@NotNull ArtisanRecipe recipe) {
        if (RegisterManager.isOpen()) {
            recipeByKey.put(recipe.getKey(), recipe);
            recipeByType.put(recipe.getType(), recipe);
            if (NeoArtisan.isDebugMode()) {
                NeoArtisan.logger().info("successfully register recipe " + recipe.getType().asString() + ": " + recipe.getKey().asString());
            }
        } else {
            throw RegisterManager.REGISTRY_CLOSED;
        }
    }

    @Override
    public boolean hasRecipe(NamespacedKey key) {
        return recipeByKey.containsKey(key);
    }

    @Override
    public @NotNull ArtisanRecipe getRecipe(@NotNull NamespacedKey key) {
        return recipeByKey.get(key);
    }

    @Override
    @Unmodifiable
    @NotNull
    public Collection<ArtisanRecipe> getRecipes(@NotNull NamespacedKey recipeType) {
        if (!recipeByType.containsKey(recipeType)) return List.of();
        else return Collections.unmodifiableCollection(recipeByType.get(recipeType));
    }

    @TerminateMethod
    static void resetRecipe() {
        Bukkit.resetRecipes();
        NeoArtisan.logger().info("Successfully reset recipes.");
    }
}
