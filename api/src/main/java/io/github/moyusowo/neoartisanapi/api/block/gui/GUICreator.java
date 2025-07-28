package io.github.moyusowo.neoartisanapi.api.block.gui;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * GUI creation functional interface, used to create GUIs lazily when blocks are placed
 * <p>
 * <b>Usage flow:</b>
 * <ol>
 *   <li>Addon plugins create GUICreator instances when registering blocks</li>
 *   <li>GUICreator implementations capture fixed parameters (size/title, etc.)</li>
 *   <li>Framework calls create() with Location when blocks are placed</li>
 * </ol>
 *
 */
@FunctionalInterface
public interface GUICreator {
    /**
     * Creates a GUI instance at the specified location
     *
     * @param location the block location to bind the GUI to
     * @return the new GUI instance (not null)
     */
    @NotNull
    ArtisanBlockGUI create(Location location);
}
