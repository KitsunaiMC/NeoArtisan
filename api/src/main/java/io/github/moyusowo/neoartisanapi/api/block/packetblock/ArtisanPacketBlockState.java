package io.github.moyusowo.neoartisanapi.api.block.packetblock;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockState;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCropState;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.Bukkit;

public interface ArtisanPacketBlockState extends ArtisanBlockState {

    static Builder builder() {
        return Bukkit.getServicesManager().load(Builder.class);
    }

    interface Builder {

        Builder actualState(int actualState);

        Builder appearanceState(int appearanceState);

        Builder generators(ItemGenerator[] generators);

        ArtisanPacketBlockState build();
    }
}
