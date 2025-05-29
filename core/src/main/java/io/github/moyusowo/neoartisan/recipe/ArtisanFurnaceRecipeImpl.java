package io.github.moyusowo.neoartisan.recipe;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanFurnaceRecipe;
import org.bukkit.NamespacedKey;

class ArtisanFurnaceRecipeImpl implements ArtisanFurnaceRecipe {
    private final NamespacedKey input;
    private final NamespacedKey result;
    private final int cookTime;
    private final int count;
    private final float exp;
    private boolean built;

    public ArtisanFurnaceRecipeImpl(NamespacedKey input, NamespacedKey result, int count, int cookTime, float exp) {
        this.input = input;
        this.result = result;
        this.count = count;
        this.cookTime = cookTime;
        this.exp = exp;
        this.built = false;
    }

    @Override
    public void build() {
        try {
            if (built) throw new IllegalAccessException("It's already registered!");
        } catch (IllegalAccessException e) {
            NeoArtisan.logger().severe(e.getLocalizedMessage());
        }
        RecipeRegistryImpl.getInstance().register(input, this);
        built = true;
    }

    protected NamespacedKey getResult() {
        return result;
    }

    protected int getCount() {
        return count;
    }

    protected int getCookTime() { return cookTime; }

    protected float getExp() { return exp; }
}
