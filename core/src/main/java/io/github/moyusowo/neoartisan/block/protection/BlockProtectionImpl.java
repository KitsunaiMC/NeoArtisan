package io.github.moyusowo.neoartisan.block.protection;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.protection.ArtisanBlockProtection;
import net.momirealms.antigrieflib.AntiGriefLib;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

final class BlockProtectionImpl implements ArtisanBlockProtection {
    @InitMethod(priority = InitPriority.DEFAULT)
    static void init() {
        Bukkit.getServicesManager().register(
                ArtisanBlockProtection.class,
                new BlockProtectionImpl(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private BlockProtectionImpl() {
        antiGriefLib = AntiGriefLib.builder(NeoArtisan.instance()).silentLogs(true).ignoreOP(true).build();
    }

    private final AntiGriefLib antiGriefLib;

    @Override
    public boolean canBreak(@NotNull Player player, @NotNull Location location) {
        return antiGriefLib.canBreak(player, location);
    }

    @Override
    public boolean canPlace(@NotNull Player player, @NotNull Location location) {
        return antiGriefLib.canPlace(player, location);
    }

    @Override
    public boolean canInteract(@NotNull Player player, @NotNull Location location) {
        return antiGriefLib.canInteract(player, location);
    }
}
