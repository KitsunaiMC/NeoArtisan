package io.github.moyusowo.neoartisan.block.original;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.base.ArtisanBlockDataBaseInternal;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.original.ArtisanOriginalBlock;
import io.github.moyusowo.neoartisanapi.api.block.original.ArtisanOriginalBlockData;
import io.github.moyusowo.neoartisanapi.api.block.original.ArtisanOriginalBlockState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.ServicePriority;

public class ArtisanOriginalBlockDataImpl extends ArtisanBlockDataBaseInternal implements ArtisanOriginalBlockData {

    @InitMethod(priority = InitPriority.BLOCKDATA)
    private static void init() {
        Bukkit.getServicesManager().register(
                Builder.class,
                new BuilderImpl(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    protected ArtisanOriginalBlockDataImpl(NamespacedKey blockId, int stage, Location location) {
        super(blockId, stage, location);
    }

    @Override
    public ArtisanOriginalBlock getArtisanBlock() {
        return (ArtisanOriginalBlock) super.getArtisanBlock();
    }

    @Override
    public ArtisanOriginalBlockState getArtisanBlockState() {
        return (ArtisanOriginalBlockState) super.getArtisanBlockState();
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
        public ArtisanOriginalBlockData build() {
            if (blockId == null || stage < 0 || location == null) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanOriginalBlockDataImpl(blockId, stage, location);
        }
    }
}
