package io.github.moyusowo.neoartisan.block.data;

import org.bukkit.Location;
import org.bukkit.entity.Marker;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class BlockEntityManager {
    private BlockEntityManager() {}

    private static final String TAG = "NeoArtisan";

    private static String toString(@NotNull Location location) {
        return "location: " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ();
    }

    public static void spawn(@NotNull Location location) {
        Marker marker = location.getWorld().spawn(location.toBlockLocation(), Marker.class);
        marker.setPersistent(true);
        marker.addScoreboardTag(TAG);
        marker.addScoreboardTag(toString(location));
    }

    public static void remove(@NotNull Location location) {
        for (Marker marker : location.getNearbyEntitiesByType(Marker.class, 1.5)) {
            if (marker.getScoreboardTags().contains(TAG) && marker.getScoreboardTags().contains(toString(location))) {
                marker.remove();
            }
        }
    }

    @Nullable
    public static PersistentDataContainer getPDC(@NotNull Location location) {
        for (Marker marker : location.getNearbyEntitiesByType(Marker.class, 1.5)) {
            if (marker.getScoreboardTags().contains(TAG) && marker.getScoreboardTags().contains(toString(location))) {
                return marker.getPersistentDataContainer();
            }
        }
        return null;
    }
}
