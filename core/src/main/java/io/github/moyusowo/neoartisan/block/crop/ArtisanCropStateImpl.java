package io.github.moyusowo.neoartisan.block.crop;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockStateBase;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCropState;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public class ArtisanCropStateImpl extends ArtisanBlockStateBase implements ArtisanCropState {

    @InitMethod(order = InitPriority.HIGH)
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
        public ArtisanCropState build() {
            if (generators == null || actualState == -1 || appearanceState == -1) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanCropStateImpl(appearanceState, actualState, generators);
        }
    }

}
