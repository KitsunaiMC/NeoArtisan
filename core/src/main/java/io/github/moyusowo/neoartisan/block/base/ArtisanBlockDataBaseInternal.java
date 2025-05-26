package io.github.moyusowo.neoartisan.block.base;

import io.github.moyusowo.neoartisan.block.base.internal.ArtisanBlockDataInternal;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockDataBase;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public abstract class ArtisanBlockDataBaseInternal extends ArtisanBlockDataBase implements ArtisanBlockDataInternal {

    protected ArtisanBlockDataBaseInternal(NamespacedKey blockId, int stage, Location location) {
        super(blockId, stage, location);
    }

}
