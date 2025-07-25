package io.github.moyusowo.neoartisan.block.data.entity;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.util.BlockPos;
import io.github.moyusowo.neoartisan.util.init.InitMethod;

import io.github.moyusowo.neoartisanapi.api.block.storage.Storages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Marker;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public final class BlockEntityManager {
    private BlockEntityManager() {}

    private static final String TAG = "NeoArtisan";

    private static final Map<BlockPos, Marker> cached = new HashMap<>();

    @InitMethod
    static void init() {
        for (World world : Bukkit.getWorlds()) {
            for (Marker marker : world.getEntitiesByClass(Marker.class)) {
                if (marker.getScoreboardTags().contains(TAG)) {
                    if (Storages.BLOCK.isArtisanBlock(marker.getLocation()) && Storages.BLOCK.getArtisanBlockData(marker.getLocation()).getArtisanBlock().hasBlockEntity()) {
                        cached.put(BlockPos.from(marker.getLocation()), marker);
                    } else {
                        marker.remove();
                    }
                }
            }
        }
        NeoArtisan.logger().info("successfully loaded block entity.");
    }

    @NotNull
    private static Marker spawn(@NotNull Location location) {
        Marker marker = location.getWorld().spawn(location.toBlockLocation(), Marker.class);
        marker.setPersistent(true);
        marker.addScoreboardTag(TAG);
        return marker;
    }

    public static void remove(@NotNull Location location) {
        final BlockPos blockPos = BlockPos.from(location);
        if (cached.containsKey(blockPos)) {
            Marker marker = cached.remove(blockPos);
            marker.remove();
        }
    }

    @NotNull
    public static PersistentDataContainer getPDC(@NotNull Location location) {
        final BlockPos blockPos = BlockPos.from(location);
        if (cached.containsKey(blockPos)) {
            return cached.get(blockPos).getPersistentDataContainer();
        } else {
            Marker marker = spawn(location);
            cached.put(blockPos, marker);
            return marker.getPersistentDataContainer();
        }
    }
}
