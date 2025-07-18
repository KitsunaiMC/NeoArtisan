package io.github.moyusowo.neoartisan.block.block;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.block.base.ArtisanBaseBlockImpl;
import io.github.moyusowo.neoartisan.block.block.listener.Util;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.block.ArtisanCropBlock;
import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.block.util.SoundProperty;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

final class ArtisanCropBlockImpl extends ArtisanBaseBlockImpl implements ArtisanCropBlock {
    @InitMethod(priority = InitPriority.REGISTRAR)
    private static void init() {
        Bukkit.getServicesManager().register(
                BuilderFactory.class,
                BuilderImpl::new,
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private final int boneMealMinGrowth, boneMealMaxGrowth;

    private ArtisanCropBlockImpl(@NotNull NamespacedKey blockId, @NotNull List<ArtisanBaseBlockState> stages, SoundProperty placeSound, int boneMealMinGrowth, int boneMealMaxGrowth) {
        super(blockId, stages, null, placeSound);
        this.boneMealMinGrowth = boneMealMinGrowth;
        this.boneMealMaxGrowth = boneMealMaxGrowth;
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

    @Override
    public void onRandomTick(ArtisanBlockData cropData) {
        if (Util.hasNextStage(cropData)) {
            Util.replace(cropData.getLocation().getBlock(), Util.getNextStage(cropData));
        }
    }

    public static final class BuilderImpl implements Builder {
        private NamespacedKey blockId;
        private List<ArtisanBaseBlockState> stages;
        private int boneMealMinGrowth;
        private int boneMealMaxGrowth;
        private SoundProperty placeSound;

        public BuilderImpl() {
            blockId = null;
            stages = null;
            placeSound = null;
            boneMealMaxGrowth = -1;
            boneMealMinGrowth = -1;
        }

        @Override
        public @NotNull Builder blockId(@NotNull NamespacedKey blockId) {
            this.blockId = blockId;
            return this;
        }

        @Override
        public @NotNull Builder states(@NotNull List<ArtisanBaseBlockState> states) {
            this.stages = states;
            return this;
        }

        @Override
        public @NotNull Builder placeSound(@NotNull SoundProperty placeSoundProperty) {
            this.placeSound = placeSoundProperty;
            return this;
        }

        @Override
        public @NotNull Builder boneMealMinGrowth(int boneMealMinGrowth) {
            this.boneMealMinGrowth = boneMealMinGrowth;
            return this;
        }

        @Override
        public @NotNull Builder boneMealMaxGrowth(int boneMealMaxGrowth) {
            this.boneMealMaxGrowth = boneMealMaxGrowth;
            return this;
        }

        @Override
        public @NotNull ArtisanCropBlock build() {
            if (blockId == null || stages == null || boneMealMinGrowth == -1 || boneMealMaxGrowth == -1) throw new IllegalArgumentException("You must fill all the param!");
            if (boneMealMinGrowth < 0 || boneMealMinGrowth > boneMealMaxGrowth) throw new IllegalArgumentException("min can't larger than max!");
            return new ArtisanCropBlockImpl(blockId, stages, placeSound, boneMealMinGrowth, boneMealMaxGrowth);
        }
    }
}
