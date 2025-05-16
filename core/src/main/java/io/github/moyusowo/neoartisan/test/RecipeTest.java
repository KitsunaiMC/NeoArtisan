package io.github.moyusowo.neoartisan.test;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.item.ArmorProperty;
import io.github.moyusowo.neoartisanapi.api.item.FoodProperty;
import io.github.moyusowo.neoartisanapi.api.item.ItemRegistry;
import io.github.moyusowo.neoartisanapi.api.item.WeaponProperty;
import io.github.moyusowo.neoartisanapi.api.recipe.RecipeRegistry;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;

import java.util.List;

final class RecipeTest {

    @InitMethod(order = InitPriority.LOW)
    private static void register() {
        if (NeoArtisan.isDebugMode()) {
            NeoArtisanAPI.getRecipeRegistry().createShapedRecipe("A B", "", "")
                    .add('A', ItemTest.magic_bread)
                    .add('B', ItemTest.magic_sword)
                    .setResult(Material.APPLE.getKey(), 64)
                    .build();
            NeoArtisanAPI.getRecipeRegistry().createShapelessRecipe()
                    .add(
                            ItemTest.magic_diamond,
                            ItemTest.magic_bread,
                            Material.IRON_INGOT.getKey()
                    )
                    .setResult(Material.DIAMOND.getKey(), 32)
                    .build();
        }
    }
}
