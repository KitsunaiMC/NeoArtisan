package io.github.moyusowo.neoartisan.block.network;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

final class AsyncPlayerWorld implements Listener {
    private static final Map<UUID, UUID> playerWorld = new ConcurrentHashMap<>();

    private AsyncPlayerWorld() {}

    @InitMethod(priority = InitPriority.LISTENER)
    static void init() {
        NeoArtisan.registerListener(new AsyncPlayerWorld());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        playerWorld.put(event.getPlayer().getUniqueId(), event.getPlayer().getWorld().getUID());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerWorld.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        playerWorld.replace(event.getPlayer().getUniqueId(), event.getPlayer().getWorld().getUID());
    }

    public static @NotNull UUID getPlayerWorld(UUID playerUUID) {
        return playerWorld.get(playerUUID);
    }
}
