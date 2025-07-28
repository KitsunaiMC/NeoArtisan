package io.github.moyusowo.neoartisanapi.api.block.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Block protection check service, used to verify player interaction permissions at specified locations.
 * <p>
 * This service is a <b>core security mechanism</b>. All operations involving block breaking,
 * placing, and interaction must go through this interface for permission verification.
 * Built-in implementations integrate with major protection plugins (such as WorldGuard,
 * GriefPrevention) to ensure compatibility with the server ecosystem.
 * </p>
 *
 * <p><b>Add-on plugin development requirements:</b></p>
 * <ol>
 *   <li><strong>Must</strong> call this interface method for permission checking before
 *       implementing any block operation logic</li>
 *   <li>Ignoring permission checks will cause:<br>
 *       - Conflicts with other protection plugins<br>
 *       - Players bypassing territory permissions<br>
 *       - Security vulnerabilities</li>
 * </ol>
 *
 * @see io.github.moyusowo.neoartisanapi.api.block.protection.Protections#BLOCK Get service instance
 */
public interface ArtisanBlockProtection {
    /**
     * Checks if a player can break a block at the specified location
     *
     * @param player   the operating player (non-null)
     * @param location the block location (non-null)
     * @return true if the player has break permission, false otherwise
     *
     * @apiNote Add-on plugins must call this method when implementing break logic
     */
    boolean canBreak(@NotNull Player player, @NotNull Location location);

    /**
     * Checks if a player can place a block at the specified location
     *
     * @param player   the operating player (non-null)
     * @param location the target location (non-null)
     * @return true if the player has place permission, false otherwise
     *
     * @apiNote This permission must be verified before block placement events
     */
    boolean canPlace(@NotNull Player player, @NotNull Location location);

    /**
     * Checks if a player can interact with a block at the specified location
     * (such as opening containers)
     *
     * @param player   the operating player (non-null)
     * @param location the block location (non-null)
     * @return true if the player has interaction permission, false otherwise
     *
     * @apiNote Suitable for container interaction permission verification such as chests,
     *          furnaces, or custom GUIs
     */
    boolean canInteract(@NotNull Player player, @NotNull Location location);
}
