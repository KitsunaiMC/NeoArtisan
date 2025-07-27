package io.github.moyusowo.neoartisanapi.api.registry;

import io.github.moyusowo.neoartisanapi.api.util.ServiceUtil;

/**
 * NeoArtisan 所有注册表系统的访问类。
 */
public final class Registries {
    private Registries() {}

    // lazy load
    /**
     * NeoArtisan 自定义物品注册表。
     */
    public static final ItemRegistry ITEM = ServiceUtil.createProxy(ItemRegistry.class);

    /**
     * NeoArtisan 自定义配方注册表。
     */
    public static final RecipeRegistry RECIPE = ServiceUtil.createProxy(RecipeRegistry.class);

    /**
     * NeoArtisan 自定义方块注册表。
     */
    public static final BlockRegistry BLOCK = ServiceUtil.createProxy(BlockRegistry.class);
}
