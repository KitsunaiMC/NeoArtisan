package io.github.moyusowo.neoartisan.block.network;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.netty.channel.Channel;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class AddPipeline implements Listener {

    private static final String INJECTED = "block_packet_neoartisan_handler";

    private AddPipeline() {}

    @InitMethod(priority = InitPriority.LISTENER)
    static void init() {
        NeoArtisan.registerListener(new AddPipeline());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        final CraftPlayer craftPlayer = (CraftPlayer) player;
        final ServerPlayer serverPlayer = craftPlayer.getHandle();
        final Channel channel = serverPlayer.connection.connection.channel;
        channel.eventLoop().execute(() -> {
            if (channel.pipeline().get(INJECTED) == null) {
                channel.pipeline().addBefore(
                        "packet_handler", INJECTED, new BlockPacketHandler(serverPlayer)
                );
            }
        });
    }
}
