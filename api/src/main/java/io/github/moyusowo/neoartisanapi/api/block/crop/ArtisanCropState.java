package io.github.moyusowo.neoartisanapi.api.block.crop;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockState;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@SuppressWarnings("unused")
public interface ArtisanCropState extends ArtisanBlockState {

    static Builder builder() {
        return Bukkit.getServicesManager().load(ArtisanCropState.Builder.class);
    }

    interface Builder {
        Builder appearanceState(int appearanceState);

        Builder generators(ItemGenerator[] generators);

        Builder actualState(int actualState);

        ArtisanCropState build();
    }
}
