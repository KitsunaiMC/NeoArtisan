package io.github.moyusowo.neoartisan.block.crop;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.internal.ArtisanCropStorageInternal;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCropStorage;
import io.github.moyusowo.neoartisanapi.api.block.crop.CurrentCropStage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

final class ArtisanCropStorageImpl implements ArtisanCropStorage, ArtisanCropStorageInternal {

    private static ArtisanCropStorageImpl instance;

    public static ArtisanCropStorageImpl getInstance() {
        return instance;
    }

    @InitMethod
    public static void init() {
        new ArtisanCropStorageImpl();
    }

    private ArtisanCropStorageImpl() {
        instance = this;
        this.storage = new HashMap<>();
        this.lock = new ReentrantReadWriteLock();
        CropDataSerializer.load(this.storage);
        new BukkitRunnable() {
            @Override
            public void run() {
                CropDataSerializer.save();
            }
        }.runTaskLaterAsynchronously(NeoArtisan.instance(), 20L * 120);
        Bukkit.getServicesManager().register(
                ArtisanCropStorage.class,
                ArtisanCropStorageImpl.getInstance(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
        Bukkit.getServicesManager().register(
                ArtisanCropStorageInternal.class,
                ArtisanCropStorageImpl.getInstance(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private final Map<Level, Map<ChunkPos, Map<BlockPos, CurrentCropStage>>> storage;

    private final ReentrantReadWriteLock lock;

    public void replaceArtisanCrop(Level level, BlockPos blockPos, CurrentCropStage block) {
        ChunkPos chunkPos = new ChunkPos(blockPos);
        lock.writeLock().lock();
        try {
            storage.get(level).get(chunkPos).replace(blockPos, block);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void placeArtisanCrop(Level level, BlockPos blockPos, CurrentCropStage block) {
        ChunkPos chunkPos = new ChunkPos(blockPos);
        lock.writeLock().lock();
        try {
            storage.computeIfAbsent(level, k -> new HashMap<>()).computeIfAbsent(chunkPos, k -> new HashMap<>()).put(blockPos, block);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removeArtisanCrop(Level level, BlockPos blockPos) {
        ChunkPos chunkPos = new ChunkPos(blockPos);
        lock.writeLock().lock();
        try {
            storage.get(level).get(chunkPos).remove(blockPos);
            if (storage.get(level).get(chunkPos).isEmpty()) {
                storage.get(level).remove(chunkPos);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removeArtisanCrop(Level level, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        removeArtisanCrop(level, blockPos);
    }

    public void removeArtisanCrop(Block block) {
        CraftWorld world = (CraftWorld) block.getWorld();
        removeArtisanCrop(world.getHandle(), block.getX(), block.getY(), block.getZ());
    }

    public @NotNull CurrentCropStage getArtisanCropStage(Level level, BlockPos blockPos) {
        ChunkPos chunkPos = new ChunkPos(blockPos);
        lock.readLock().lock();
        try {
            CurrentCropStage currentCropStage = storage.get(level).get(chunkPos).get(blockPos);
            if (currentCropStage == null) throw new IllegalArgumentException("Please use is method to check first!");
            else return currentCropStage;
        } finally {
            lock.readLock().unlock();
        }
    }

    public CurrentCropStage getArtisanCropStage(Level level, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        return getArtisanCropStage(level, blockPos);
    }

    public CurrentCropStage getArtisanCropStage(World world, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        return getArtisanCropStage(((CraftWorld) world).getHandle(), blockPos);
    }

    public CurrentCropStage getArtisanCropStage(Block block) {
        CraftWorld world = (CraftWorld) block.getWorld();
        return getArtisanCropStage(world.getHandle(), block.getX(), block.getY(), block.getZ());
    }

    public Map<BlockPos, CurrentCropStage> getChunkArtisanCrops(Level level, ChunkPos chunkPos) {
        lock.readLock().lock();
        try {
            Map<ChunkPos, Map<BlockPos, CurrentCropStage>> levelMap = storage.getOrDefault(level, null);
            if (levelMap == null) return Collections.emptyMap();
            Map<BlockPos, CurrentCropStage> chunkMap = levelMap.getOrDefault(chunkPos, null);
            return chunkMap != null ? Map.copyOf(chunkMap) : Collections.emptyMap();
        } finally {
            lock.readLock().unlock();
        }
    }

    public Map<BlockPos, CurrentCropStage> getChunkArtisanCrops(Level level, int chunkX, int chunkZ) {
        ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
        return getChunkArtisanCrops(level, chunkPos);
    }

    public Map<ChunkPos, Map<BlockPos, CurrentCropStage>> getLevelArtisanCrops(Level level) {
        lock.readLock().lock();
        try {
            Map<ChunkPos, Map<BlockPos, CurrentCropStage>> levelMap = storage.getOrDefault(level, null);
            return levelMap != null ? Map.copyOf(levelMap) : Collections.emptyMap();
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean isArtisanCrop(Level level, BlockPos blockPos) {
        ChunkPos chunkPos = new ChunkPos(blockPos);
        lock.readLock().lock();
        try {
            return storage.containsKey(level) && storage.get(level).containsKey(chunkPos) && storage.get(level).get(chunkPos).containsKey(blockPos);
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean isArtisanCrop(Level level, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        return isArtisanCrop(level, blockPos);
    }

    public boolean isArtisanCrop(Block block) {
        CraftWorld world = (CraftWorld) block.getWorld();
        return isArtisanCrop(world.getHandle(), block.getX(), block.getY(), block.getZ());
    }

    public boolean isArtisanCrop(World world, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        return isArtisanCrop(((CraftWorld) world).getHandle(), blockPos);
    }

    public boolean hasArtisanCropInChunk(Level level, ChunkPos chunkPos) {
        lock.readLock().lock();
        try {
            return storage.containsKey(level) && storage.get(level).containsKey(chunkPos);
        } finally {
            lock.readLock().unlock();
        }
    }

    public boolean hasArtisanCropInChunk(Level level, int chunkX, int chunkZ) {
        ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
        return hasArtisanCropInChunk(level, chunkPos);
    }

    public boolean hasArtisanCropInLevel(Level level) {
        lock.readLock().lock();
        try {
            return storage.containsKey(level);
        } finally {
            lock.readLock().unlock();
        }
    }
}
