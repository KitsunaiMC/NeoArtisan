package io.github.moyusowo.neoartisan.block.storage.internal;

import io.github.moyusowo.neoartisan.block.util.BlockPos;
import io.github.moyusowo.neoartisan.block.util.ChunkPos;
import io.github.moyusowo.neoartisan.util.multithread.SpecificThreadUse;
import io.github.moyusowo.neoartisan.util.multithread.Threads;
import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.UUID;

@ApiStatus.Internal
public interface ArtisanBlockStorageInternal {

    static ArtisanBlockStorageInternal getInternal() {
        return Bukkit.getServicesManager().load(ArtisanBlockStorageInternal.class);
    }

    @SpecificThreadUse(thread = Threads.MAIN)
    void replaceArtisanBlock(UUID worldUUID, BlockPos blockPos, ArtisanBlockData block);

    @SpecificThreadUse(thread = Threads.MAIN)
    void placeArtisanBlock(UUID worldUUID, BlockPos blockPos, ArtisanBlockData block);

    @SpecificThreadUse(thread = Threads.MAIN)
    void removeArtisanBlock(UUID worldUUID, BlockPos blockPos);

    @SpecificThreadUse(thread = Threads.MAIN)
    void removeArtisanBlock(UUID worldUUID, int x, int y, int z);

    @SpecificThreadUse(thread = Threads.MAIN)
    void removeArtisanBlock(Block block);

    @NotNull
    ArtisanBlockData getArtisanBlock(UUID worldUID, BlockPos blockPos);

    ArtisanBlockData getArtisanBlock(UUID worldUID, int x, int y, int z);

    Map<BlockPos, ArtisanBlockData> getChunkArtisanBlocks(UUID worldUID, ChunkPos chunkPos);

    Map<BlockPos, ArtisanBlockData> getChunkArtisanBlocks(UUID worldUID, int chunkX, int chunkZ);

    Map<ChunkPos, Map<BlockPos, ArtisanBlockData>> getLevelArtisanBlocks(UUID worldUID);

    boolean isArtisanBlock(UUID worldUID, BlockPos blockPos);

    boolean isArtisanBlock(UUID worldUID, int x, int y, int z);

    boolean hasArtisanBlockInChunk(UUID worldUID, ChunkPos chunkPos);

    boolean hasArtisanBlockInChunk(UUID worldUID, int chunkX, int chunkZ);

    boolean hasArtisanBlockInLevel(UUID worldUID);
}
