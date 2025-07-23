package io.github.moyusowo.neoartisan.recipe.impl;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanBlastingRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.RecipeType;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.Choice;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

public class ArtisanBlastingRecipeImpl implements ArtisanBlastingRecipe {
    private final NamespacedKey key;
    private final Choice input;
    private final int cookTime;
    private final float exp;
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

    private ArtisanBlastingRecipeImpl(NamespacedKey key, Choice input, ItemGenerator resultGenerator, int cookTime, float exp) {
        this.key = key;
        this.input = input;
        this.resultGenerator = resultGenerator;
        this.cookTime = cookTime;
        this.exp = exp;
    }

    @Override
    public @NotNull Choice getInput() {
        return input;
    }

    @Override
    public int getCookTime() { return cookTime; }

    @Override
    public float getExp() { return exp; }

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }

    @Override
    public @NotNull NamespacedKey getType() {
        return RecipeType.BLASTING;
    }

    @Override
    @Unmodifiable
    @NotNull
    public List<Choice> getInputs() {
        return List.of(input);
    }

    @Override
    @Unmodifiable
    @NotNull
    public List<ItemGenerator> getResultGenerators() {
        return List.of(resultGenerator);
    }

    @Override
    public boolean matches(ItemStack @NotNull [] matrix) {
        if (matrix.length != 1) return false;
        return input.matches(matrix[0]);
    }

    public static final class BuilderImpl implements Builder {
        private NamespacedKey key;
        private Choice input;
        private Integer cookTime;
        private Float exp;
        private ItemGenerator resultGenerator;

        public BuilderImpl() {
            key = null;
            input = null;
            cookTime = null;
            exp = null;
            resultGenerator = null;
        }

        @Override
        public @NotNull Builder key(@NotNull NamespacedKey key) {
            this.key = key;
            return this;
        }

        @Override
        public @NotNull Builder input(@NotNull Choice choice) {
            this.input = choice;
            return this;
        }

        @Override
        public @NotNull Builder resultGenerator(@NotNull ItemGenerator resultGenerator) {
            this.resultGenerator = resultGenerator;
            return this;
        }

        @Override
        public @NotNull Builder cookTime(int cookTime) {
            this.cookTime = cookTime;
            return this;
        }

        @Override
        public @NotNull Builder exp(float exp) {
            this.exp = exp;
            return this;
        }

        @Override
        public @NotNull ArtisanBlastingRecipe build() {
            if (key == null || input == null || cookTime == null || resultGenerator == null || exp == null) throw new IllegalCallerException("You have to fill all the params before build!");
            return new ArtisanBlastingRecipeImpl(key, input, resultGenerator, cookTime, exp);
        }
    }
}
