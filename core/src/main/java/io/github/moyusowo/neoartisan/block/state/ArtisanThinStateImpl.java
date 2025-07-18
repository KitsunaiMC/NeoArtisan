package io.github.moyusowo.neoartisan.block.state;

import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.state.base.ArtisanBaseBlockStateImpl;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.state.ArtisanThinState;
import io.github.moyusowo.neoartisanapi.api.block.state.appearance.ThinAppearance;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

final class ArtisanThinStateImpl extends ArtisanBaseBlockStateImpl implements ArtisanThinState {
    @InitMethod(priority = InitPriority.REGISTRAR)
    private static void init() {
        Bukkit.getServicesManager().register(
                BuilderFactory.class,
                BuilderImpl::new,
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private ArtisanThinStateImpl(int appearanceState, int actualState, ItemGenerator[] generators) {
        super(appearanceState, actualState, generators);
    }

    private static final class BuilderImpl implements Builder {
        private ThinAppearance thinAppearance;
        private ItemGenerator[] generators;
        private static final int actualState = WrappedBlockState.getByString("minecraft:oak_pressure_plate[powered=false]").getGlobalId();

        private int generateAppearanceState() {
            final WrappedBlockState wrappedBlockState;
            switch (this.thinAppearance.appearance()) {
                case HEAVY_WEIGHTED_PRESSURE_PLATE -> wrappedBlockState = WrappedBlockState.getByString("minecraft:heavy_weighted_pressure_plate[power=0]");
                case LIGHT_WEIGHTED_PRESSURE_PLATE -> wrappedBlockState = WrappedBlockState.getByString("minecraft:light_weighted_pressure_plate[power=0]");
                default -> wrappedBlockState = WrappedBlockState.getByGlobalId(0);
            }
            wrappedBlockState.setPower(this.thinAppearance.power());
            return wrappedBlockState.getGlobalId();
        }

        private BuilderImpl() {
            thinAppearance = null;
            generators = null;
        }


        @Override
        public @NotNull Builder appearance(@NotNull ThinAppearance thinBlockAppearance) {
            this.thinAppearance = thinBlockAppearance;
            return this;
        }

        @Override
        public @NotNull Builder generators(ItemGenerator[] generators) {
            this.generators = generators;
            return this;
        }

        @Override
        public @NotNull ArtisanThinState build() {
            if (generators == null || thinAppearance == null) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanThinStateImpl(generateAppearanceState(), actualState, generators);
        }
    }
}
