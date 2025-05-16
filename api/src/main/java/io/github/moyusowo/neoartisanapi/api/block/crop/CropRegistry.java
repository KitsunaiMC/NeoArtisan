package io.github.moyusowo.neoartisanapi.api.block.crop;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;

import java.util.List;

public interface CropRegistry {

    void registerCrop(NamespacedKey cropId, int actualState, List<CropStageProperty> stages, int boneMealMinGrowth, int boneMealMaxGrowth);

    void registerCrop(NamespacedKey cropId, int actualState, List<CropStageProperty> stages, int boneMealGrowth);

    boolean isArtisanCrop(NamespacedKey cropId);

    ArtisanCrop getArtisanCrop(NamespacedKey cropId);
}
