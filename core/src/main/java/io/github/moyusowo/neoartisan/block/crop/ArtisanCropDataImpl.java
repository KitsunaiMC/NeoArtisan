package io.github.moyusowo.neoartisan.block.crop;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.base.ArtisanBlockBaseDataInternal;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCrop;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCropData;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCropState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

final class ArtisanCropDataImpl extends ArtisanBlockBaseDataInternal implements ArtisanCropData {

    @InitMethod(priority = InitPriority.BLOCKDATA)
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

    ArtisanCropDataImpl(NamespacedKey cropId, int stage, Location location) {
        super(cropId, stage, location);
    }

    @Override
    public @NotNull ArtisanCrop getArtisanBlock() {
        return (ArtisanCrop) super.getArtisanBlock();
    }

    @Override
    public @NotNull ArtisanCropState getArtisanBlockState() {
        return (ArtisanCropState) super.getArtisanBlockState();
    }

    @Override
    public boolean hasNextStage() {
        return super.stage() < super.getArtisanBlock().getTotalStates();
    }

    @Override
    public @NotNull ArtisanCropData getNextStage() {
        if (!hasNextStage()) throw new IllegalCallerException("use has to check the existence before get!");
        return new ArtisanCropDataImpl(super.blockId(), super.stage() + 1, super.getLocation());
    }

    @Override
    public @NotNull ArtisanCropData getNextFertilizeStage() {
        if (!hasNextStage()) throw new IllegalCallerException("use has to check the existence before get!");
        int growth = ((ArtisanCropImpl) NeoArtisanAPI.getBlockRegistry().getArtisanBlock(super.blockId())).generateBoneMealGrowth();
        return new ArtisanCropDataImpl(super.blockId(), Math.min(super.stage() + growth, super.getArtisanBlock().getTotalStates()), super.getLocation());
    }

    public static class BuilderImpl implements Builder {

        private NamespacedKey blockId;
        private int stage;
        private Location location;

        public BuilderImpl() {
            blockId = null;
            location = null;
            stage = -1;
        }

        @Override
        public @NotNull Builder blockId(@NotNull NamespacedKey blockId) {
            this.blockId = blockId;
            return this;
        }

        @Override
        public @NotNull Builder stage(int stage) {
            this.stage = stage;
            return this;
        }

        @Override
        public @NotNull Builder location(@NotNull Location location) {
            this.location = location;
            return this;
        }

        @Override
        public @NotNull ArtisanCropData build() {
            if (blockId == null || stage == -1 || location == null) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanCropDataImpl(blockId, stage, location);
        }
    }
}
