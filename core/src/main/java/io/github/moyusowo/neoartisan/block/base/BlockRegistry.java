package io.github.moyusowo.neoartisan.block.base;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlock;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.ServicePriority;

import java.util.concurrent.ConcurrentHashMap;

final class BlockRegistry implements io.github.moyusowo.neoartisanapi.api.block.base.BlockRegistry {

    private static BlockRegistry instance;

    public static BlockRegistry getInstance() {
        return instance;
    }

    @InitMethod(order = InitPriority.HIGH)
    public static void init() {
        new BlockRegistry();
    }

    private BlockRegistry() {
        instance = this;
        registry = new ConcurrentHashMap<>();
        Bukkit.getServicesManager().register(
                io.github.moyusowo.neoartisanapi.api.block.base.BlockRegistry.class,
                BlockRegistry.getInstance(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private final ConcurrentHashMap<NamespacedKey, ArtisanBlock> registry;

    @Override
    public void register(ArtisanBlock artisanBlock) {
        registry.put(artisanBlock.getBlockId(), artisanBlock);
    }

    @Override
    public boolean isArtisanBlock(NamespacedKey blockId) {
        return registry.containsKey(blockId);
    }

    @Override
    public ArtisanBlock getArtisanBlock(NamespacedKey blockId) {
        return registry.get(blockId);
    }
}
