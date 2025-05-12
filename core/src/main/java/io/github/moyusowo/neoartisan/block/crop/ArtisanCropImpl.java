package io.github.moyusowo.neoartisan.block.crop;

import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCrop;
import io.github.moyusowo.neoartisanapi.api.block.crop.CropStageProperty;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.IntegerRange;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

class ArtisanCropImpl implements ArtisanCrop {
    private final NamespacedKey cropId;
    private final List<CropStageProperty> stages;
    private final int actualState;
    private final int boneMealMinGrowth, boneMealMaxGrowth;

    public ArtisanCropImpl(NamespacedKey cropId, int actualState, List<CropStageProperty> stages, int boneMealMinGrowth, int boneMealMaxGrowth) {
        this.cropId = cropId;
        this.stages = stages;
        this.actualState = actualState;
        this.boneMealMinGrowth = boneMealMinGrowth;
        this.boneMealMaxGrowth = boneMealMaxGrowth;
    }

    @Override
    public @NotNull NamespacedKey getCropId() {
        return this.cropId;
    }

    @Override
    public @NotNull CropStageProperty getStage(int n) {
        if (n > getMaxStage()) return this.stages.getLast();
        else return this.stages.get(n);
    }

    @Override
    public int getMaxStage() {
        return this.stages.size() - 1;
    }

    @Override
    public int getActualState() {
        return this.actualState;
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

}
