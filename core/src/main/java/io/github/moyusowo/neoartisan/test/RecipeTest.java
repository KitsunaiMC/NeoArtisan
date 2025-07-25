package io.github.moyusowo.neoartisan.test;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanCampfireRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanFurnaceRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanShapedRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanShapelessRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.ItemChoice;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.TagChoice;
import io.github.moyusowo.neoartisanapi.api.registry.Registries;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

public final class RecipeTest {

    public static void register() {
        if (NeoArtisan.isDebugMode()) {
            Registries.RECIPE.register(
                    ArtisanShapedRecipe.builder()
                            .key(new NamespacedKey(NeoArtisan.instance(), "aaaa"))
                            .set("A B")
                            .add('A', new TagChoice("item/magic"))
                            .add('B', new ItemChoice(ItemTest.magic_sword))
                            .resultGenerator(
                                    ItemGenerator.simpleGenerator(
                                            Material.APPLE.getKey(),
                                            64
                                    )
                            )
                            .build()

            );
            Registries.RECIPE.register(
                    ArtisanShapelessRecipe.builder()
                            .key(new NamespacedKey(NeoArtisan.instance(), "bbbb"))
                            .add(
                                    new ItemChoice(ItemTest.magic_diamond),
                                    new TagChoice("item/magic"),
                                    new ItemChoice(Material.IRON_INGOT.getKey())
                            )
                            .resultGenerator(
                                    ItemGenerator.simpleGenerator(
                                            Material.DIAMOND.getKey(), 32
                                    )
                            )
                            .build()
            );
            Registries.RECIPE.register(
                    ArtisanFurnaceRecipe.builder()
                            .key(new NamespacedKey(NeoArtisan.instance(), "diamondF"))
                            .input(new ItemChoice(ItemTest.magic_diamond))
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
            Registries.RECIPE.register(
                    ArtisanCampfireRecipe.builder()
                            .key(new NamespacedKey(NeoArtisan.instance(), "diamondC"))
                            .input(new ItemChoice(ItemTest.magic_diamond))
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
