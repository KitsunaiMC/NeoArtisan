package io.github.moyusowo.neoartisan.recipe.behavior;

import io.github.moyusowo.neoartisan.util.ArrayKey;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import io.github.moyusowo.neoartisanapi.api.recipe.RecipeType;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

final class ArrayKeyUtil {
    public static ArrayKey toShapedKey(ItemStack[] matrix) {
        NamespacedKey[] matrixKeys = new NamespacedKey[9];
        for (int i = 0; i < 9; i++) {
            if (matrix[i] != null) {
                matrixKeys[i] = NeoArtisanAPI.getItemRegistry().getRegistryId(matrix[i]);
            } else {
                matrixKeys[i] = ArtisanItem.EMPTY;
            }
        }
        return ArrayKey.from(matrixKeys, RecipeType.SHAPED);
    }

    public static ArrayKey toShapelessKey(ItemStack[] matrix) {
        NamespacedKey[] matrixKeys = new NamespacedKey[9];
        for (int i = 0; i < 9; i++) {
            if (matrix[i] != null) {
                matrixKeys[i] = NeoArtisanAPI.getItemRegistry().getRegistryId(matrix[i]);
            } else {
                matrixKeys[i] = ArtisanItem.EMPTY;
            }
        }
        Arrays.sort(matrixKeys);
        return ArrayKey.from(matrixKeys, RecipeType.SHAPELESS);
    }

    public static ArrayKey toFurnaceKey(ItemStack itemStack) {
        NamespacedKey key = NeoArtisanAPI.getItemRegistry().getRegistryId(itemStack);
        return ArrayKey.from(new NamespacedKey[] {key}, RecipeType.FURNACE);
    }

    public static ArrayKey toCampfireKey(ItemStack itemStack) {
        NamespacedKey key = NeoArtisanAPI.getItemRegistry().getRegistryId(itemStack);
        return ArrayKey.from(new NamespacedKey[] {key}, RecipeType.CAMPFIRE);
    }

    public static ArrayKey toSmokerKey(ItemStack itemStack) {
        NamespacedKey key = NeoArtisanAPI.getItemRegistry().getRegistryId(itemStack);
        return ArrayKey.from(new NamespacedKey[] {key}, RecipeType.SMOKING);
    }

    public static ArrayKey toBlastFurnaceKey(ItemStack itemStack) {
        NamespacedKey key = NeoArtisanAPI.getItemRegistry().getRegistryId(itemStack);
        return ArrayKey.from(new NamespacedKey[] {key}, RecipeType.BLASTING);
    }
}
