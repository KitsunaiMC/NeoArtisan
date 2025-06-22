package io.github.moyusowo.neoartisan.test;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCrop;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCropState;
import io.github.moyusowo.neoartisanapi.api.block.crop.TripwireAppearance;
import io.github.moyusowo.neoartisanapi.api.block.full.ArtisanFullBlock;
import io.github.moyusowo.neoartisanapi.api.block.full.ArtisanFullBlockState;
import io.github.moyusowo.neoartisanapi.api.block.full.FullBlockAppearance;
import io.github.moyusowo.neoartisanapi.api.block.thin.ArtisanThinBlock;
import io.github.moyusowo.neoartisanapi.api.block.thin.ArtisanThinBlockState;
import io.github.moyusowo.neoartisanapi.api.block.thin.ThinBlockAppearance;
import io.github.moyusowo.neoartisanapi.api.block.transparent.TransparentAppearance;
import io.github.moyusowo.neoartisanapi.api.block.transparent.ArtisanTransparentBlock;
import io.github.moyusowo.neoartisanapi.api.block.transparent.ArtisanTransparentBlockState;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.List;

import static io.github.moyusowo.neoartisan.test.ItemTest.namespace;

final class BlockTest {

    static final NamespacedKey magic_crop = new NamespacedKey(namespace, "magic_crop");

    @NeoArtisanAPI.Register
    private static void register() {
        if (NeoArtisan.isDebugMode()) {
            NeoArtisanAPI.getBlockRegistry().register(
                    ArtisanCrop.factory().builder()
                            .blockId(magic_crop)
                            .stages(
                                    List.of(
                                            ArtisanCropState.factory().builder()
                                                    .appearance(
                                                            new TripwireAppearance(
                                                                    false,
                                                                    false,
                                                                    false,
                                                                    false,
                                                                    false,
                                                                    false,
                                                                    false
                                                            )
                                                    )
                                                    .generators(
                                                            new ItemGenerator[]{
                                                                    ItemGenerator.simpleGenerator(
                                                                            ItemTest.broken_stick,
                                                                            1
                                                                    ),
                                                                    ItemGenerator.rangedGenerator(
                                                                            Material.WHEAT.getKey(),
                                                                            1,
                                                                            3
                                                                    )
                                                            }
                                                    )
                                                    .build(),
                                            ArtisanCropState.factory().builder()
                                                    .appearance(
                                                            new TripwireAppearance(
                                                                    true,
                                                                    false,
                                                                    false,
                                                                    false,
                                                                    false,
                                                                    false,
                                                                    false
                                                            )
                                                    )
                                                    .generators(
                                                            new ItemGenerator[0]
                                                    )
                                                    .build(),
                                            ArtisanCropState.factory().builder()
                                                    .appearance(
                                                            new TripwireAppearance(
                                                                    false,
                                                                    true,
                                                                    false,
                                                                    false,
                                                                    false,
                                                                    false,
                                                                    false
                                                            )
                                                    )
                                                    .generators(
                                                            new ItemGenerator[]{
                                                                    ItemGenerator.simpleGenerator(
                                                                            Material.IRON_SWORD.getKey(),
                                                                            1
                                                                    ),
                                                                    ItemGenerator.rangedGenerator(
                                                                            ItemTest.magic_diamond,
                                                                            1,
                                                                            5
                                                                    )
                                                            }
                                                    )
                                                    .build()
                                    )
                            )
                            .boneMealMinGrowth(0)
                            .boneMealMaxGrowth(2)
                            .build()
            );
            NeoArtisanAPI.getBlockRegistry().register(
                    ArtisanTransparentBlock.factory().builder()
                            .blockId(ItemTest.cooking_pot)
                            .canBurn(false)
                            .states(
                                    List.of(
                                            ArtisanTransparentBlockState.factory().builder()
                                                    .appearanceState(
                                                            new TransparentAppearance(
                                                                    TransparentAppearance.LeavesAppearance.OAK_LEAVES,
                                                                    1,
                                                                    false,
                                                                    false
                                                            )
                                                    )
                                                    .generators(
                                                            new ItemGenerator[]{
                                                                    ItemGenerator.simpleGenerator(
                                                                            ItemTest.cooking_pot,
                                                                            1
                                                                    )
                                                            }
                                                    )
                                                    .build()
                                    )
                            )
                            .build()
            );
            NeoArtisanAPI.getBlockRegistry().register(
                    ArtisanThinBlock.factory().builder()
                            .blockId(ItemTest.cutting_board)
                            .states(
                                    List.of(
                                            ArtisanThinBlockState.factory().builder()
                                                    .appearanceState(
                                                            new ThinBlockAppearance(
                                                                    ThinBlockAppearance.PressurePlateAppearance.LIGHT_WEIGHTED_PRESSURE_PLATE,
                                                                    2
                                                            )
                                                    )
                                                    .generators(
                                                            new ItemGenerator[]{
                                                                    ItemGenerator.simpleGenerator(
                                                                            ItemTest.cutting_board,
                                                                            1
                                                                    )
                                                            }
                                                    )
                                                    .build()
                                    )
                            )
                            .build()
            );
            NeoArtisanAPI.getBlockRegistry().register(
                    ArtisanFullBlock.factory().builder()
                            .blockId(ItemTest.magic_block)
                            .states(
                                    List.of(
                                            ArtisanFullBlockState.factory().builder()
                                                    .appearanceState(new FullBlockAppearance(FullBlockAppearance.NoteBlockAppearance.SKELETON, 1, true))
                                                    .generators(
                                                            new ItemGenerator[] {
                                                                    ItemGenerator.simpleGenerator(ItemTest.magic_block, 1)
                                                            }
                                                    )
                                                    .build()
                                    )
                            )
                            .build()
            );
        }
    }
}
