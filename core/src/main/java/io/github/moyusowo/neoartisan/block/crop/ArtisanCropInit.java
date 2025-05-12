package io.github.moyusowo.neoartisan.block.crop;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.internal.ArtisanCropStorageInternal;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCropStorage;
import io.github.moyusowo.neoartisanapi.api.block.crop.CropRegistry;
import io.github.moyusowo.neoartisanapi.api.block.crop.CurrentCropStage;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;

public final class ArtisanCropInit {

    private ArtisanCropInit() {}

    public static void init() {
        CropRegistryImpl.init();
        ArtisanCropStorageImpl.init();
        Bukkit.getServicesManager().register(
                CropRegistry.class,
                CropRegistryImpl.getInstance(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
        Bukkit.getServicesManager().register(
                ArtisanCropStorage.class,
                ArtisanCropStorageImpl.getInstance(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
        Bukkit.getServicesManager().register(
                ArtisanCropStorageInternal.class,
                ArtisanCropStorageImpl.getInstance(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }
}
