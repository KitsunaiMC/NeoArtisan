package io.github.moyusowo.neoartisan.recipe;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.ArrayKey;
import io.github.moyusowo.neoartisanapi.api.item.ItemRegistry;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanShapedRecipe;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

class ArtisanShapedRecipeImpl implements ArtisanShapedRecipe {
    private final Map<Character, NamespacedKey> toRegistryId;
    private final char[] recipe;
    private NamespacedKey result;
    private int count;
    private boolean built;

    static ArrayKey toRegistryKey(ItemStack[] matrix) {
        NamespacedKey[] matrixKeys = new NamespacedKey[9];
        for (int i = 0; i < 9; i++) {
            if (matrix[i] != null) {
                matrixKeys[i] = ItemRegistry.getItemRegistryManager().getRegistryId(matrix[i]);
            } else {
                matrixKeys[i] = RecipeRegistryImpl.EMPTY;
            }
        }
        return ArrayKey.from(matrixKeys);
    }

    public ArtisanShapedRecipeImpl(@NotNull String line1, @NotNull String line2, @NotNull String line3) {
        if (line1.isEmpty()) line1 = "   ";
        if (line2.isEmpty()) line2 = "   ";
        if (line3.isEmpty()) line3 = "   ";
        if (line1.length() != 3 || line2.length() != 3 || line3.length() != 3) {
            throw new IllegalArgumentException("You must input a String that length is 3!");
        }
        this.recipe = new char[]{
                line1.charAt(0), line1.charAt(1), line1.charAt(2),
                line2.charAt(0), line2.charAt(1), line2.charAt(2),
                line3.charAt(0), line3.charAt(1), line3.charAt(2)
        };
        toRegistryId = new HashMap<>();
        this.built = false;
    }

    @Override
    public void add(char c, @NotNull NamespacedKey registryId) {
        try {
            if (built) throw new IllegalAccessException("It's already registered!");
        } catch (IllegalAccessException e) {
            NeoArtisan.logger().severe(e.getLocalizedMessage());
        }
        toRegistryId.put(c, registryId);
    }

    @Override
    public void setResult(@NotNull NamespacedKey result, int count) {
        try {
            if (built) throw new IllegalAccessException("It's already registered!");
        } catch (IllegalAccessException e) {
            NeoArtisan.logger().severe(e.getLocalizedMessage());
        }
        this.result = result;
        this.count = count;
    }

    @Override
    public void build() {
        try {
            if (built) throw new IllegalAccessException("It's already registered!");
        } catch (IllegalAccessException e) {
            NeoArtisan.logger().severe(e.getLocalizedMessage());
        }
        NamespacedKey[] builtRecipe = new NamespacedKey[9];
        for (int i = 0; i < 9; i++) {
            if (recipe[i] != ' ') builtRecipe[i] = toRegistryId.get(recipe[i]);
            else builtRecipe[i] = RecipeRegistryImpl.EMPTY;
        }
        RecipeRegistryImpl.getInstance().register(ArrayKey.from(builtRecipe), this);
        this.built = true;
    }

    protected NamespacedKey getResult() {
        return result;
    }

    protected int getCount() {
        return count;
    }
}
