package io.github.moyusowo.neoartisanapi.api.block.packetblock;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlock;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ArtisanPacketBlock extends ArtisanBlock {

    static Builder builder() {
        return Bukkit.getServicesManager().load(Builder.class);
    }

    @NotNull ArtisanPacketBlockState getState(int n);

    interface Builder {

        Builder blockId(NamespacedKey blockId);

        Builder states(List<ArtisanPacketBlockState> states);

        Builder defaultState(int defaultState);

        ArtisanBlock build();
    }
}
