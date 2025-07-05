package io.github.moyusowo.neoartisan.block.base;

import io.github.moyusowo.neoartisan.block.base.internal.ArtisanBlockDataInternal;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockBaseData;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public abstract class ArtisanBlockBaseDataInternal extends ArtisanBlockBaseData implements ArtisanBlockDataInternal {
    protected ArtisanBlockBaseDataInternal(NamespacedKey blockId, int stage, Location location) {
        super(blockId, stage, location);
    }

    @Override
    public final void setPersistentDataContainer(PersistentDataContainer persistentDataContainer) {
        super.setPersistentDataContainer(persistentDataContainer);
    }
}
