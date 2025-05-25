package io.github.moyusowo.neoartisan.test;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import org.bukkit.Material;

final class RecipeTest {

    @NeoArtisanAPI.Register
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
