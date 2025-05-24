package io.github.moyusowo.neoartisan.block.thin;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.base.ArtisanBlockDataBaseInternal;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.thin.ArtisanThinBlock;
import io.github.moyusowo.neoartisanapi.api.block.thin.ArtisanThinBlockData;
import io.github.moyusowo.neoartisanapi.api.block.thin.ArtisanThinBlockState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.ServicePriority;

public class ArtisanThinBlockDataImpl extends ArtisanBlockDataBaseInternal implements ArtisanThinBlockData {

    @InitMethod(order = InitPriority.HIGH)
    private static void init() {
        Bukkit.getServicesManager().register(
                Builder.class,
                new BuilderImpl(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    protected ArtisanThinBlockDataImpl(NamespacedKey blockId, int stage, Location location) {
        super(blockId, stage, location);
    }

    @Override
    public ArtisanThinBlock getArtisanBlock() {
        return (ArtisanThinBlock) super.getArtisanBlock();
    }

    @Override
    public ArtisanThinBlockState getArtisanBlockState() {
        return (ArtisanThinBlockState) super.getArtisanBlockState();
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
        public ArtisanThinBlockData build() {
            if (blockId == null || stage < 0 || location == null) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanThinBlockDataImpl(blockId, stage, location);
        }
    }
}
