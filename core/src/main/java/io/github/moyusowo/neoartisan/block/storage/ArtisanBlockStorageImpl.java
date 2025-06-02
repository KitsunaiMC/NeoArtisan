package io.github.moyusowo.neoartisan.block.storage;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.storage.internal.ArtisanBlockStorageInternal;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.gui.ArtisanBlockGUI;
import io.github.moyusowo.neoartisanapi.api.block.storage.ArtisanBlockStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SuppressWarnings({"unused", "UnstableApiUsage"})
final class ArtisanBlockStorageImpl implements ArtisanBlockStorage, ArtisanBlockStorageInternal {

    private static ArtisanBlockStorageImpl instance;

    public static ArtisanBlockStorageImpl getInstance() {
        return instance;
    }

    @InitMethod
    public static void init() {
        new ArtisanBlockStorageImpl();
    }

    private ArtisanBlockStorageImpl() {
        instance = this;
        Bukkit.getServicesManager().register(
                ArtisanBlockStorage.class,
                ArtisanBlockStorageImpl.getInstance(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
        Bukkit.getServicesManager().register(
                ArtisanBlockStorageInternal.class,
                ArtisanBlockStorageImpl.getInstance(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
        this.storage = new HashMap<>();
        this.lock = new ReentrantReadWriteLock();
        Bukkit.getPluginManager().registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.MONITOR)
            private void onPluginEnabled(PluginEnableEvent event) {
                final Listener listener = this;
                if (event.getPlugin().getName().equals("NeoArtisan")) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            HandlerList.unregisterAll(listener);
                        }
                    }.runTaskLater(NeoArtisan.instance(), 10L);
                    new BukkitRunnable() {
                        private boolean isAllLoaded() {
                            for (Plugin otherPlugin : Bukkit.getPluginManager().getPlugins()) {
                                if (otherPlugin.getPluginMeta().getPluginDependencies().contains("NeoArtisan") ||
                                        otherPlugin.getPluginMeta().getPluginSoftDependencies().contains("NeoArtisan")) {
                                    if (!otherPlugin.isEnabled()) return false;
                                }
                            }
                            return true;
                        }
                        @Override
                        public void run() {
                            if (isAllLoaded()) {
                                BlockDataSerializer.load(ArtisanBlockStorageImpl.instance.storage);
                                cancel();
                            }
                        }
                    }.runTaskTimer(NeoArtisan.instance(), 1L, 5L);
                }
            }
        }, NeoArtisan.instance());
        new BukkitRunnable() {
            @Override
            public void run() {
                BlockDataSerializer.save();
            }
        }.runTaskLaterAsynchronously(NeoArtisan.instance(), 20L * 120);
    }

    private final Map<Level, Map<ChunkPos, Map<BlockPos, ArtisanBlockData>>> storage;

    private final ReentrantReadWriteLock lock;

    public void replaceArtisanBlock(Level level, BlockPos blockPos, ArtisanBlockData block) {
        ChunkPos chunkPos = new ChunkPos(blockPos);
        lock.writeLock().lock();
        try {
            storage.get(level).get(chunkPos).replace(blockPos, block);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void placeArtisanBlock(Level level, BlockPos blockPos, ArtisanBlockData block) {
        ChunkPos chunkPos = new ChunkPos(blockPos);
        lock.writeLock().lock();
        try {
            storage.computeIfAbsent(level, k -> new HashMap<>()).computeIfAbsent(chunkPos, k -> new HashMap<>()).put(blockPos, block);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removeArtisanBlock(Level level, BlockPos blockPos) {
        ChunkPos chunkPos = new ChunkPos(blockPos);
        lock.writeLock().lock();
        try {
            if (storage.get(level).get(chunkPos).containsKey(blockPos)) {
                ArtisanBlockGUI gui = storage.get(level).get(chunkPos).get(blockPos).getGUI();
                if (gui != null) {
                    gui.onTerminate();
                }
                storage.get(level).get(chunkPos).remove(blockPos);
            }
            if (storage.get(level).get(chunkPos).isEmpty()) {
                storage.get(level).remove(chunkPos);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removeArtisanBlock(Level level, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        removeArtisanBlock(level, blockPos);
    }

    public void removeArtisanBlock(Block block) {
        CraftWorld world = (CraftWorld) block.getWorld();
        removeArtisanBlock(world.getHandle(), block.getX(), block.getY(), block.getZ());
    }

    public @NotNull ArtisanBlockData getArtisanBlock(Level level, BlockPos blockPos) {
        ChunkPos chunkPos = new ChunkPos(blockPos);
        lock.readLock().lock();
        try {
            ArtisanBlockData artisanBlockData = storage.get(level).get(chunkPos).get(blockPos);
            if (artisanBlockData == null) throw new IllegalArgumentException("Please use is method to check first!");
            else return artisanBlockData;
        } finally {
            lock.readLock().unlock();
        }
    }

    public ArtisanBlockData getArtisanBlock(Level level, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        return getArtisanBlock(level, blockPos);
    }

    @Override
    public ArtisanBlockData getArtisanBlockData(@NotNull World world, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        return getArtisanBlock(((CraftWorld) world).getHandle(), blockPos);
    }

    @Override
    public ArtisanBlockData getArtisanBlockData(@NotNull Block block) {
        CraftWorld world = (CraftWorld) block.getWorld();
        return getArtisanBlock(world.getHandle(), block.getX(), block.getY(), block.getZ());
    }

    public Map<BlockPos, ArtisanBlockData> getChunkArtisanBlocks(Level level, ChunkPos chunkPos) {
        lock.readLock().lock();
        try {
            Map<ChunkPos, Map<BlockPos, ArtisanBlockData>> levelMap = storage.getOrDefault(level, null);
            if (levelMap == null) return Collections.emptyMap();
            Map<BlockPos, ArtisanBlockData> chunkMap = levelMap.getOrDefault(chunkPos, null);
            return chunkMap != null ? Map.copyOf(chunkMap) : Collections.emptyMap();
        } finally {
            lock.readLock().unlock();
        }
    }

    public Map<BlockPos, ArtisanBlockData> getChunkArtisanBlocks(Level level, int chunkX, int chunkZ) {
        ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
        return getChunkArtisanBlocks(level, chunkPos);
    }

    public Map<ChunkPos, Map<BlockPos, ArtisanBlockData>> getLevelArtisanBlocks(Level level) {
        lock.readLock().lock();
        try {
            Map<ChunkPos, Map<BlockPos, ArtisanBlockData>> levelMap = storage.getOrDefault(level, null);
            return levelMap != null ? Map.copyOf(levelMap) : Collections.emptyMap();
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean isArtisanBlock(Level level, BlockPos blockPos) {
        ChunkPos chunkPos = new ChunkPos(blockPos);
        lock.readLock().lock();
        try {
            return storage.containsKey(level) && storage.get(level).containsKey(chunkPos) && storage.get(level).get(chunkPos).containsKey(blockPos);
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean isArtisanBlock(Level level, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        return isArtisanBlock(level, blockPos);
    }

    @Override
    public boolean isArtisanBlock(@NotNull Block block) {
        CraftWorld world = (CraftWorld) block.getWorld();
        return isArtisanBlock(world.getHandle(), block.getX(), block.getY(), block.getZ());
    }

    @Override
    public boolean isArtisanBlock(@NotNull World world, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        return isArtisanBlock(((CraftWorld) world).getHandle(), blockPos);
    }

    public boolean hasArtisanBlockInChunk(Level level, ChunkPos chunkPos) {
        lock.readLock().lock();
        try {
            return storage.containsKey(level) && storage.get(level).containsKey(chunkPos);
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean hasArtisanBlockInChunk(Level level, int chunkX, int chunkZ) {
        ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
        return hasArtisanBlockInChunk(level, chunkPos);
    }

    public boolean hasArtisanBlockInLevel(Level level) {
        lock.readLock().lock();
        try {
            return storage.containsKey(level);
        } finally {
            lock.readLock().unlock();
        }
    }
}
