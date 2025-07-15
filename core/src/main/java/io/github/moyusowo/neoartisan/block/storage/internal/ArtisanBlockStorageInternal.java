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
import java.util.UUID;

@SuppressWarnings("unused")
@ApiStatus.Internal
public interface ArtisanBlockStorageInternal {

    static ArtisanBlockStorageInternal getInternal() {
        return Bukkit.getServicesManager().load(ArtisanBlockStorageInternal.class);
    }

    void replaceArtisanBlock(UUID worldUUID, BlockPos blockPos, ArtisanBlockData block);

    void placeArtisanBlock(UUID worldUUID, BlockPos blockPos, ArtisanBlockData block);

    void removeArtisanBlock(UUID worldUUID, BlockPos blockPos);

    void removeArtisanBlock(UUID worldUUID, int x, int y, int z);

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
