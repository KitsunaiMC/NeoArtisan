package io.github.moyusowo.neoartisanapi.api.block;

import io.github.moyusowo.neoartisanapi.api.block.crop.CurrentCropStage;
import org.bukkit.persistence.PersistentDataContainer;

@SuppressWarnings("unused")
public interface ArtisanBlockState {
    PersistentDataContainer getPersistentDataContainer();
}
