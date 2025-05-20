package io.github.moyusowo.neoartisan.test;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockState;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCrop;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCropState;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.List;

import static io.github.moyusowo.neoartisan.test.ItemTest.namespace;

final class CropTest {

    static final NamespacedKey magic_crop = new NamespacedKey(namespace, "magic_crop");

    @InitMethod(order = InitPriority.LOW)
    private static void register() {
        if (NeoArtisan.isDebugMode()) {
            NeoArtisanAPI.getBlockRegistry().register(
                    ArtisanCrop.builder()
                            .blockId(magic_crop)
                            .defaultState(0)
                            .stages(
                                    List.of(
                                            ArtisanCropState.builder()
                                                    .actualState(Block.getId(Blocks.WHEAT.defaultBlockState().setValue(BlockStateProperties.AGE_7, 1)))
                                                    .appearanceState(
                                                            Block.getId(
                                                                    Blocks.TRIPWIRE.defaultBlockState()
                                                                            .setValue(BlockStateProperties.ATTACHED, false)
                                                                            .setValue(BlockStateProperties.DISARMED, false)
                                                                            .setValue(BlockStateProperties.EAST, false)
                                                                            .setValue(BlockStateProperties.NORTH, false)
                                                                            .setValue(BlockStateProperties.SOUTH, false)
                                                                            .setValue(BlockStateProperties.WEST, false)
                                                                            .setValue(BlockStateProperties.POWERED, false)
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
                                            ArtisanCropState.builder()
                                                    .actualState(Block.getId(Blocks.WHEAT.defaultBlockState().setValue(BlockStateProperties.AGE_7, 1)))
                                                    .appearanceState(
                                                            Block.getId(
                                                                    Blocks.TRIPWIRE.defaultBlockState()
                                                                            .setValue(BlockStateProperties.ATTACHED, true)
                                                                            .setValue(BlockStateProperties.DISARMED, false)
                                                                            .setValue(BlockStateProperties.EAST, false)
                                                                            .setValue(BlockStateProperties.NORTH, false)
                                                                            .setValue(BlockStateProperties.SOUTH, false)
                                                                            .setValue(BlockStateProperties.WEST, false)
                                                                            .setValue(BlockStateProperties.POWERED, false)
                                                            )
                                                    )
                                                    .generators(
                                                            new ItemGenerator[0]
                                                    )
                                                    .build(),
                                            ArtisanCropState.builder()
                                                    .actualState(Block.getId(Blocks.WHEAT.defaultBlockState().setValue(BlockStateProperties.AGE_7, 1)))
                                                    .appearanceState(
                                                            Block.getId(
                                                                    Blocks.TRIPWIRE.defaultBlockState()
                                                                            .setValue(BlockStateProperties.ATTACHED, false)
                                                                            .setValue(BlockStateProperties.DISARMED, true)
                                                                            .setValue(BlockStateProperties.EAST, false)
                                                                            .setValue(BlockStateProperties.NORTH, false)
                                                                            .setValue(BlockStateProperties.SOUTH, false)
                                                                            .setValue(BlockStateProperties.WEST, false)
                                                                            .setValue(BlockStateProperties.POWERED, false)
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
        }
    }
}
