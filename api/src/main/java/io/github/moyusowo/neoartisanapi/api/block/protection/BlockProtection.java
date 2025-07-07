package io.github.moyusowo.neoartisanapi.api.block.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface BlockProtection {

    boolean canBreak(@NotNull Player player, @NotNull Location location);

    boolean canPlace(@NotNull Player player, @NotNull Location location);

    boolean canInteract(@NotNull Player player, @NotNull Location location);
}
