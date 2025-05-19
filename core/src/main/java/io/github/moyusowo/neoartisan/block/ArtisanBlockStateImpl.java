package io.github.moyusowo.neoartisan.block;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.internal.ArtisanBlockStateInternal;
import io.github.moyusowo.neoartisanapi.api.block.ArtisanBlockState;
import org.bukkit.persistence.PersistentDataContainer;

public class ArtisanBlockStateImpl implements ArtisanBlockState, ArtisanBlockStateInternal {

    private final PersistentDataContainer persistentDataContainer;

    public ArtisanBlockStateImpl() {
        this.persistentDataContainer = NeoArtisan.emptyPersistentDataContainer();
    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return this.persistentDataContainer;
    }

    public void setPersistentDataContainer(PersistentDataContainer persistentDataContainer) {
        persistentDataContainer.copyTo(this.persistentDataContainer, true);
    }
}
