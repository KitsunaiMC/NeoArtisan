package io.github.moyusowo.neoartisan.block.protection;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.protection.BlockProtection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

final class BlockProtectionImpl implements BlockProtection {
    @InitMethod(priority = InitPriority.DEFAULT)
    static void init() {
        Bukkit.getServicesManager().register(
                BlockProtection.class,
                new BlockProtectionImpl(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private BlockProtectionImpl() {}

    @Override
    public boolean canBreak(@NotNull Player player, @NotNull Location location) {
        return NeoArtisan.getAntiGriefLib().canBreak(player, location);
    }

    @Override
    public boolean canPlace(@NotNull Player player, @NotNull Location location) {
        return NeoArtisan.getAntiGriefLib().canPlace(player, location);
    }

    @Override
    public boolean canInteract(@NotNull Player player, @NotNull Location location) {
        return NeoArtisan.getAntiGriefLib().canInteract(player, location);
    }
}
