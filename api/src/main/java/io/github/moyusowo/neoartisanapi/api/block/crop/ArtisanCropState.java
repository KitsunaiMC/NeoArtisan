package io.github.moyusowo.neoartisanapi.api.block.crop;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockState;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.Bukkit;

@SuppressWarnings("unused")
public interface ArtisanCropState extends ArtisanBlockState {

    static Builder builder() {
        return Bukkit.getServicesManager().load(ArtisanCropState.Builder.class);
    }

    interface Builder {
        Builder appearance(Appearance appearance);

        Builder generators(ItemGenerator[] generators);

        ArtisanCropState build();
    }
}
