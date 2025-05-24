package io.github.moyusowo.neoartisan.block.transparent;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.base.internal.ArtisanBlockDataInternal;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockDataBase;
import io.github.moyusowo.neoartisanapi.api.block.transparent.ArtisanTransparentBlock;
import io.github.moyusowo.neoartisanapi.api.block.transparent.ArtisanTransparentBlockData;
import io.github.moyusowo.neoartisanapi.api.block.transparent.ArtisanTransparentBlockState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.ServicePriority;

class ArtisanTransparentBlockDataImpl extends ArtisanBlockDataBase implements ArtisanTransparentBlockData, ArtisanBlockDataInternal {

    @InitMethod(order = InitPriority.HIGH)
    private static void init() {
        Bukkit.getServicesManager().register(
                Builder.class,
                new BuilderImpl(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    protected ArtisanTransparentBlockDataImpl(NamespacedKey blockId, int stage, Location location) {
        super(blockId, stage, location);
    }

    @Override
    public ArtisanTransparentBlock getArtisanBlock() {
        return (ArtisanTransparentBlock) super.getArtisanBlock();
    }

    @Override
    public ArtisanTransparentBlockState getArtisanBlockState() {
        return (ArtisanTransparentBlockState) super.getArtisanBlockState();
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
        public Builder location(Location location) {
            this.location = location;
            return this;
        }

        @Override
        public ArtisanTransparentBlockData build() {
            if (blockId == null || stage < 0 || location == null) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanTransparentBlockDataImpl(blockId, stage, location);
        }
    }
}
