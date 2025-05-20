package io.github.moyusowo.neoartisanapi.api.block.base;

import org.bukkit.NamespacedKey;

public interface BlockRegistry {

    void register(ArtisanBlock artisanBlock);

    boolean isArtisanBlock(NamespacedKey blockId);

    ArtisanBlock getArtisanBlock(NamespacedKey blockId);

}
