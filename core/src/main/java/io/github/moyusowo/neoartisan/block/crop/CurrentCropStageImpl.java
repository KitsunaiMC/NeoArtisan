package io.github.moyusowo.neoartisan.block.crop;

import io.github.moyusowo.neoartisan.block.ArtisanBlockStateImpl;
import io.github.moyusowo.neoartisanapi.api.block.crop.CurrentCropStage;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

class CurrentCropStageImpl extends ArtisanBlockStateImpl implements CurrentCropStage {

    private final NamespacedKey cropId;
    private final int stage;

    public CurrentCropStageImpl(NamespacedKey cropId, int stage) {
        this.cropId = cropId;
        this.stage = stage;
    }

    @Override
    public NamespacedKey cropId() {
        return this.cropId;
    }

    @Override
    public int stage() {
        return this.stage;
    }

    @Override
    public int getBlockState() {
        return CropRegistryImpl.getInstance().getArtisanCrop(cropId).getStage(stage).appearanceState();
    }

    @Override
    public ItemStack[] getDrops() {
        return CropRegistryImpl.getInstance().getArtisanCrop(cropId).getStage(stage).drops();
    }

    @Override
    public ItemGenerator[] getGenerators() {
        return CropRegistryImpl.getInstance().getArtisanCrop(cropId).getStage(stage).generators();
    }

    @Override
    public boolean hasNextStage() {
        return stage < getMaxStage();
    }

    @Override
    public CurrentCropStage getNextStage() {
        if (!hasNextStage()) throw new IllegalCallerException("use has to check the existence before get!");
        return new CurrentCropStageImpl(cropId, stage + 1);
    }

    @Override
    public CurrentCropStage getNextFertilizeStage() {
        if (!hasNextStage()) throw new IllegalCallerException("use has to check the existence before get!");
        int growth = CropRegistryImpl.getInstance().getArtisanCrop(cropId).generateBoneMealGrowth();
        return new CurrentCropStageImpl(cropId, Math.min(stage + growth, getMaxStage()));
    }

    @Override
    public int getMaxStage() {
        return CropRegistryImpl.getInstance().getArtisanCrop(cropId).getMaxStage();
    }
}
