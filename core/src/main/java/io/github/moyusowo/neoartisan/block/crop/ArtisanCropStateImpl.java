package io.github.moyusowo.neoartisan.block.crop;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockStateBase;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.crop.*;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

class ArtisanCropStateImpl extends ArtisanBlockStateBase implements ArtisanCropState {

    @InitMethod(priority = InitPriority.REGISTRAR)
    private static void init() {
        Bukkit.getServicesManager().register(
                ArtisanCropState.Builder.class,
                new BuilderImpl(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    protected ArtisanCropStateImpl(int appearanceState, int actualState, ItemGenerator[] generators) {
        super(appearanceState, actualState, generators);
    }

    public static class BuilderImpl implements ArtisanCropState.Builder {
        protected CropAppearance cropAppearanceBlock;
        protected ItemGenerator[] generators;
        protected static final int actualState = Block.getId(Blocks.WHEAT.defaultBlockState().setValue(BlockStateProperties.AGE_7, 1));

        private int generateAppearanceState() {
            if (this.cropAppearanceBlock instanceof TripwireAppearance tripwireAppearance) {
                return Block.getId(
                        Blocks.TRIPWIRE.defaultBlockState()
                                .setValue(BlockStateProperties.ATTACHED, tripwireAppearance.get(TripwireAppearance.BlockStateProperty.ATTACHED))
                                .setValue(BlockStateProperties.DISARMED, tripwireAppearance.get(TripwireAppearance.BlockStateProperty.DISARMED))
                                .setValue(BlockStateProperties.EAST, tripwireAppearance.get(TripwireAppearance.BlockStateProperty.EAST))
                                .setValue(BlockStateProperties.NORTH, tripwireAppearance.get(TripwireAppearance.BlockStateProperty.NORTH))
                                .setValue(BlockStateProperties.SOUTH, tripwireAppearance.get(TripwireAppearance.BlockStateProperty.SOUTH))
                                .setValue(BlockStateProperties.WEST, tripwireAppearance.get(TripwireAppearance.BlockStateProperty.WEST))
                                .setValue(BlockStateProperties.POWERED, tripwireAppearance.get(TripwireAppearance.BlockStateProperty.POWERED))
                );
            } else {
                SugarCaneAppearance sugarCaneAppearance = (SugarCaneAppearance) this.cropAppearanceBlock;
                return Block.getId(
                        Blocks.SUGAR_CANE.defaultBlockState()
                                .setValue(BlockStateProperties.AGE_15, sugarCaneAppearance.get())
                );
            }
        }

        public BuilderImpl() {
            cropAppearanceBlock = null;
            generators = null;
        }

        @Override
        public Builder appearance(@NotNull CropAppearance cropAppearanceBlock) {
            this.cropAppearanceBlock = cropAppearanceBlock;
            return this;
        }

        @Override
        public Builder generators(ItemGenerator[] generators) {
            this.generators = generators;
            return this;
        }

        @Override
        public ArtisanCropState build() {
            if (generators == null || cropAppearanceBlock == null) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanCropStateImpl(generateAppearanceState(), actualState, generators);
        }
    }

}
