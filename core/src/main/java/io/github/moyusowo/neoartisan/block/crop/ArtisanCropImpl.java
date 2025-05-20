package io.github.moyusowo.neoartisan.block.crop;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockBase;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlock;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCrop;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCropState;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

class ArtisanCropImpl extends ArtisanBlockBase implements ArtisanCrop {

    @InitMethod(order = InitPriority.HIGH)
    private static void init() {
        Bukkit.getServicesManager().register(
                ArtisanCrop.Builder.class,
                new BuilderImpl(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private final int boneMealMinGrowth, boneMealMaxGrowth;

    public ArtisanCropImpl(NamespacedKey cropId, int defaultState, List<ArtisanCropState> stages, int boneMealMinGrowth, int boneMealMaxGrowth) {
        super(cropId, stages, defaultState);
        this.boneMealMinGrowth = boneMealMinGrowth;
        this.boneMealMaxGrowth = boneMealMaxGrowth;
    }

    @Override
    public @NotNull ArtisanCropState getState(int n) {
        return (ArtisanCropState) super.getState(n);
    }

    @Override
    public int getBoneMealMinGrowth() {
        return this.boneMealMinGrowth;
    }

    @Override
    public int getBoneMealMaxGrowth() {
        return this.boneMealMaxGrowth;
    }

    @Override
    public int generateBoneMealGrowth() {
        return ThreadLocalRandom.current().nextInt(this.boneMealMinGrowth, this.boneMealMaxGrowth + 1);
    }

    public static class BuilderImpl implements Builder {

        protected NamespacedKey blockId;
        protected List<ArtisanCropState> stages;
        protected int defaultState;
        protected int boneMealMinGrowth;
        protected int boneMealMaxGrowth;

        public BuilderImpl() {
            blockId = null;
            stages = null;
            defaultState = -1;
            boneMealMaxGrowth = -1;
            boneMealMinGrowth = -1;
        }

        @Override
        public Builder blockId(NamespacedKey blockId) {
            this.blockId = blockId;
            return this;
        }

        public Builder stages(List<ArtisanCropState> stages) {
            this.stages = stages;
            return this;
        }

        public Builder defaultState(int defaultState) {
            this.defaultState = defaultState;
            return this;
        }

        @Override
        public Builder boneMealMinGrowth(int boneMealMinGrowth) {
            this.boneMealMinGrowth = boneMealMinGrowth;
            return this;
        }

        @Override
        public Builder boneMealMaxGrowth(int boneMealMaxGrowth) {
            this.boneMealMaxGrowth = boneMealMaxGrowth;
            return this;
        }

        @Override
        public ArtisanBlock build() {
            if (blockId == null || stages == null || defaultState == -1 || boneMealMinGrowth == -1 || boneMealMaxGrowth == -1) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanCropImpl(blockId, defaultState, stages, boneMealMinGrowth, boneMealMaxGrowth);
        }
    }

}
