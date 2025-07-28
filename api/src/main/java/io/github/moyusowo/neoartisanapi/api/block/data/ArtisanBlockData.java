package io.github.moyusowo.neoartisanapi.api.block.data;

import io.github.moyusowo.neoartisanapi.api.block.block.base.ArtisanBaseBlock;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.block.task.LifecycleTaskManager;
import io.github.moyusowo.neoartisanapi.api.util.ServiceUtil;
import io.github.moyusowo.neoartisanapi.api.block.gui.ArtisanBlockGUI;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Data container representing an actual custom block instance in the world.
 * <p>
 * This interface encapsulates all runtime state of a placed custom block, including:
 * <ul>
 *   <li>Physical location and display state</li>
 *   <li>Associated GUI instance (if any)</li>
 *   <li>Block lifecycle event manager</li>
 *   <li>Custom persistent data storage (if any)</li>
 *   <li>Current block state index</li>
 * </ul>
 * </p>
 *
 * @see ArtisanBaseBlock custom block type definition
 * @see ArtisanBaseBlockState block state system
 */
@ApiStatus.NonExtendable
public interface ArtisanBlockData {
    /**
     * Creates a new block data builder instance
     *
     * @return a block data builder instance for creating custom block data
     */
    @NotNull
    static Builder builder() {
        return ServiceUtil.getService(BuilderFactory.class).builder();
    }

    interface BuilderFactory {
        @NotNull
        Builder builder();
    }

    /**
     * Gets the GUI associated with this block
     *
     * @return GUI instance, or {@code null} if the block doesn't support it
     */
    @Nullable
    ArtisanBlockGUI getGUI();

    /**
     * Gets the physical location of the block in the world
     *
     * @return an immutable location object containing world and coordinate information
     */
    @NotNull
    Location getLocation();

    /**
     * Gets the block's ID
     *
     * @return a namespaced key consistent with {@link ArtisanBaseBlock#getBlockId()}
     * @see ArtisanBaseBlock#getBlockId()
     */
    @NotNull
    NamespacedKey blockId();

    /**
     * Gets the current block state index
     *
     * @return an integer between 0 and {@link ArtisanBaseBlock#getTotalStates()} (exclusive of the upper bound)
     */
    int stage();

    /**
     * Gets the associated custom block definition
     *
     * @return an immutable block type instance
     */
    @NotNull
    ArtisanBaseBlock getArtisanBlock();

    /**
     * Gets the current block state instance
     *
     * @return an immutable state obtained through {@link ArtisanBaseBlock#getState(int)}
     */
    @NotNull
    ArtisanBaseBlockState getArtisanBlockState();

    /**
     * Gets the persistent data container for the block
     * <p>
     * Used to store additional custom data
     * </p>
     *
     * @return a readable and writable Bukkit API data container with the same lifecycle as the block instance
     * @throws IllegalStateException if the custom block doesn't have a block entity
     * @apiNote Please ensure the custom block has a block entity bound to it before calling this method,
     *          or check with {@link ArtisanBaseBlock#hasBlockEntity()}
     */
    @NotNull
    PersistentDataContainer getPersistentDataContainer();

    /**
     * Gets the lifecycle task manager for the block
     *
     * @return the lifecycle task manager instance for this block
     */
    @NotNull
    LifecycleTaskManager getLifecycleTaskManager();

    /**
     * Builder interface for block data, used to gradually build custom block data instances
     */
    interface Builder {
        /**
         * Sets the location for the block data
         *
         * @param location the location for the block data, cannot be null
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder location(@NotNull Location location);

        /**
         * Sets the block ID for the block data
         *
         * @param blockId the namespaced key identifier for the block, cannot be null
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder blockId(@NotNull NamespacedKey blockId);

        /**
         * Sets the state index for the block data
         *
         * @param stage the state index for the block data
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder stage(int stage);

        /**
         * Builds and returns the final block data instance
         *
         * @return the completed block data instance
         */
        @NotNull
        ArtisanBlockData build();
    }
}
