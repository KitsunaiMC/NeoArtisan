package io.github.moyusowo.neoartisan.block.util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.BoundingBox;
import org.jetbrains.annotations.NotNull;

public class BoundingBoxUtil {

    public static boolean overlap(@NotNull final Block block) {
        final Location location = block.getLocation().add(0.5, 0.5, 0.5);
        for (LivingEntity livingEntity : location.getWorld().getNearbyLivingEntities(location, 1.5, 1.5, 1.5)) {
            if (livingEntity.isCollidable() && livingEntity.getBoundingBox().overlaps(BoundingBox.of(block))) {
                return true;
            }
        }
        return false;
    }
}
