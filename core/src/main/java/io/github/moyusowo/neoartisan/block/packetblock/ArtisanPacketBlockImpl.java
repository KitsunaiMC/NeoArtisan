package io.github.moyusowo.neoartisan.block.packetblock;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlock;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockBase;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockState;
import io.github.moyusowo.neoartisanapi.api.block.packetblock.ArtisanPacketBlock;
import io.github.moyusowo.neoartisanapi.api.block.packetblock.ArtisanPacketBlockState;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ArtisanPacketBlockImpl extends ArtisanBlockBase implements ArtisanPacketBlock {

    @InitMethod(order = InitPriority.HIGH)
    private static void init() {
        Bukkit.getServicesManager().register(
                Builder.class,
                new BuilderImpl(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    protected ArtisanPacketBlockImpl(NamespacedKey blockId, List<? extends ArtisanBlockState> stages, int defaultState) {
        super(blockId, stages, defaultState);
    }

    @Override
    public @NotNull ArtisanPacketBlockState getState(int n) {
        return (ArtisanPacketBlockState) super.getState(n);
    }

    public static class BuilderImpl implements Builder {

        protected NamespacedKey blockId;
        protected List<ArtisanPacketBlockState> stages;
        protected int defaultState;

        public BuilderImpl() {
            blockId = null;
            stages = null;
            defaultState = -1;
        }

        @Override
        public Builder blockId(NamespacedKey blockId) {
            this.blockId = blockId;
            return this;
        }

        public Builder states(List<ArtisanPacketBlockState> states) {
            this.stages = states;
            return this;
        }

        public Builder defaultState(int defaultState) {
            this.defaultState = defaultState;
            return this;
        }

        @Override
        public ArtisanBlock build() {
            if (blockId == null || stages == null || defaultState == -1) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanPacketBlockImpl(blockId, stages, defaultState);
        }
    }
}
