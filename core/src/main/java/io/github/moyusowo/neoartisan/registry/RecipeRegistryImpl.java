package io.github.moyusowo.neoartisan.registry;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.recipe.guide.generator.FurnaceLikeGuide;
import io.github.moyusowo.neoartisan.recipe.guide.generator.ShapedGuide;
import io.github.moyusowo.neoartisan.recipe.guide.generator.ShapelessGuide;
import io.github.moyusowo.neoartisan.registry.internal.RecipeRegistryInternal;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisan.util.terminate.TerminateMethod;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.recipe.*;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.Choice;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.ItemChoice;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.MultiChoice;
import io.github.moyusowo.neoartisanapi.api.recipe.guide.GuideGUIGenerator;
import io.github.moyusowo.neoartisanapi.api.recipe.guide.ItemCategories;
import io.github.moyusowo.neoartisanapi.api.registry.RecipeRegistry;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.*;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.function.Supplier;

final class RecipeRegistryImpl implements Listener, RecipeRegistryInternal {

    private static RecipeRegistryImpl instance;

    public static RecipeRegistryImpl getInstance() {
        return instance;
    }

    @InitMethod(priority = InitPriority.REGISTRY_LOAD)
    static void init() {
        new RecipeRegistryImpl();
    }

    @SuppressWarnings("UnstableApiUsage")
    private RecipeRegistryImpl() {
        instance = this;
        recipeByType = ArrayListMultimap.create();
        recipeByKey = new HashMap<>();
        generators = new HashMap<>();
        generators.put(RecipeType.SHAPED, new ShapedGuide());
        generators.put(RecipeType.SHAPELESS, new ShapelessGuide());
        generators.put(RecipeType.SMOKING, new FurnaceLikeGuide());
        generators.put(RecipeType.BLASTING, new FurnaceLikeGuide());
        generators.put(RecipeType.FURNACE, new FurnaceLikeGuide());
        generators.put(RecipeType.CAMPFIRE, new FurnaceLikeGuide());
        categorys = new HashMap<>();
        setCategory(ItemCategories.ORIGINAL, () -> {
            ItemStack itemStack = ItemStack.of(Material.GRASS_BLOCK);
            itemStack.setData(DataComponentTypes.ITEM_NAME, MiniMessage.miniMessage().deserialize("原版物品"));
            return itemStack;
        });
        setCategory(ItemCategories.COMBAT, () -> {
            ItemStack itemStack = ItemStack.of(Material.DIAMOND_SWORD);
            itemStack.setData(DataComponentTypes.ITEM_NAME, MiniMessage.miniMessage().deserialize("武器"));
            return itemStack;
        });
        setCategory(ItemCategories.DECORATIONS, () -> {
            ItemStack itemStack = ItemStack.of(Material.FLOWER_POT);
            itemStack.setData(DataComponentTypes.ITEM_NAME, MiniMessage.miniMessage().deserialize("装饰"));
            return itemStack;
        });
        setCategory(ItemCategories.FOOD, () -> {
            ItemStack itemStack = ItemStack.of(Material.BREAD);
            itemStack.setData(DataComponentTypes.ITEM_NAME, MiniMessage.miniMessage().deserialize("食物"));
            return itemStack;
        });
        setCategory(ItemCategories.MISC, () -> {
            ItemStack itemStack = ItemStack.of(Material.CLAY_BALL);
            itemStack.setData(DataComponentTypes.ITEM_NAME, MiniMessage.miniMessage().deserialize("杂项"));
            return itemStack;
        });
        setCategory(ItemCategories.TOOLS, () -> {
            ItemStack itemStack = ItemStack.of(Material.GOLDEN_PICKAXE);
            itemStack.setData(DataComponentTypes.ITEM_NAME, MiniMessage.miniMessage().deserialize("工具"));
            return itemStack;
        });
        NeoArtisan.registerListener(instance);
        Bukkit.getServicesManager().register(
                RecipeRegistry.class,
                RecipeRegistryImpl.getInstance(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private final Map<NamespacedKey, ArtisanRecipe> recipeByKey;
    private final Multimap<NamespacedKey, ArtisanRecipe> recipeByType;
    private final Map<NamespacedKey, GuideGUIGenerator> generators;
    private final Map<NamespacedKey, ItemStack> categorys;

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
    public Collection<ArtisanRecipe> getRecipesByType(@NotNull NamespacedKey recipeType) {
        if (!recipeByType.containsKey(recipeType)) return List.of();
        else return Collections.unmodifiableCollection(recipeByType.get(recipeType));
    }

    @Override
    @Unmodifiable
    @NotNull
    public Collection<ArtisanRecipe> getAllRecipes() {
        return Collections.unmodifiableCollection(
                recipeByKey.values()
        );
    }

    @Override
    public void setGuide(@NotNull NamespacedKey recipeType, @NotNull GuideGUIGenerator generator) {
        if (!generators.containsKey(recipeType)) generators.put(recipeType, generator);
        else generators.replace(recipeType, generator);
    }

    @Override
    public void setCategory(@NotNull NamespacedKey category, @NotNull Supplier<ItemStack> itemStackSupplier) {
        if (!categorys.containsKey(category)) categorys.put(category, itemStackSupplier.get());
        else categorys.replace(category, itemStackSupplier.get());
    }

    @TerminateMethod
    static void resetRecipe() {
        Bukkit.resetRecipes();
        NeoArtisan.logger().info("Successfully reset recipes.");
    }

    @Override
    public @NotNull Optional<GuideGUIGenerator> getGuide(@NotNull NamespacedKey recipeType) {
        if (generators.containsKey(recipeType)) return Optional.of(generators.get(recipeType));
        return Optional.empty();
    }

    @Override
    public @NotNull Optional<ItemStack> getCategory(@NotNull NamespacedKey category) {
        if (categorys.containsKey(category)) return Optional.of(categorys.get(category));
        return Optional.empty();
    }

    @Override
    public @Unmodifiable @NotNull Multimap<NamespacedKey, ArtisanRecipe> getAllRecipesByType() {
        return Multimaps.unmodifiableMultimap(recipeByType);
    }
}
