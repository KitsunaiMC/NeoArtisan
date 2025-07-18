package io.github.moyusowo.neoartisan.block.data.entity;

import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Marker;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

public final class BlockEntityManager {
    private BlockEntityManager() {}

    private static final String TAG = "NeoArtisan";

    @InitMethod
    static void init() {
        for (World world : Bukkit.getWorlds()) {
            for (Marker marker : world.getEntitiesByClass(Marker.class)) {
                if (marker.getScoreboardTags().contains(TAG) && !NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(marker.getLocation())) {
                    marker.remove();
                }
            }
        }
    }

    @NotNull
    private static Marker spawn(@NotNull Location location) {
        Marker marker = location.getWorld().spawn(location.toBlockLocation(), Marker.class);
        marker.setPersistent(true);
        marker.addScoreboardTag(TAG);
        return marker;
    }

    public static boolean blockLocationEquals(@NotNull Location p1, @NotNull Location p2) {
        return p1.getBlockX() == p2.getBlockX() && p1.getBlockY() == p2.getBlockY() && p1.getBlockZ() == p2.getBlockZ();
    }

    public static void remove(@NotNull Location location) {
        for (Marker marker : location.getNearbyEntitiesByType(Marker.class, 1.5)) {
            if (marker.getScoreboardTags().contains(TAG) && blockLocationEquals(marker.getLocation(), location)) {
                marker.remove();
            }
        }
    }

    @NotNull
    public static PersistentDataContainer getPDC(@NotNull Location location) {
        for (Marker marker : location.getNearbyEntitiesByType(Marker.class, 1.5)) {
            if (marker.getScoreboardTags().contains(TAG) && blockLocationEquals(marker.getLocation(), location)) {
                return marker.getPersistentDataContainer();
            }
        }
        return spawn(location).getPersistentDataContainer();
    }
}
