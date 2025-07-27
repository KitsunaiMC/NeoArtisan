package io.github.moyusowo.neoartisanapi.api.block.storage;

import io.github.moyusowo.neoartisanapi.api.util.ServiceUtil;

public final class Storages {
    private Storages() {}

    // lazy load
    /**
     * 获取方块储存模块实例。
     */
    public static final ArtisanBlockStorage BLOCK = ServiceUtil.createProxy(ArtisanBlockStorage.class);
}
