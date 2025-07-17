package io.github.moyusowo.neoartisan.block;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.RegisterManager;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.BlockRegistry;
import io.github.moyusowo.neoartisanapi.api.block.block.base.ArtisanBaseBlock;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ConcurrentHashMap;

final class BlockRegistryImpl implements BlockRegistry {

    private static BlockRegistryImpl instance;

    public static BlockRegistryImpl getInstance() {
        return instance;
    }

    @InitMethod(priority = InitPriority.REGISTRY_LOAD)
    public static void init() {
        new BlockRegistryImpl();
    }

    private BlockRegistryImpl() {
        instance = this;
        registry = new ConcurrentHashMap<>();
        Bukkit.getServicesManager().register(
                BlockRegistry.class,
                BlockRegistryImpl.getInstance(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private final ConcurrentHashMap<NamespacedKey, ArtisanBaseBlock> registry;

    @Override
    public void register(@NotNull ArtisanBaseBlock artisanBlock) {
        if (RegisterManager.isOpen()) {
            registry.put(artisanBlock.getBlockId(), artisanBlock);
            NeoArtisan.logger().info("successfully register block: " + artisanBlock.getBlockId().asString());
        } else {
            throw RegisterManager.REGISTRY_CLOSED;
        }
    }

    @Override
    public boolean isArtisanBlock(NamespacedKey blockId) {
        return registry.containsKey(blockId);
    }

    @Override
    public @NotNull ArtisanBaseBlock getArtisanBlock(@NotNull NamespacedKey blockId) {
        return registry.get(blockId);
    }
}
