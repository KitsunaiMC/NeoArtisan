package io.github.moyusowo.neoartisan.block.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class BlockStateUtil {

    private BlockStateUtil() {}

    public static BlockState stateById(int id) {
        return Block.stateById(id);
    }
}
