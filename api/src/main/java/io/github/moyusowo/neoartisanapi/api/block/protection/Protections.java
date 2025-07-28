package io.github.moyusowo.neoartisanapi.api.block.protection;

import io.github.moyusowo.neoartisanapi.api.util.ServiceUtil;

public final class Protections {
    private Protections() {}

    // lazy load
    /**
     * Gets the protection module instance.
     */
    public static final ArtisanBlockProtection BLOCK = ServiceUtil.createProxy(ArtisanBlockProtection.class);
}
