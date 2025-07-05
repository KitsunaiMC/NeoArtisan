package io.github.moyusowo.neoartisan.block.full;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.base.ArtisanBlockBaseDataInternal;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.full.ArtisanFullBlock;
import io.github.moyusowo.neoartisanapi.api.block.full.ArtisanFullBlockData;
import io.github.moyusowo.neoartisanapi.api.block.full.ArtisanFullBlockState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

class ArtisanFullBlockDataImpl extends ArtisanBlockBaseDataInternal implements ArtisanFullBlockData {
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

    protected ArtisanFullBlockDataImpl(NamespacedKey blockId, int stage, Location location) {
        super(blockId, stage, location);
    }

    @Override
    public @NotNull ArtisanFullBlock getArtisanBlock() {
        return (ArtisanFullBlock) super.getArtisanBlock();
    }

    @Override
    public @NotNull ArtisanFullBlockState getArtisanBlockState() {
        return (ArtisanFullBlockState) super.getArtisanBlockState();
    }

    private static class BuilderImpl implements Builder {

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
        public ArtisanFullBlockData build() {
            if (blockId == null || stage < 0 || location == null) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanFullBlockDataImpl(blockId, stage, location);
        }
    }
}
