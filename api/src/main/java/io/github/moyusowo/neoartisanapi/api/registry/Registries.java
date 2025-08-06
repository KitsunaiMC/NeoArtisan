package io.github.moyusowo.neoartisanapi.api.registry;

import io.github.moyusowo.neoartisanapi.api.util.ServiceUtil;

/**
 * Access class for all NeoArtisan registry systems.
 */
public final class Registries {
    private Registries() {}

    // lazy load
    /**
     * NeoArtisan custom item registry.
     */
    public static final ItemRegistry ITEM = ServiceUtil.createProxy(ItemRegistry.class);

    /**
     * NeoArtisan custom recipe registry.
     */
    public static final RecipeRegistry RECIPE = ServiceUtil.createProxy(RecipeRegistry.class);

    /**
     * NeoArtisan custom block registry.
     */
    public static final BlockRegistry BLOCK = ServiceUtil.createProxy(BlockRegistry.class);

    /**
     * NeoArtisan guide book registry.
     */
    public static final GuideRegistry GUIDE = ServiceUtil.createProxy(GuideRegistry.class);
}
