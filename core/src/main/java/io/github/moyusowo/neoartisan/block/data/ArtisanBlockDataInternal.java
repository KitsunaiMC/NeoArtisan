package io.github.moyusowo.neoartisan.block.data;

import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

public interface ArtisanBlockDataInternal extends ArtisanBlockData {
    void setPersistentDataContainer(@NotNull PersistentDataContainer persistentDataContainer);
}
