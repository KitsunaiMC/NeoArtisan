package io.github.moyusowo.neoartisanapi.api.block.transparent;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockState;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.Bukkit;

public interface ArtisanTransparentBlockState extends ArtisanBlockState {

    static Builder builder() {
        return Bukkit.getServicesManager().load(Builder.class);
    }

    interface Builder {

        Builder appearanceState(Appearance appearance);

        Builder generators(ItemGenerator[] generators);

        ArtisanTransparentBlockState build();
    }
}
