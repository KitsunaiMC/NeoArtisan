package io.github.moyusowo.neoartisan.block.crop;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockDataBase;
import io.github.moyusowo.neoartisan.block.base.internal.ArtisanBlockDataInternal;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCropData;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.ServicePriority;

class ArtisanCropDataImpl extends ArtisanBlockDataBase implements ArtisanCropData {

    @InitMethod(order = InitPriority.HIGH)
    private static void init() {
        Bukkit.getServicesManager().register(
                Builder.class,
                new BuilderImpl(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    public ArtisanCropDataImpl(NamespacedKey cropId, int stage) {
        super(cropId, stage);
    }

    @Override
    public boolean hasNextStage() {
        return super.stage() < super.getArtisanBlock().getTotalStates();
    }

    @Override
    public ArtisanCropData getNextStage() {
        if (!hasNextStage()) throw new IllegalCallerException("use has to check the existence before get!");
        return new ArtisanCropDataImpl(super.blockId(), super.stage() + 1);
    }

    @Override
    public ArtisanCropData getNextFertilizeStage() {
        if (!hasNextStage()) throw new IllegalCallerException("use has to check the existence before get!");
        int growth = ((ArtisanCropImpl) NeoArtisanAPI.getBlockRegistry().getArtisanBlock(super.blockId())).generateBoneMealGrowth();
        return new ArtisanCropDataImpl(super.blockId(), Math.min(super.stage() + growth, super.getArtisanBlock().getTotalStates()));
    }

    public static class BuilderImpl implements Builder {

        private NamespacedKey blockId;
        private int stage;

        public BuilderImpl() {
            blockId = null;
            stage = -1;
        }

        @Override
        public Builder blockId(NamespacedKey blockId) {
            this.blockId = blockId;
            return this;
        }

        @Override
        public Builder stage(int stage) {
            this.stage = stage;
            return this;
        }

        @Override
        public ArtisanCropData build() {
            if (blockId == null || stage == -1) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanCropDataImpl(blockId, stage);
        }
    }
}
