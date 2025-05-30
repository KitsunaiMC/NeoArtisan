package io.github.moyusowo.neoartisan.recipe;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.ArrayKey;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanShapedRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.RecipeRegistry;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

class ArtisanShapedRecipeImpl implements ArtisanShapedRecipe {
    private final NamespacedKey key;
    private final NamespacedKey[] matrix;
    private final ItemGenerator resultGenerator;

    @InitMethod(priority = InitPriority.REGISTRAR)
    public static void init() {
        Bukkit.getServicesManager().register(
                ArtisanShapedRecipe.Builder.class,
                new BuilderImpl(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private ArtisanShapedRecipeImpl(@NotNull NamespacedKey key, @NotNull NamespacedKey[] matrix, @NotNull ItemGenerator resultGenerator) {
        this.key = key;
        this.matrix = Arrays.copyOf(matrix, matrix.length);
        this.resultGenerator = resultGenerator;
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }

    @Override
    public @NotNull NamespacedKey[] getInputs() {
        return Arrays.copyOf(matrix, matrix.length);
    }

    @Override
    public @NotNull ItemGenerator getResultGenerator() {
        return resultGenerator;
    }

    public static final class BuilderImpl implements Builder {
        private NamespacedKey key;
        private final Map<Character, NamespacedKey> toRegistryId;
        private char[] recipe;
        private ItemGenerator resultGenerator;

        public BuilderImpl() {
            this.toRegistryId = new HashMap<>();
            this.key = null;
            this.recipe = null;
            this.resultGenerator = null;
        }

        @Override
        public @NotNull Builder key(NamespacedKey key) {
            this.key = key;
            return this;
        }

        @Override
        public @NotNull Builder set(@NotNull String line1, @NotNull String line2, @NotNull String line3) {
            if (line1.isEmpty()) line1 = "   ";
            if (line2.isEmpty()) line2 = "   ";
            if (line3.isEmpty()) line3 = "   ";
            if (line1.length() != 3 || line2.length() != 3 || line3.length() != 3) {
                throw new IllegalArgumentException("You must input a String that length is 3!");
            }
            this.recipe = new char[] {
                    line1.charAt(0), line1.charAt(1), line1.charAt(2),
                    line2.charAt(0), line2.charAt(1), line2.charAt(2),
                    line3.charAt(0), line3.charAt(1), line3.charAt(2)
            };
            return this;
        }

        @Override
        public @NotNull Builder set(@NotNull String line1, @NotNull String line2) {
            return set(line1, line2, "");
        }

        @Override
        public @NotNull Builder set(@NotNull String line1) {
            return set(line1, "", "");
        }

        @Override
        public @NotNull Builder add(char c, @NotNull NamespacedKey itemId) {
            toRegistryId.put(c, itemId);
            return this;
        }

        @Override
        public @NotNull Builder resultGenerator(ItemGenerator resultGenerator) {
            this.resultGenerator = resultGenerator;
            return this;
        }

        @Override
        public @NotNull ArtisanShapedRecipe build() {
            if (key == null || recipe == null || resultGenerator == null) throw new IllegalCallerException("You have to fill all the params before build!");
            NamespacedKey[] matrix = new NamespacedKey[9];
            for (int i = 0; i < 9; i++) {
                if (recipe[i] == ' ') matrix[i] = ArtisanItem.EMPTY;
                else matrix[i] = toRegistryId.get(recipe[i]);
            }
            return new ArtisanShapedRecipeImpl(key, matrix, resultGenerator);
        }
    }
}
