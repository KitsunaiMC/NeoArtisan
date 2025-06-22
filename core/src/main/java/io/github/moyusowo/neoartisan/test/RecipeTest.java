package io.github.moyusowo.neoartisan.test;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanFurnaceRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanShapedRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanShapelessRecipe;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

final class RecipeTest {

    @NeoArtisanAPI.Register
    private static void register() {
        if (NeoArtisan.isDebugMode()) {
            NeoArtisanAPI.getRecipeRegistry().register(
                    ArtisanShapedRecipe.factory().builder()
                            .key(new NamespacedKey(NeoArtisan.instance(), "aaaa"))
                            .set("A B")
                            .add('A', ItemTest.magic_bread)
                            .add('B', ItemTest.magic_sword)
                            .resultGenerator(
                                    ItemGenerator.simpleGenerator(
                                            Material.APPLE.getKey(),
                                            64
                                    )
                            )
                            .build()

            );
            NeoArtisanAPI.getRecipeRegistry().register(
                    ArtisanShapelessRecipe.factory().builder()
                            .key(new NamespacedKey(NeoArtisan.instance(), "bbbb"))
                            .add(
                                    ItemTest.magic_diamond,
                                    ItemTest.magic_bread,
                                    Material.IRON_INGOT.getKey()
                            )
                            .resultGenerator(
                                    ItemGenerator.simpleGenerator(
                                            Material.DIAMOND.getKey(), 32
                                    )
                            )
                            .build()
            );
            NeoArtisanAPI.getRecipeRegistry().register(
                    ArtisanFurnaceRecipe.factory().builder()
                            .key(new NamespacedKey(NeoArtisan.instance(), "diamond"))
                            .inputItemId(ItemTest.magic_diamond)
                            .resultGenerator(
                                    ItemGenerator.simpleGenerator(
                                            Material.IRON_INGOT.getKey(),
                                            24
                                    )
                            )
                            .exp(5.0f)
                            .cookTime(100)
                            .build()
            );
        }
    }
}
