package io.github.moyusowo.neoartisanapi.api.block.storage;

import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Custom block data storage service, responsible for managing all placed custom block instances.
 * <p>
 * Provides query and type checking functions for custom blocks, supporting access via coordinates
 * or {@link Block} instances. Some methods marked as thread-safe can be called in multi-threaded environments.
 * </p>
 *
 * @see ArtisanBlockData basic block data interface
 * @see io.github.moyusowo.neoartisanapi.api.block.storage.Storages#BLOCK Get service instance
 */
@ApiStatus.NonExtendable
public interface ArtisanBlockStorage {
    /**
     * Gets custom block data by world coordinates
     *
     * @param worldUID target world UID
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return the corresponding block data
     * @throws IllegalArgumentException if not found
     * @apiNote Call {@link #isArtisanBlock(World, int, int, int)} to check before using this method
     * @apiNote This method is thread-safe
     */
    @NotNull
    ArtisanBlockData getArtisanBlockData(@NotNull UUID worldUID, int x, int y, int z);

    /**
     * Gets custom block data by world coordinates
     *
     * @param world target world
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return the corresponding block data
     * @throws IllegalArgumentException if not found
     * @apiNote Call {@link #isArtisanBlock(World, int, int, int)} to check before using this method
     */
    @NotNull
    default ArtisanBlockData getArtisanBlockData(@NotNull World world, int x, int y, int z) {
        return getArtisanBlockData(world.getUID(), x, y, z);
    }

    /**
     * Gets custom block data by block instance
     *
     * @param block target block (non-null)
     * @return the corresponding block data
     * @throws IllegalArgumentException if not found
     * @apiNote Call {@link #isArtisanBlock(Block)} to check before using this method
     */
    @NotNull
    default ArtisanBlockData getArtisanBlockData(@NotNull Block block) {
        return getArtisanBlockData(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    /**
     * Gets custom block data by location instance
     *
     * @param location target location (non-null)
     * @return the corresponding block data
     * @throws IllegalArgumentException if not found
     * @apiNote Call {@link #isArtisanBlock(Block)} to check before using this method
     */
    @NotNull
    default ArtisanBlockData getArtisanBlockData(@NotNull Location location) {
        return getArtisanBlockData(location.getBlock());
    }

    /**
     * Checks if the coordinate position is a registered custom block
     *
     * @param worldUID target world UID (non-null)
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return true if it is a valid custom block
     * @apiNote This method is thread-safe
     */
    boolean isArtisanBlock(@NotNull UUID worldUID, int x, int y, int z);

    /**
     * Checks if the coordinate position is a registered custom block
     *
     * @param world target world (non-null)
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @return true if it is a valid custom block
     */
    default boolean isArtisanBlock(@NotNull World world, int x, int y, int z) {
        return isArtisanBlock(world.getUID(), x, y, z);
    }

    /**
     * Checks if the block is a registered custom block
     *
     * @param block block to check (non-null)
     * @return true if it is a valid custom block
     */
    default boolean isArtisanBlock(@NotNull Block block) {
        return isArtisanBlock(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    /**
     * Checks if the block at the location is a registered custom block
     *
     * @param location location to check (non-null)
     * @return true if it is a valid custom block
     */
    default boolean isArtisanBlock(@NotNull Location location) {
        return isArtisanBlock(location.getBlock());
    }

    /**
     * Sets custom block data at the specified location (the actual block in the world needs to be set manually first)
     *
     * @param worldUID target world UID (non-null)
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param artisanBlockData custom block data (null means air)
     * @apiNote This method is thread-safe
     */
    void setArtisanBlockData(@NotNull UUID worldUID, int x, int y, int z, @Nullable ArtisanBlockData artisanBlockData);

    /**
     * Sets custom block data at the specified location (the actual block in the world needs to be set manually first)
     *
     * @param world target world (non-null)
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param artisanBlockData custom block data (null means air)
     */
    default void setArtisanBlockData(@NotNull World world, int x, int y, int z, @Nullable ArtisanBlockData artisanBlockData) {
        setArtisanBlockData(world.getUID(), x, y, z, artisanBlockData);
    }

    /**
     * Sets custom block data at the specified location (the actual block in the world needs to be set manually first)
     *
     * @param block target location
     * @param artisanBlockData custom block data (null means air)
     */
    default void setArtisanBlockData(@NotNull Block block, @Nullable ArtisanBlockData artisanBlockData) {
        setArtisanBlockData(block.getWorld(), block.getX(), block.getY(), block.getZ(), artisanBlockData);
    }

    /**
     * Sets custom block data at the specified location (the actual block in the world needs to be set manually first)
     *
     * @param location target location
     * @param artisanBlockData custom block data (null means air)
     */
    default void setArtisanBlockData(@NotNull Location location, @Nullable ArtisanBlockData artisanBlockData) {
        setArtisanBlockData(location.getBlock(), artisanBlockData);
    }
}
