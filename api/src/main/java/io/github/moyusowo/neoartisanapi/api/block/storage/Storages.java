package io.github.moyusowo.neoartisanapi.api.block.storage;

import io.github.moyusowo.neoartisanapi.api.util.ServiceUtil;

public final class Storages {
    private Storages() {}

    // lazy load
    /**
     * Gets the block storage module instance.
     */
    public static final ArtisanBlockStorage BLOCK = ServiceUtil.createProxy(ArtisanBlockStorage.class);
}
