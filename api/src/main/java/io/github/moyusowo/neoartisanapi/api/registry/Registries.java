package io.github.moyusowo.neoartisanapi.api.registry;

import io.github.moyusowo.neoartisanapi.api.util.BuilderFactoryUtil;

/**
 * NeoArtisan 所有注册表系统的访问类。
 */
public final class Registries {
    private Registries() {}

    // lazy load
    /**
     * NeoArtisan 自定义物品注册表。
     */
    public static final ItemRegistry ITEM = BuilderFactoryUtil.getBuilder(ItemRegistry.class);

    /**
     * NeoArtisan 自定义配方注册表。
     */
    public static final RecipeRegistry RECIPE = BuilderFactoryUtil.getBuilder(RecipeRegistry.class);

    /**
     * NeoArtisan 自定义方块注册表。
     */
    public static final BlockRegistry BLOCK = BuilderFactoryUtil.getBuilder(BlockRegistry.class);
}
