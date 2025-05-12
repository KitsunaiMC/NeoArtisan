package io.github.moyusowo.neoartisanapi.api.block.crop;

import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public interface ArtisanCrop {

    @NotNull NamespacedKey getCropId();

    @NotNull CropStageProperty getStage(int n);

    int getMaxStage();

    int getActualState();
}
