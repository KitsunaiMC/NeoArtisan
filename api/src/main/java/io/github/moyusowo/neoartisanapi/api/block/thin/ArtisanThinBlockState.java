package io.github.moyusowo.neoartisanapi.api.block.thin;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockState;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.Bukkit;

public interface ArtisanThinBlockState extends ArtisanBlockState {

    static Builder builder() {
        return Bukkit.getServicesManager().load(Builder.class);
    }

    interface Builder {

        Builder appearanceState(ThinBlockAppearance thinBlockAppearance);

        Builder generators(ItemGenerator[] generators);

        ArtisanThinBlockState build();
    }
}
