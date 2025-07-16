package io.github.moyusowo.neoartisan.block.blockstate;

import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.blockstate.ArtisanLeavesState;
import io.github.moyusowo.neoartisanapi.api.block.blockstate.appearance.LeavesAppearance;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

public class ArtisanLeavesStateImpl extends ArtisanBaseBlockStateImpl implements ArtisanLeavesState {
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

    protected ArtisanLeavesStateImpl(int appearanceState, int actualState, ItemGenerator[] generators, boolean canBurn) {
        super(appearanceState, actualState, generators);
        this.canBurn = canBurn;
    }

    @Override
    public boolean canSurviveFloating() {
        return true;
    }

    @Override
    public boolean canBurn() {
        return this.canBurn;
    }

    private static final class BuilderImpl implements Builder {
        private LeavesAppearance leavesAppearance;
        private ItemGenerator[] generators;
        private boolean canBurn;
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
        public @NotNull Builder burnable() {
            canBurn = true;
            return this;
        }

        @Override
        public @NotNull ArtisanLeavesState build() {
            if (generators == null || leavesAppearance == null) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanLeavesStateImpl(generateAppearanceState(), actualState, generators, canBurn);
        }
    }
}
