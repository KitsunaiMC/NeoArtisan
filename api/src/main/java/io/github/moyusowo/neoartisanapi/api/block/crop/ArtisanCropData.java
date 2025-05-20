package io.github.moyusowo.neoartisanapi.api.block.crop;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockData;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;

@SuppressWarnings("unused")
public interface ArtisanCropData extends ArtisanBlockData {

    static Builder builder() {
        return Bukkit.getServicesManager().load(Builder.class);
    }

    boolean hasNextStage();

    ArtisanCropData getNextStage();

    ArtisanCropData getNextFertilizeStage();

    interface Builder {
        Builder blockId(NamespacedKey blockId);

        Builder stage(int stage);

        ArtisanCropData build();
    }

}
