package io.github.moyusowo.neoartisanapi.api.registry;

import io.github.moyusowo.neoartisanapi.api.block.block.base.ArtisanBaseBlock;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Custom block registry, responsible for managing registration and lookup of all {@link ArtisanBaseBlock} instances.
 * <p>
 * This registry can be accessed through {@link io.github.moyusowo.neoartisanapi.api.registry.Registries#BLOCK}.
 * All registration operations must be performed during the plugin's [onEnable](file://E:\code\NeoArtisan\core\src\main\java\io\github\moyusowo\neoartisan\NeoArtisan.java#L77-L86) method.
 * </p>
 *
 * @see ArtisanBaseBlock Custom block interface
 */
@ApiStatus.NonExtendable
public interface BlockRegistry {
    /**
     * Registers a custom block to the central registry.
     *
     * @param artisanBlock The custom block instance to register (must not be null)
     * @throws IllegalArgumentException If the block ID already exists or parameters are invalid
     */
    void register(@NotNull ArtisanBaseBlock artisanBlock);

    /**
     * Checks if the specified ID is registered as a custom block.
     *
     * @param blockId The namespace key to check (must not be null)
     * @return true if the ID corresponds to a registered block, false otherwise
     */
    boolean isArtisanBlock(NamespacedKey blockId);

    /**
     * Gets a registered custom block instance.
     *
     * @param blockId The namespace key of the block (must not be null)
     * @return The corresponding custom block instance
     * @throws IllegalArgumentException If the ID is not registered
     */
    @NotNull ArtisanBaseBlock getArtisanBlock(@NotNull NamespacedKey blockId);

}
