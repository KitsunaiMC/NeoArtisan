package io.github.moyusowo.neoartisan.recipe.impl;

import com.google.common.base.Preconditions;
import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanShapelessRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.RecipeType;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.Choice;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

final class ArtisanShapelessRecipeImpl implements ArtisanShapelessRecipe {
    private final NamespacedKey key;
    private final List<Choice> recipe;
    private final ItemGenerator resultGenerator;

    @InitMethod(priority = InitPriority.REGISTRAR)
    public static void init() {
        Bukkit.getServicesManager().register(
                BuilderFactory.class,
                new BuilderFactory() {
                    @Override
                    public @NotNull Builder builder() {
                        return new BuilderImpl();
                    }
                },
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private ArtisanShapelessRecipeImpl(NamespacedKey key, List<Choice> recipe, ItemGenerator resultGenerator) {
        this.key = key;
        Preconditions.checkArgument(!recipe.isEmpty() && recipe.size() < 9);
        this.recipe = new ArrayList<>(recipe);
        this.recipe.removeIf(
                choice -> choice == null || choice == Choice.EMPTY
        );
        this.resultGenerator = resultGenerator;
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }

    @Override
    public @NotNull NamespacedKey getType() {
        return RecipeType.SHAPELESS;
    }

    @Override
    @Unmodifiable
    @NotNull
    public List<Choice> getInputs() {
        return Collections.unmodifiableList(recipe);
    }

    @Override
    @Unmodifiable
    @NotNull
    public List<ItemGenerator> getResultGenerators() {
        return List.of(resultGenerator);
    }

    @Override
    public boolean matches(ItemStack @NotNull [] matrix) {
        final List<ItemStack> items = new ArrayList<>(Arrays.stream(matrix).filter(itemStack -> itemStack != null && !itemStack.isEmpty()).toList());
        if (items.size() != recipe.size()) return false;
        final int size = items.size();
        final List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            graph.add(new ArrayList<>());
        }
        for (int itemId = 0; itemId < size; itemId++) {
            final ItemStack itemStack = items.get(itemId);
            for (int slotId = 0; slotId < size; slotId++) {
                final Choice choice = recipe.get(slotId);
                if (choice.matches(itemStack)) {
                    graph.get(itemId).add(slotId);
                }
            }
        }
        final int[] slotMatches = new int[size];
        Arrays.fill(slotMatches, -1);
        final int[] itemMatches = new int[size];
        Arrays.fill(itemMatches, -1);
        boolean[] visited;
        for (int slotId = 0; slotId < size; slotId++) {
            visited = new boolean[size];
            if (!findMatchForSlot(slotId, graph, slotMatches, itemMatches, visited)) {
                return false;
            }
        }
        return true;
    }

    private static boolean findMatchForSlot(int slotId, final List<List<Integer>> graph, final int[] slotMatches, final int[] itemMatches, final boolean[] visited) {
        for (int itemId = 0; itemId < graph.size(); itemId++) {
            if (graph.get(itemId).contains(slotId) && !visited[itemId]) {
                visited[itemId] = true;
                if (itemMatches[itemId] == -1 || findMatchForSlot(itemMatches[itemId], graph, slotMatches, itemMatches, visited)) {
                    slotMatches[slotId] = itemId;
                    itemMatches[itemId] = slotId;
                    return true;
                }
            }
        }
        return false;
    }

    public static final class BuilderImpl implements Builder {
        private NamespacedKey key;
        private final List<Choice> recipe;
        private ItemGenerator resultGenerator;

        public BuilderImpl() {
            this.recipe = new ArrayList<>();
            this.key = null;
            this.resultGenerator = null;
        }

        @Override
        public @NotNull Builder key(@NotNull NamespacedKey key) {
            this.key = key;
            return this;
        }

        @Override
        public @NotNull Builder add(@NotNull Choice choice) {
            if (recipe.size() + 1 > 9) {
                throw new ArrayIndexOutOfBoundsException("You can no long add!");
            } else {
                recipe.add(choice);
            }
            return this;
        }

        @Override
        public @NotNull Builder add(@NotNull Choice... choices) {
            if (this.recipe.size() + choices.length > 9) {
                throw new ArrayIndexOutOfBoundsException("You can no longer add!");
            } else {
                this.recipe.addAll(Arrays.stream(choices).toList());
            }
            return this;
        }

        @Override
        public @NotNull Builder add(@NotNull Choice choice, int count) {
            if (this.recipe.size() + count > 9) {
                throw new ArrayIndexOutOfBoundsException("You can no longer add!");
            } else {
                for (int i = 0; i < count; i++) {
                    this.recipe.add(choice);
                }
            }
            return this;
        }

        @Override
        public @NotNull Builder resultGenerator(@NotNull ItemGenerator resultGenerator) {
            this.resultGenerator = resultGenerator;
            return this;
        }

        @Override
        public @NotNull ArtisanShapelessRecipe build() {
            if (key == null || resultGenerator == null) throw new IllegalCallerException("You have to fill all the params before build!");
            return new ArtisanShapelessRecipeImpl(key, recipe, resultGenerator);
        }
    }
}
