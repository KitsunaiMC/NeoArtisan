package io.github.moyusowo.neoartisanapi.api.block.crop;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;

import java.util.List;

public interface CropRegistry {

    static CropRegistry getCropRegistryManager() {
        return Bukkit.getServicesManager().load(CropRegistry.class);
    }

    void registerCrop(NamespacedKey cropId, int actualState, List<CropStageProperty> stages);

    boolean isArtisanCrop(NamespacedKey cropId);

    ArtisanCrop getArtisanCrop(NamespacedKey cropId);
}
