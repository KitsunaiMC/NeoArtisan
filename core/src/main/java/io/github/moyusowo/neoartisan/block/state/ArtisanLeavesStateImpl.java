package io.github.moyusowo.neoartisan.block.state;

import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.state.base.ArtisanBaseBlockStateImpl;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.state.ArtisanLeavesState;
import io.github.moyusowo.neoartisanapi.api.block.state.appearance.LeavesAppearance;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

final class ArtisanLeavesStateImpl extends ArtisanBaseBlockStateImpl implements ArtisanLeavesState {
    @InitMethod(priority = InitPriority.REGISTRAR)
    private static void init() {
        Bukkit.getServicesManager().register(
                BuilderFactory.class,
                BuilderImpl::new,
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private final boolean canBurn;
    private final float hardness;

    private ArtisanLeavesStateImpl(int appearanceState, int actualState, ItemGenerator[] generators, float hardness, boolean canBurn) {
        super(appearanceState, actualState, generators);
        this.hardness = hardness;
        this.canBurn = canBurn;
    }

    @Override
    public boolean canSurviveFloating() {
        return true;
    }

    @Override
    @NotNull
    public Float getHardness() {
        return hardness;
    }

    @Override
    public boolean canBurn() {
        return this.canBurn;
    }

    private static final class BuilderImpl implements Builder {
        private LeavesAppearance leavesAppearance;
        private ItemGenerator[] generators;
        private boolean canBurn;
        private float hardness;
        private static final int actualState = WrappedBlockState.getByString("minecraft:oak_leaves[distance=7,persistent=true,waterlogged=false]").getGlobalId();

        private int generateAppearanceState() {
            WrappedBlockState wrappedBlockState;
            switch (this.leavesAppearance.leavesAppearance()) {
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
            wrappedBlockState.setDistance(this.leavesAppearance.distance());
            wrappedBlockState.setPersistent(this.leavesAppearance.persistent());
            wrappedBlockState.setWaterlogged(this.leavesAppearance.waterlogged());
            return wrappedBlockState.getGlobalId();
        }

        public BuilderImpl() {
            leavesAppearance = null;
            generators = null;
            canBurn = false;
            hardness = -Float.MAX_VALUE;
        }


        @Override
        public @NotNull Builder appearance(@NotNull LeavesAppearance transparentAppearance) {
            this.leavesAppearance = transparentAppearance;
            return this;
        }

        @Override
        public @NotNull Builder generators(ItemGenerator[] generators) {
            this.generators = generators;
            return this;
        }

        @Override
        public @NotNull Builder hardness(float hardness) {
            this.hardness = hardness;
            return this;
        }

        @Override
        public @NotNull Builder burnable() {
            canBurn = true;
            return this;
        }

        @Override
        public @NotNull ArtisanLeavesState build() {
            if (generators == null || leavesAppearance == null || hardness <= 0) throw new IllegalArgumentException("You must fill all the param correctly!");
            return new ArtisanLeavesStateImpl(generateAppearanceState(), actualState, generators, hardness, canBurn);
        }
    }
}
