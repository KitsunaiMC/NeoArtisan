package io.github.moyusowo.neoartisan.block.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

public class BoundingBoxUtil {

    public static boolean overlap(@NotNull final Block block) {
        final Location location = block.getLocation().add(0.5, 0.5, 0.5);
        for (Entity entity : location.getWorld().getNearbyEntities(location, 1.5, 1.5, 1.5)) {
            if (entity.getBoundingBox().overlaps(BoundingBox.of(block))) {
                return true;
            }
        }
        return false;
    }
}
