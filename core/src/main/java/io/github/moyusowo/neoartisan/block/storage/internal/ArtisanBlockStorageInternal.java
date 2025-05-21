package io.github.moyusowo.neoartisan.block.storage.internal;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockData;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@SuppressWarnings("unused")
@ApiStatus.Internal
public interface ArtisanBlockStorageInternal {

    static ArtisanBlockStorageInternal getInternal() {
        return Bukkit.getServicesManager().load(ArtisanBlockStorageInternal.class);
    }

    void replaceArtisanBlock(Level level, BlockPos blockPos, ArtisanBlockData block);

    void placeArtisanBlock(Level level, BlockPos blockPos, ArtisanBlockData block);

    void removeArtisanBlock(Level level, BlockPos blockPos);

    void removeArtisanBlock(Level level, int x, int y, int z);

    void removeArtisanBlock(Block block);

    @NotNull
    ArtisanBlockData getArtisanBlock(Level level, BlockPos blockPos);

    ArtisanBlockData getArtisanBlock(Level level, int x, int y, int z);

    Map<BlockPos, ArtisanBlockData> getChunkArtisanBlocks(Level level, ChunkPos chunkPos);

    Map<BlockPos, ArtisanBlockData> getChunkArtisanBlocks(Level level, int chunkX, int chunkZ);

    Map<ChunkPos, Map<BlockPos, ArtisanBlockData>> getLevelArtisanBlocks(Level level);

    boolean isArtisanBlock(Level level, BlockPos blockPos);

    boolean isArtisanBlock(Level level, int x, int y, int z);

    boolean hasArtisanBlockInChunk(Level level, ChunkPos chunkPos);

    boolean hasArtisanBlockInChunk(Level level, int chunkX, int chunkZ);

    boolean hasArtisanBlockInLevel(Level level);
}
