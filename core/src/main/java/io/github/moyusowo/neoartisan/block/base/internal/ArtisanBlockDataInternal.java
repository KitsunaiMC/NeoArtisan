package io.github.moyusowo.neoartisan.block.base.internal;

import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.Internal
public interface ArtisanBlockDataInternal {
    void setPersistentDataContainer(PersistentDataContainer persistentDataContainer);

    static @NotNull ArtisanBlockDataInternal asInternal(Object object) {
        return (ArtisanBlockDataInternal) object;
    }
}
