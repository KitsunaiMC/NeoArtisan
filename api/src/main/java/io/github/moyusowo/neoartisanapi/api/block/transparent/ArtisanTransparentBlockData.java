package io.github.moyusowo.neoartisanapi.api.block.transparent;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlock;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCrop;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCropState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;

public interface ArtisanTransparentBlockData extends ArtisanBlockData {

    @Override
    ArtisanTransparentBlock getArtisanBlock();

    @Override
    ArtisanTransparentBlockState getArtisanBlockState();

    static Builder builder() {
        return Bukkit.getServicesManager().load(Builder.class);
    }

    interface Builder {

        Builder location(Location location);

        Builder blockId(NamespacedKey blockId);

        Builder stage(int stage);

        ArtisanTransparentBlockData build();
    }
}
