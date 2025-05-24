package io.github.moyusowo.neoartisan.block.transparent;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockStateBase;
import io.github.moyusowo.neoartisanapi.api.block.transparent.TransparentAppearance;
import io.github.moyusowo.neoartisanapi.api.block.transparent.ArtisanTransparentBlockState;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

class ArtisanTransparentBlockStateImpl extends ArtisanBlockStateBase implements ArtisanTransparentBlockState {

    @InitMethod(order = InitPriority.HIGH)
    private static void init() {
        Bukkit.getServicesManager().register(
                Builder.class,
                new BuilderImpl(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    protected ArtisanTransparentBlockStateImpl(int appearanceState, int actualState, ItemGenerator[] generators) {
        super(appearanceState, actualState, generators);
    }

    public static class BuilderImpl implements Builder {
        protected TransparentAppearance transparentAppearance;
        protected ItemGenerator[] generators;
        private static final int actualState = Block.getId(
                Blocks.OAK_LEAVES.defaultBlockState()
                        .setValue(BlockStateProperties.DISTANCE, 7)
                        .setValue(BlockStateProperties.PERSISTENT, true)
                        .setValue(BlockStateProperties.WATERLOGGED, false)
        );

        private int generateAppearanceState() {
            BlockState blockState;
            switch (this.transparentAppearance.leavesAppearance) {
                case BIRCH_LEAVES -> blockState = Blocks.BIRCH_LEAVES.defaultBlockState();
                case ACACIA_LEAVES -> blockState = Blocks.ACACIA_LEAVES.defaultBlockState();
                case AZALEA_LEAVES -> blockState = Blocks.AZALEA_LEAVES.defaultBlockState();
                case CHERRY_LEAVES -> blockState = Blocks.CHERRY_LEAVES.defaultBlockState();
                case JUNGLE_LEAVES -> blockState = Blocks.JUNGLE_LEAVES.defaultBlockState();
                case SPRUCE_LEAVES -> blockState = Blocks.SPRUCE_LEAVES.defaultBlockState();
                case DARK_OAK_LEAVES -> blockState = Blocks.DARK_OAK_LEAVES.defaultBlockState();
                case MANGROVE_LEAVES -> blockState = Blocks.MANGROVE_LEAVES.defaultBlockState();
                case PALE_OAK_LEAVES -> blockState = Blocks.PALE_OAK_LEAVES.defaultBlockState();
                case FLOWERING_AZALEA_LEAVES -> blockState = Blocks.FLOWERING_AZALEA_LEAVES.defaultBlockState();
                case null, default -> blockState = Blocks.OAK_LEAVES.defaultBlockState();
            }
            return Block.getId(
                    blockState.setValue(BlockStateProperties.DISTANCE, this.transparentAppearance.distance)
                            .setValue(BlockStateProperties.PERSISTENT, this.transparentAppearance.persistent)
                            .setValue(BlockStateProperties.WATERLOGGED, this.transparentAppearance.waterlogged)
            );
        }

        public BuilderImpl() {
            transparentAppearance = null;
            generators = null;
        }


        @Override
        public Builder appearanceState(TransparentAppearance transparentAppearance) {
            this.transparentAppearance = transparentAppearance;
            return this;
        }

        @Override
        public Builder generators(ItemGenerator[] generators) {
            this.generators = generators;
            return this;
        }

        @Override
        public ArtisanTransparentBlockState build() {
            if (generators == null || transparentAppearance == null) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanTransparentBlockStateImpl(generateAppearanceState(), actualState, generators);
        }
    }
}
