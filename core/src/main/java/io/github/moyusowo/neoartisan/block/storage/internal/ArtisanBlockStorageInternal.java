package io.github.moyusowo.neoartisan.block.storage.internal;

import io.github.moyusowo.neoartisanapi.api.block.ArtisanBlockState;
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

    void replaceArtisanBlock(Level level, BlockPos blockPos, ArtisanBlockState block);

    void placeArtisanBlock(Level level, BlockPos blockPos, ArtisanBlockState block);

    void removeArtisanBlock(Level level, BlockPos blockPos);

    void removeArtisanBlock(Level level, int x, int y, int z);

    void removeArtisanBlock(Block block);

    @NotNull
    ArtisanBlockState getArtisanBlock(Level level, BlockPos blockPos);

    ArtisanBlockState getArtisanBlock(Level level, int x, int y, int z);

    Map<BlockPos, ArtisanBlockState> getChunkArtisanBlocks(Level level, ChunkPos chunkPos);

    Map<BlockPos, ArtisanBlockState> getChunkArtisanBlocks(Level level, int chunkX, int chunkZ);

    Map<ChunkPos, Map<BlockPos, ArtisanBlockState>> getLevelArtisanBlocks(Level level);

    boolean isArtisanBlock(Level level, BlockPos blockPos);

    boolean isArtisanBlock(Level level, int x, int y, int z);

    boolean hasArtisanBlockInChunk(Level level, ChunkPos chunkPos);

    boolean hasArtisanBlockInChunk(Level level, int chunkX, int chunkZ);

    boolean hasArtisanBlockInLevel(Level level);
}
