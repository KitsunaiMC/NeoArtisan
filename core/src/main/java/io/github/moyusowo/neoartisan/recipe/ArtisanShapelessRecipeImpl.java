package io.github.moyusowo.neoartisan.recipe;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.ArrayKey;
import io.github.moyusowo.neoartisanapi.api.item.ItemRegistry;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanShapelessRecipe;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

class ArtisanShapelessRecipeImpl implements ArtisanShapelessRecipe {
    private final NamespacedKey[] recipe;
    private NamespacedKey result;
    private int count;
    private int i;
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
        Arrays.sort(matrixKeys);
        return ArrayKey.from(matrixKeys);
    }

    public ArtisanShapelessRecipeImpl() {
        this.recipe = new NamespacedKey[9];
        this.i = 0;
        this.built = false;
        Arrays.fill(recipe, RecipeRegistryImpl.EMPTY);
    }

    public ArtisanShapelessRecipeImpl(NamespacedKey result, int count) {
        this();
        this.result = result;
        this.count = count;
    }

    @Override
    public void add(NamespacedKey registryId) {
        try {
            if (built) throw new IllegalAccessException("It's already registered!");
        } catch (IllegalAccessException e) {
            NeoArtisan.logger().severe(e.getLocalizedMessage());
        }
        if (i == recipe.length) throw new ArrayIndexOutOfBoundsException("You can no longer add!");
        recipe[i++] = registryId;
    }

    @Override
    @SuppressWarnings("unused")
    public void add(NamespacedKey... registryIds) {
        try {
            if (built) throw new IllegalAccessException("It's already registered!");
        } catch (IllegalAccessException e) {
            NeoArtisan.logger().severe(e.getLocalizedMessage());
        }
        if (registryIds.length + i > recipe.length) throw new ArrayIndexOutOfBoundsException("You can no long add!");
        for (NamespacedKey registryId : registryIds) {
            recipe[i++] = registryId;
        }
    }

    @Override
    @SuppressWarnings("unused")
    public void setResult(NamespacedKey registryId, int count) {
        try {
            if (built) throw new IllegalAccessException("It's already registered!");
        } catch (IllegalAccessException e) {
            NeoArtisan.logger().severe(e.getLocalizedMessage());
        }
        result = registryId;
        this.count = count;
    }

    @Override
    public void build() {
        try {
            if (built) throw new IllegalAccessException("It's already registered!");
        } catch (IllegalAccessException e) {
            NeoArtisan.logger().severe(e.getLocalizedMessage());
        }
        NamespacedKey[] builtRecipe = Arrays.copyOf(recipe, recipe.length);
        Arrays.sort(builtRecipe);
        RecipeRegistryImpl.getInstance().register(ArrayKey.from(builtRecipe), this);
        built = true;
    }

    protected NamespacedKey getResult() {
        return result;
    }

    protected int getCount() {
        return count;
    }
}
