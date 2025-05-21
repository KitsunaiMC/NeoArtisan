package io.github.moyusowo.neoartisan.block.packetblock;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.base.internal.ArtisanBlockDataInternal;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockDataBase;
import io.github.moyusowo.neoartisanapi.api.block.packetblock.ArtisanPacketBlockData;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.ServicePriority;

class ArtisanPacketBlockDataImpl extends ArtisanBlockDataBase implements ArtisanPacketBlockData, ArtisanBlockDataInternal {

    @InitMethod(order = InitPriority.HIGH)
    private static void init() {
        Bukkit.getServicesManager().register(
                Builder.class,
                new BuilderImpl(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    protected ArtisanPacketBlockDataImpl(NamespacedKey blockId, int stage) {
        super(blockId, stage);
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
        public ArtisanPacketBlockData build() {
            if (blockId == null || stage == -1) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanPacketBlockDataImpl(blockId, stage);
        }
    }
}
