package io.github.moyusowo.neoartisan.block.storage.internal;

import io.github.moyusowo.neoartisan.block.util.BlockPos;
import io.github.moyusowo.neoartisan.util.multithread.SpecificThreadUse;
import io.github.moyusowo.neoartisan.util.multithread.Threads;
import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;

@SpecificThreadUse(thread = Threads.MAIN)
public interface ArtisanBlockStorageInternal {

    static ArtisanBlockStorageInternal getInternal() {
        return Bukkit.getServicesManager().load(ArtisanBlockStorageInternal.class);
    }

    void replaceArtisanBlock(@NotNull ArtisanBlockData data);

    void placeArtisanBlock(@NotNull ArtisanBlockData data);

    void removeArtisanBlock(@NotNull BlockPos blockPos);

    default void removeArtisanBlock(@NotNull Block block) {
        removeArtisanBlock(BlockPos.from(block.getLocation()));
    }

    @NotNull
    ArtisanBlockData getArtisanBlockData(@NotNull BlockPos blockPos);
}
