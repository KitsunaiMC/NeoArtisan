package io.github.moyusowo.neoartisan.test;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.crop.CropRegistry;
import io.github.moyusowo.neoartisanapi.api.block.crop.CropStageProperty;
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
            CropRegistry.getCropRegistryManager().registerCrop(
                    magic_crop,
                    Block.getId(Blocks.WHEAT.defaultBlockState().setValue(BlockStateProperties.AGE_7, 1)),
                    List.of(
                            new CropStageProperty(
                                    Block.getId(
                                            Blocks.TRIPWIRE.defaultBlockState()
                                                    .setValue(BlockStateProperties.ATTACHED, false)
                                                    .setValue(BlockStateProperties.DISARMED, false)
                                                    .setValue(BlockStateProperties.EAST, false)
                                                    .setValue(BlockStateProperties.NORTH, false)
                                                    .setValue(BlockStateProperties.SOUTH, false)
                                                    .setValue(BlockStateProperties.WEST, false)
                                                    .setValue(BlockStateProperties.POWERED, false)
                                    ),
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
                            ),
                            new CropStageProperty(
                                    Block.getId(
                                            Blocks.TRIPWIRE.defaultBlockState()
                                                    .setValue(BlockStateProperties.ATTACHED, true)
                                                    .setValue(BlockStateProperties.DISARMED, false)
                                                    .setValue(BlockStateProperties.EAST, false)
                                                    .setValue(BlockStateProperties.NORTH, false)
                                                    .setValue(BlockStateProperties.SOUTH, false)
                                                    .setValue(BlockStateProperties.WEST, false)
                                                    .setValue(BlockStateProperties.POWERED, false)
                                    ),
                                    new ItemGenerator[0]
                            ),
                            new CropStageProperty(
                                    Block.getId(
                                            Blocks.TRIPWIRE.defaultBlockState()
                                                    .setValue(BlockStateProperties.ATTACHED, false)
                                                    .setValue(BlockStateProperties.DISARMED, true)
                                                    .setValue(BlockStateProperties.EAST, false)
                                                    .setValue(BlockStateProperties.NORTH, false)
                                                    .setValue(BlockStateProperties.SOUTH, false)
                                                    .setValue(BlockStateProperties.WEST, false)
                                                    .setValue(BlockStateProperties.POWERED, false)
                                    ),
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
                    ),
                    0,
                    2
            );
        }
    }
}
