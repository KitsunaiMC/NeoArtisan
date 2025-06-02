package io.github.moyusowo.neoartisan.block.util;

import io.github.moyusowo.neoartisan.NeoArtisan;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

public final class BlockStateUtil {

    private BlockStateUtil() {}

    public static BlockState stateById(int id) {
        return Block.stateById(id);
    }
}
