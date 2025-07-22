package io.github.moyusowo.neoartisan.recipe.impl;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanShapelessRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.RecipeType;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class ArtisanShapelessRecipeImpl implements ArtisanShapelessRecipe {
    private final NamespacedKey key;
    private final NamespacedKey[] recipe;
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

    private ArtisanShapelessRecipeImpl(NamespacedKey key, List<NamespacedKey> recipe, ItemGenerator resultGenerator) {
        this.key = key;
        this.recipe = new NamespacedKey[9];
        for (int i = 0; i < 9; i++) {
            if (i < recipe.size()) {
                this.recipe[i] = recipe.get(i);
            } else {
                this.recipe[i] = ArtisanItem.EMPTY;
            }
        }
        Arrays.sort(this.recipe);
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
    public @NotNull NamespacedKey[] getInputs() {
        return Arrays.copyOf(recipe, recipe.length);
    }

    @Override
    public @NotNull ItemGenerator[] getResultGenerator() {
        return new ItemGenerator[]{ resultGenerator };
    }

    public static final class BuilderImpl implements Builder {
        private NamespacedKey key;
        private final List<NamespacedKey> itemIds;
        private ItemGenerator resultGenerator;

        public BuilderImpl() {
            this.itemIds = new ArrayList<>();
            this.key = null;
            this.resultGenerator = null;
        }

        @Override
        public @NotNull Builder key(NamespacedKey key) {
            this.key = key;
            return this;
        }

        @Override
        public @NotNull Builder add(@NotNull NamespacedKey itemId) {
            if (itemIds.size() + 1 > 9) {
                throw new ArrayIndexOutOfBoundsException("You can no long add!");
            } else {
                itemIds.add(itemId);
            }
            return this;
        }

        @Override
        public @NotNull Builder add(@NotNull NamespacedKey... itemIds) {
            if (this.itemIds.size() + itemIds.length > 9) {
                throw new ArrayIndexOutOfBoundsException("You can no long add!");
            } else {
                this.itemIds.addAll(Arrays.stream(itemIds).toList());
            }
            return this;
        }

        @Override
        public @NotNull Builder add(@NotNull NamespacedKey itemId, int count) {
            if (this.itemIds.size() + count > 9) {
                throw new ArrayIndexOutOfBoundsException("You can no long add!");
            } else {
                for (int i = 0; i < count; i++) {
                    this.itemIds.add(itemId);
                }
            }
            return this;
        }

        @Override
        public @NotNull Builder resultGenerator(ItemGenerator resultGenerator) {
            this.resultGenerator = resultGenerator;
            return this;
        }

        @Override
        public @NotNull ArtisanShapelessRecipe build() {
            if (key == null || resultGenerator == null) throw new IllegalCallerException("You have to fill all the params before build!");
            return new ArtisanShapelessRecipeImpl(key, itemIds, resultGenerator);
        }
    }
}
