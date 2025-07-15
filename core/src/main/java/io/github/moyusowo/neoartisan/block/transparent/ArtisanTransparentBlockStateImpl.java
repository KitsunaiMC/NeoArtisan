package io.github.moyusowo.neoartisan.block.transparent;

import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockStateBase;
import io.github.moyusowo.neoartisanapi.api.block.transparent.TransparentAppearance;
import io.github.moyusowo.neoartisanapi.api.block.transparent.ArtisanTransparentBlockState;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

final class ArtisanTransparentBlockStateImpl extends ArtisanBlockStateBase implements ArtisanTransparentBlockState {

    @InitMethod(priority = InitPriority.REGISTRAR)
    private static void init() {
        Bukkit.getServicesManager().register(
                BuilderFactory.class,
                new BuilderFactory() {
                    @Override
                    public @NotNull Builder builder() {
                        return new BuilderImpl();
                    }
                },
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    ArtisanTransparentBlockStateImpl(int appearanceState, int actualState, ItemGenerator[] generators) {
        super(appearanceState, actualState, generators);
    }

    public static class BuilderImpl implements Builder {
        protected TransparentAppearance transparentAppearance;
        protected ItemGenerator[] generators;
        private static final int actualState = WrappedBlockState.getByString("minecraft:oak_leaves[distance=7,persistent=true,waterlogged=false]").getGlobalId();

        private int generateAppearanceState() {
            WrappedBlockState wrappedBlockState;
            switch (this.transparentAppearance.leavesAppearance) {
                case OAK_LEAVES -> wrappedBlockState = WrappedBlockState.getByString("minecraft:oak_leaves[distance=7,persistent=true,waterlogged=false]");
                case BIRCH_LEAVES -> wrappedBlockState = WrappedBlockState.getByString("minecraft:birch_leaves[distance=7,persistent=true,waterlogged=false]");
                case ACACIA_LEAVES -> wrappedBlockState = WrappedBlockState.getByString("minecraft:acacia_leaves[distance=7,persistent=true,waterlogged=false]");
                case AZALEA_LEAVES -> wrappedBlockState = WrappedBlockState.getByString("minecraft:azalea_leaves[distance=7,persistent=true,waterlogged=false]");
                case CHERRY_LEAVES -> wrappedBlockState = WrappedBlockState.getByString("minecraft:cherry_leaves[distance=7,persistent=true,waterlogged=false]");
                case JUNGLE_LEAVES -> wrappedBlockState = WrappedBlockState.getByString("minecraft:jungle_leaves[distance=7,persistent=true,waterlogged=false]");
                case SPRUCE_LEAVES -> wrappedBlockState = WrappedBlockState.getByString("minecraft:spruce_leaves[distance=7,persistent=true,waterlogged=false]");
                case DARK_OAK_LEAVES -> wrappedBlockState = WrappedBlockState.getByString("minecraft:dark_oak_leaves[distance=7,persistent=true,waterlogged=false]");
                case MANGROVE_LEAVES -> wrappedBlockState = WrappedBlockState.getByString("minecraft:mangrove_leaves[distance=7,persistent=true,waterlogged=false]");
                case PALE_OAK_LEAVES -> wrappedBlockState = WrappedBlockState.getByString("minecraft:pale_oak_leaves[distance=7,persistent=true,waterlogged=false]");
                case FLOWERING_AZALEA_LEAVES -> wrappedBlockState = WrappedBlockState.getByString("minecraft:flowering_azalea_leaves[distance=7,persistent=true,waterlogged=false]");
                default -> wrappedBlockState = WrappedBlockState.getByGlobalId(0);
            }
            wrappedBlockState.setDistance(this.transparentAppearance.distance);
            wrappedBlockState.setPersistent(this.transparentAppearance.persistent);
            wrappedBlockState.setWaterlogged(this.transparentAppearance.waterlogged);
            return wrappedBlockState.getGlobalId();
        }

        public BuilderImpl() {
            transparentAppearance = null;
            generators = null;
        }


        @Override
        public @NotNull Builder appearanceState(@NotNull TransparentAppearance transparentAppearance) {
            this.transparentAppearance = transparentAppearance;
            return this;
        }

        @Override
        public @NotNull Builder generators(ItemGenerator[] generators) {
            this.generators = generators;
            return this;
        }

        @Override
        public @NotNull ArtisanTransparentBlockState build() {
            if (generators == null || transparentAppearance == null) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanTransparentBlockStateImpl(generateAppearanceState(), actualState, generators);
        }
    }
}
