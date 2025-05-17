package io.github.moyusowo.neoartisan.block.storage;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.storage.internal.ArtisanBlockStorageInternal;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisanapi.api.block.ArtisanBlockState;
import io.github.moyusowo.neoartisanapi.api.block.crop.CurrentCropStage;
import io.github.moyusowo.neoartisanapi.api.block.storage.ArtisanBlockStorage;
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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@SuppressWarnings("unused")
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
        this.storage = new HashMap<>();
        this.lock = new ReentrantReadWriteLock();
        BlockDataSerializer.load(this.storage);
        new BukkitRunnable() {
            @Override
            public void run() {
                BlockDataSerializer.save();
            }
        }.runTaskLaterAsynchronously(NeoArtisan.instance(), 20L * 120);
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
    }

    private final Map<Level, Map<ChunkPos, Map<BlockPos, ArtisanBlockState>>> storage;

    private final ReentrantReadWriteLock lock;

    public void replaceArtisanBlock(Level level, BlockPos blockPos, ArtisanBlockState block) {
        ChunkPos chunkPos = new ChunkPos(blockPos);
        lock.writeLock().lock();
        try {
            storage.get(level).get(chunkPos).replace(blockPos, block);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void placeArtisanBlock(Level level, BlockPos blockPos, ArtisanBlockState block) {
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
            storage.get(level).get(chunkPos).remove(blockPos);
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

    public @NotNull ArtisanBlockState getArtisanBlock(Level level, BlockPos blockPos) {
        ChunkPos chunkPos = new ChunkPos(blockPos);
        lock.readLock().lock();
        try {
            ArtisanBlockState artisanBlockState = storage.get(level).get(chunkPos).get(blockPos);
            if (artisanBlockState == null) throw new IllegalArgumentException("Please use is method to check first!");
            else return artisanBlockState;
        } finally {
            lock.readLock().unlock();
        }
    }

    public ArtisanBlockState getArtisanBlock(Level level, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        return getArtisanBlock(level, blockPos);
    }

    @Override
    public ArtisanBlockState getArtisanBlock(World world, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(x, y, z);
        return getArtisanBlock(((CraftWorld) world).getHandle(), blockPos);
    }

    @Override
    public ArtisanBlockState getArtisanBlock(Block block) {
        CraftWorld world = (CraftWorld) block.getWorld();
        return getArtisanBlock(world.getHandle(), block.getX(), block.getY(), block.getZ());
    }

    public Map<BlockPos, ArtisanBlockState> getChunkArtisanBlocks(Level level, ChunkPos chunkPos) {
        lock.readLock().lock();
        try {
            Map<ChunkPos, Map<BlockPos, ArtisanBlockState>> levelMap = storage.getOrDefault(level, null);
            if (levelMap == null) return Collections.emptyMap();
            Map<BlockPos, ArtisanBlockState> chunkMap = levelMap.getOrDefault(chunkPos, null);
            return chunkMap != null ? Map.copyOf(chunkMap) : Collections.emptyMap();
        } finally {
            lock.readLock().unlock();
        }
    }

    public Map<BlockPos, ArtisanBlockState> getChunkArtisanBlocks(Level level, int chunkX, int chunkZ) {
        ChunkPos chunkPos = new ChunkPos(chunkX, chunkZ);
        return getChunkArtisanBlocks(level, chunkPos);
    }

    public Map<ChunkPos, Map<BlockPos, ArtisanBlockState>> getLevelArtisanBlocks(Level level) {
        lock.readLock().lock();
        try {
            Map<ChunkPos, Map<BlockPos, ArtisanBlockState>> levelMap = storage.getOrDefault(level, null);
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
    public boolean isArtisanBlock(Block block) {
        CraftWorld world = (CraftWorld) block.getWorld();
        return isArtisanBlock(world.getHandle(), block.getX(), block.getY(), block.getZ());
    }

    @Override
    public boolean isArtisanBlock(World world, int x, int y, int z) {
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
