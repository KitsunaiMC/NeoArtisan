package io.github.moyusowo.neoartisan.block.util;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public record BlockPos(
        @NotNull UUID worldUID,
        int x,
        int y,
        int z
) {
    public static BlockPos from(@NotNull Location location) {
        return new BlockPos(location.getWorld().getUID(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }
}
