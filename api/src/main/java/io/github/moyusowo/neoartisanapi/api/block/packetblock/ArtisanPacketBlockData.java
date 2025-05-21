package io.github.moyusowo.neoartisanapi.api.block.packetblock;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockData;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;

public interface ArtisanPacketBlockData extends ArtisanBlockData {

    static Builder builder() {
        return Bukkit.getServicesManager().load(Builder.class);
    }

    interface Builder {

        Builder blockId(NamespacedKey blockId);

        Builder stage(int stage);

        ArtisanPacketBlockData build();
    }
}
