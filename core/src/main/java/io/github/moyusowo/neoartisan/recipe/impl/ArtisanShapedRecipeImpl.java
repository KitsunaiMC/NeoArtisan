package io.github.moyusowo.neoartisan.recipe.impl;

import com.google.common.base.Preconditions;
import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanShapedRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.RecipeType;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.Choice;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

final class ArtisanShapedRecipeImpl implements ArtisanShapedRecipe {
    private final NamespacedKey key;
    private final List<Choice> inputs;
    private final ItemGenerator resultGenerator;
    private final Choice[][] inputMatrix;

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

    private ArtisanShapedRecipeImpl(@NotNull NamespacedKey key, @NotNull List<@NotNull Choice> inputs, @NotNull ItemGenerator resultGenerator) {
        this.key = key;
        Preconditions.checkArgument(inputs.size() == 9, "shaped inputs size must be 9.");
        this.inputs = new ArrayList<>(inputs);
        final Choice[][] matrix = new Choice[3][3];
        for (int i = 0; i < 9; i++) {
            matrix[i / 3][i % 3] = inputs.get(i);
        }
        int minRow = 3, maxRow = -1;
        int minCol = 3, maxCol = -1;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (matrix[r][c] != Choice.EMPTY) {
                    minRow = Math.min(minRow, r);
                    maxRow = Math.max(maxRow, r);
                    minCol = Math.min(minCol, c);
                    maxCol = Math.max(maxCol, c);
                }
            }
        }
        if (maxRow == -1) {
            throw new IllegalArgumentException("recipe can not be full of empty!");
        }
        int rows = maxRow - minRow + 1;
        int cols = maxCol - minCol + 1;
        this.inputMatrix = new Choice[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                this.inputMatrix[r][c] = matrix[minRow + r][minCol + c];
            }
        }
        this.resultGenerator = resultGenerator;
    }

    @Override
    public @NotNull NamespacedKey getKey() {
        return key;
    }

    @Override
    public @NotNull NamespacedKey getType() {
        return RecipeType.SHAPED;
    }

    @Override
    @Unmodifiable
    @NotNull
    public List<Choice> getInputs() {
        return Collections.unmodifiableList(inputs);
    }

    @Override
    @NotNull
    public Choice[][] getInputMatrix() {
        return this.inputMatrix.clone();
    }

    @Override
    @Unmodifiable
    @NotNull
    public List<ItemGenerator> getResultGenerators() {
        return List.of(resultGenerator);
    }

    @Override
    public boolean matches(ItemStack @NotNull [] originalMatrix) {
        if (originalMatrix.length != 9) return false;
        final ItemStack[][] firstMatrix = new ItemStack[3][3];
        for (int i = 0; i < 9; i++) {
            if (originalMatrix[i] != null && !originalMatrix[i].isEmpty()) {
                firstMatrix[i / 3][i % 3] = originalMatrix[i].clone();
            } else {
                firstMatrix[i / 3][i % 3] = ItemStack.empty();
            }
        }
        int minRow = 3, maxRow = -1;
        int minCol = 3, maxCol = -1;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (firstMatrix[r][c] != null && !firstMatrix[r][c].isEmpty()) {
                    minRow = Math.min(minRow, r);
                    maxRow = Math.max(maxRow, r);
                    minCol = Math.min(minCol, c);
                    maxCol = Math.max(maxCol, c);
                }
            }
        }
        if (maxRow == -1) return false;
        int rows = maxRow - minRow + 1;
        int cols = maxCol - minCol + 1;
        final ItemStack[][] matrix = new ItemStack[rows][cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                matrix[r][c] = firstMatrix[minRow + r][minCol + c];
            }
        }
        if (matrix.length != this.inputMatrix.length || matrix[0].length != this.inputMatrix[0].length) return false;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (!this.inputMatrix[row][col].matches(matrix[row][col])) return false;
            }
        }
        return true;
    }

    public static final class BuilderImpl implements Builder {
        private NamespacedKey key;
        private final Map<Character, Choice> toRegistryId;
        private char[] recipe;
        private ItemGenerator resultGenerator;

        public BuilderImpl() {
            this.toRegistryId = new HashMap<>();
            this.key = null;
            this.recipe = null;
            this.resultGenerator = null;
        }

        @Override
        public @NotNull Builder key(@NotNull NamespacedKey key) {
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
        public @NotNull Builder add(char c, @NotNull Choice choice) {
            toRegistryId.put(c, choice);
            return this;
        }

        @Override
        public @NotNull Builder resultGenerator(@NotNull ItemGenerator resultGenerator) {
            this.resultGenerator = resultGenerator;
            return this;
        }

        @Override
        public @NotNull ArtisanShapedRecipe build() {
            if (key == null || recipe == null || resultGenerator == null) throw new IllegalCallerException("You have to fill all the params before build!");
            final List<Choice> matrix = new ArrayList<>(9);
            for (char c : recipe) {
                if (c == ' ') matrix.add(Choice.EMPTY);
                else matrix.add(toRegistryId.get(c));
            }
            return new ArtisanShapedRecipeImpl(key, matrix, resultGenerator);
        }
    }
}
