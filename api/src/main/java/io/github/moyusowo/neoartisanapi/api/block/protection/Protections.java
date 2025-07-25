package io.github.moyusowo.neoartisanapi.api.block.protection;

import io.github.moyusowo.neoartisanapi.api.util.BuilderFactoryUtil;

public final class Protections {
    private Protections() {}

    // lazy load
    /**
     * 获取保护模块实例。
     */
    public static final ArtisanBlockProtection BLOCK = BuilderFactoryUtil.getBuilder(ArtisanBlockProtection.class);
}
