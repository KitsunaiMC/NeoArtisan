package io.github.moyusowo.neoartisanapi.api.block.crop;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockState;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;

@SuppressWarnings("unused")
public interface ArtisanCropData extends ArtisanBlockData {

    @Override
    ArtisanCrop getArtisanBlock();

    @Override
    ArtisanCropState getArtisanBlockState();

    static Builder builder() {
        return Bukkit.getServicesManager().load(Builder.class);
    }

    boolean hasNextStage();

    ArtisanCropData getNextStage();

    ArtisanCropData getNextFertilizeStage();

    interface Builder {

        Builder location(Location location);

        Builder blockId(NamespacedKey blockId);

        Builder stage(int stage);

        ArtisanCropData build();
    }

}
