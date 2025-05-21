package io.github.moyusowo.neoartisan.block.packetblock;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockStateBase;
import io.github.moyusowo.neoartisanapi.api.block.packetblock.ArtisanPacketBlockState;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

class ArtisanPacketBlockStateImpl extends ArtisanBlockStateBase implements ArtisanPacketBlockState {

    @InitMethod(order = InitPriority.HIGH)
    private static void init() {
        Bukkit.getServicesManager().register(
                Builder.class,
                new BuilderImpl(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    protected ArtisanPacketBlockStateImpl(int appearanceState, int actualState, ItemGenerator[] generators) {
        super(appearanceState, actualState, generators);
    }

    public static class BuilderImpl implements Builder {
        protected int appearanceState;
        protected ItemGenerator[] generators;
        protected int actualState;

        public BuilderImpl() {
            appearanceState = -1;
            generators = null;
            actualState = -1;
        }

        @Override
        public Builder appearanceState(int appearanceState) {
            this.appearanceState = appearanceState;
            return this;
        }

        @Override
        public Builder actualState(int actualState) {
            this.actualState = actualState;
            return this;
        }

        @Override
        public Builder generators(ItemGenerator[] generators) {
            this.generators = generators;
            return this;
        }

        @Override
        public ArtisanPacketBlockState build() {
            if (generators == null || actualState == -1 || appearanceState == -1) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanPacketBlockStateImpl(appearanceState, actualState, generators);
        }
    }
}
