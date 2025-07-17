package io.github.moyusowo.neoartisan.block.util;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record ChunkPos(
        @NotNull UUID worldUID,
        int x,
        int z
) {
    public ChunkPos(BlockPos blockPos) {
        this(blockPos.worldUID(), blockPos.x() >> 4, blockPos.z() >> 4);
    }

    public static ChunkPos from(@NotNull Location location) {
        return new ChunkPos(BlockPos.from(location));
    }
}
