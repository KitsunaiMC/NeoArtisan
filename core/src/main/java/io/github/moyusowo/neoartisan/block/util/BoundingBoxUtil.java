package io.github.moyusowo.neoartisan.block.util;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

public class BoundingBoxUtil {

    public static boolean overlap(@NotNull final Player player, @NotNull final Block block) {
        return player.getBoundingBox().overlaps(BoundingBox.of(block));
    }
}
