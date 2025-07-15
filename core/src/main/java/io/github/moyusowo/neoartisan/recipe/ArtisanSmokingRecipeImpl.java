package io.github.moyusowo.neoartisan.recipe;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanSmokingRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.RecipeType;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

final class ArtisanSmokingRecipeImpl implements ArtisanSmokingRecipe {
    private final NamespacedKey key;
    private final NamespacedKey input;
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

    private ArtisanSmokingRecipeImpl(NamespacedKey key, NamespacedKey input, ItemGenerator resultGenerator, int cookTime, float exp) {
        this.key = key;
        this.input = input;
        this.resultGenerator = resultGenerator;
        this.cookTime = cookTime;
        this.exp = exp;
    }

    @Override
    public @NotNull NamespacedKey getInput() {
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
    public @NotNull RecipeType getType() {
        return RecipeType.SMOKING;
    }

    @Override
    public @NotNull NamespacedKey[] getInputs() {
        return new NamespacedKey[] { input };
    }

    @Override
    public @NotNull ItemGenerator getResultGenerator() {
        return resultGenerator;
    }

    public static final class BuilderImpl implements Builder {
        private NamespacedKey key;
        private NamespacedKey input;
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
        public @NotNull Builder key(NamespacedKey key) {
            this.key = key;
            return this;
        }

        @Override
        public @NotNull Builder inputItemId(NamespacedKey inputItemId) {
            this.input = inputItemId;
            return this;
        }

        @Override
        public @NotNull Builder resultGenerator(ItemGenerator resultGenerator) {
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
        public @NotNull ArtisanSmokingRecipe build() {
            if (key == null || input == null || cookTime == null || resultGenerator == null || exp == null) throw new IllegalCallerException("You have to fill all the params before build!");
            return new ArtisanSmokingRecipeImpl(key, input, resultGenerator, cookTime, exp);
        }
    }
}
