package io.github.moyusowo.neoartisan.block.internal;

import io.github.moyusowo.neoartisanapi.api.block.crop.CurrentCropStage;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

@ApiStatus.Internal
@SuppressWarnings("unused")
public interface ArtisanBlockStateInternal {
    void setPersistentDataContainer(PersistentDataContainer persistentDataContainer);

    static @Nullable ArtisanBlockStateInternal asInternal(Object object) {
        if (object instanceof ArtisanBlockStateInternal) {
            return (ArtisanBlockStateInternal) object;
        }
        return null;
    }
}
