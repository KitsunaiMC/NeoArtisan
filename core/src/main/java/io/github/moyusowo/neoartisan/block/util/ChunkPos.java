package io.github.moyusowo.neoartisan.block.util;

public record ChunkPos(
        int x,
        int z
) {
    public ChunkPos(BlockPos blockPos) {
        this(blockPos.x() >> 4, blockPos.z() >> 4);
    }
}
