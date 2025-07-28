package io.github.moyusowo.neoartisanapi.api.block.block.base;

import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.block.util.SoundProperty;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Base interface for custom blocks, defining basic behaviors and properties
 * for all custom blocks such as crops, machines, etc.
 *
 * <p>This interface is mainly used to manage transitions between block states
 * and define common behaviors for block types.</p>
 *
 * <p>
 * Each custom block instance should be immutable. All state changes must be
 * handled through {@link ArtisanBlockData} to ensure data consistency and
 * persistence support.
 * </p>
 *
 * @see ArtisanBlockData represents a specific block data instance in the world
 */
public interface ArtisanBaseBlock {
    /**
     * Gets the unique identifier of this custom block.
     * <p>
     * This ID is used for persistent storage and cross-module references.
     * It must be globally unique and remain unchanged during program execution.
     * </p>
     *
     * @return an immutable namespaced key that serves as the unique identifier for the block
     */
    @NotNull
    NamespacedKey getBlockId();

    /**
     * Gets the block state instance for the specified state value.
     *
     * <p>
     * State values are typically consecutive integers used to represent visual
     * or logical change stages of the block, such as crop growth stages or
     * machine working states.
     * </p>
     *
     * @param n state index (0 <= n < {@link #getTotalStates()})
     * @return the corresponding immutable state instance (fails silently,
     *         returns boundary value if out of range)
     */
    @NotNull
    ArtisanBaseBlockState getState(int n);

    /**
     * Gets the total number of states supported by this block.
     * <p>
     * The total number of states determines the granularity of visual or logical
     * changes of the block. Examples:
     * <ul>
     *   <li>Regular block: usually 1 (only one state)</li>
     *   <li>Growing crop: equals the number of growth stages (e.g., wheat has 8 stages)</li>
     *   <li>Complex device: equals the number of all possible state combinations</li>
     * </ul>
     * </p>
     *
     * @return a positive integer representing the total number of states the block has
     */
    int getTotalStates();

    /**
     * Gets the sound property played when placing the custom block.
     *
     * @return the sound property object for the custom block placement sound,
     *         or {@code null} if not specified
     */
    @Nullable
    SoundProperty getPlaceSoundProperty();

    /**
     * Gets the type enum that this block belongs to.
     *
     * @return the enum value representing the block type, not {@code null}
     */
    @NotNull
    ArtisanBlocks getType();

    /**
     * Checks whether this block has a block entity (BlockEntity).
     *
     * @return {@code true} if the block has an associated block entity;
     *         {@code false} otherwise
     * @apiNote Blocks with block entities can use
     *          {@link ArtisanBlockData#getPersistentDataContainer()} for data storage
     */
    boolean hasBlockEntity();
}
