package io.github.moyusowo.neoartisan.block.storage;

import com.google.common.collect.HashMultimap;
import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.data.ArtisanBlockDataView;
import io.github.moyusowo.neoartisan.block.storage.internal.ArtisanBlockStorageAsync;
import io.github.moyusowo.neoartisan.block.storage.internal.ArtisanBlockStorageInternal;
import io.github.moyusowo.neoartisan.block.task.LifecycleTaskManagerInternal;
import io.github.moyusowo.neoartisan.block.util.BlockPos;
import io.github.moyusowo.neoartisan.block.util.ChunkPos;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisan.util.multithread.SpecificThreadUse;
import io.github.moyusowo.neoartisan.util.multithread.Threads;
import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.storage.ArtisanBlockStorage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

final class ArtisanBlockStorageImpl implements ArtisanBlockStorage, ArtisanBlockStorageInternal, ArtisanBlockStorageAsync {

    private static ArtisanBlockStorageImpl instance;

    public static ArtisanBlockStorageImpl getInstance() {
        return instance;
    }

    @InitMethod(priority = InitPriority.STORAGE_INIT)
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
        Bukkit.getServicesManager().register(
                ArtisanBlockStorageAsync.class,
                ArtisanBlockStorageImpl.getInstance(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
        new BukkitRunnable() {
            @Override
            public void run() {
                BlockDataSerializer.save();
            }
        }.runTaskTimerAsynchronously(NeoArtisan.instance(), 20L * 30, 20L * 15);
    }

    private final Map<BlockPos, ArtisanBlockData> blockStorage = new HashMap<>();
    private final HashMultimap<ChunkPos, BlockPos> chunkStorage = HashMultimap.create();
    private final HashMultimap<UUID, ChunkPos> worldStorage = HashMultimap.create();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ConcurrentHashMap<ChunkPos, Boolean> dirtyChunkMarker = new ConcurrentHashMap<>();

    @Override
    @SpecificThreadUse(thread = Threads.MAIN)
    public void replaceArtisanBlock(@NotNull ArtisanBlockData newData) {
        BlockPos blockPos = BlockPos.from(newData.getLocation());
        ChunkPos chunkPos = new ChunkPos(blockPos);
        ((LifecycleTaskManagerInternal) blockStorage.get(blockPos).getLifecycleTaskManager()).runTerminate(newData.getLocation());
        lock.writeLock().lock();
        try {
            blockStorage.replace(blockPos, newData);
        } finally {
            lock.writeLock().unlock();
            dirtyChunkMarker.putIfAbsent(chunkPos, true);
            ((LifecycleTaskManagerInternal) newData.getLifecycleTaskManager()).runInit(newData.getLocation());
        }
    }

    @Override
    @SpecificThreadUse(thread = Threads.MAIN)
    public void placeArtisanBlock(@NotNull ArtisanBlockData data) {
        BlockPos blockPos = BlockPos.from(data.getLocation());
        ChunkPos chunkPos = new ChunkPos(blockPos);
        lock.writeLock().lock();
        try {
            blockStorage.put(blockPos, data);
            if (!chunkStorage.containsEntry(chunkPos, blockPos)) chunkStorage.put(chunkPos, blockPos);
            if (!worldStorage.containsEntry(blockPos.worldUID(), chunkPos)) worldStorage.put(blockPos.worldUID(), chunkPos);
        } finally {
            lock.writeLock().unlock();
            dirtyChunkMarker.putIfAbsent(chunkPos, true);
            ((LifecycleTaskManagerInternal) data.getLifecycleTaskManager()).runInit(data.getLocation());
        }
    }

    @Override
    @SpecificThreadUse(thread = Threads.MAIN)
    public void removeArtisanBlock(@NotNull BlockPos blockPos) {
        ArtisanBlockData data = blockStorage.get(blockPos);
        ((LifecycleTaskManagerInternal) data.getLifecycleTaskManager()).runTerminate(data.getLocation());
        ChunkPos chunkPos = new ChunkPos(blockPos);
        lock.writeLock().lock();
        try {
            blockStorage.remove(blockPos);
            chunkStorage.remove(chunkPos, blockPos);
        } finally {
            lock.writeLock().unlock();
            dirtyChunkMarker.putIfAbsent(chunkPos, true);
        }
    }

    @Override
    @NotNull
    @SpecificThreadUse(thread = Threads.STRORAGE)
    public ArtisanBlockDataView getArtisanBlockDataView(@NotNull BlockPos blockPos) {
        lock.readLock().lock();
        try {
            return ArtisanBlockDataView.from(Objects.requireNonNull(blockStorage.get(blockPos), "Please check before get!"));
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    @NotNull
    @SpecificThreadUse(thread = Threads.MAIN)
    public ArtisanBlockData getArtisanBlockData(@NotNull BlockPos blockPos) {
        lock.readLock().lock();
        try {
            return Objects.requireNonNull(blockStorage.get(blockPos), "Please check before get!");
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    @NotNull
    @SpecificThreadUse(thread = Threads.MAIN)
    public ArtisanBlockData getArtisanBlockData(@NotNull World world, int x, int y, int z) {
        BlockPos blockPos = new BlockPos(world.getUID(), x, y, z);
        return getArtisanBlockData(blockPos);
    }

    @Override
    @SpecificThreadUse(thread = Threads.MAIN)
    public boolean isArtisanBlock(@NotNull World world, int x, int y, int z) {
        return isArtisanBlock(new BlockPos(world.getUID(), x, y, z));
    }

    @Override
    @SpecificThreadUse(thread = Threads.MAIN)
    public void setArtisanBlockData(@NotNull World world, int x, int y, int z, @Nullable ArtisanBlockData artisanBlockData) {
        if (isArtisanBlock(world, x, y, z)) {
            if (artisanBlockData == null) {
                removeArtisanBlock(new BlockPos(world.getUID(), x, y, z));
            } else {
                replaceArtisanBlock(artisanBlockData);
            }
        } else {
            if (artisanBlockData == null) {
                world.getBlockAt(x, y, z).setType(Material.AIR);
            } else {
                placeArtisanBlock(artisanBlockData);
            }
        }
    }

    @SpecificThreadUse(thread = Threads.STRORAGE)
    @NotNull
    @UnmodifiableView
    public List<ArtisanBlockDataView> getChunkArtisanBlockDataViews(ChunkPos chunkPos) {
        List<ArtisanBlockDataView> views = new ArrayList<>();
        lock.readLock().lock();
        try {
            if (!chunkStorage.containsKey(chunkPos)) return List.of();
            chunkStorage.get(chunkPos).forEach(
                    blockPos -> views.add(ArtisanBlockDataView.from(blockStorage.get(blockPos)))
            );
            return Collections.unmodifiableList(views);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    @SpecificThreadUse(thread = Threads.STRORAGE)
    public boolean isArtisanBlock(BlockPos blockPos) {
        lock.readLock().lock();
        try {
            return blockStorage.containsKey(blockPos);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    @SpecificThreadUse(thread = Threads.STRORAGE)
    public boolean hasArtisanBlockInWorld(UUID worldUID) {
        lock.readLock().lock();
        try {
            return worldStorage.containsKey(worldUID);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    @SpecificThreadUse(thread = Threads.STRORAGE)
    public boolean hasArtisanBlockInChunk(ChunkPos chunkPos) {
        lock.readLock().lock();
        try {
            return chunkStorage.containsKey(chunkPos);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public boolean checkAndCleanDirtyChunk(ChunkPos chunkPos) {
        return dirtyChunkMarker.remove(chunkPos, true);
    }

    @SpecificThreadUse(thread = Threads.STRORAGE)
    @NotNull
    @UnmodifiableView
    public List<ChunkPos> getWorldArtisanBlockChunks(UUID worldUID) {
        lock.readLock().lock();
        try {
            if (!worldStorage.containsKey(worldUID)) return List.of();
            return List.copyOf(worldStorage.get(worldUID));
        } finally {
            lock.readLock().unlock();
        }
    }
}
