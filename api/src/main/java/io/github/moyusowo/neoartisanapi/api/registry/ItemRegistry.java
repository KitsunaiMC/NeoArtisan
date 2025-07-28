package io.github.moyusowo.neoartisanapi.api.registry;

import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Core interface for custom item registry, providing complete lifecycle management for custom items.
 *
 * <p>This interface is responsible for:</p>
 * <ul>
 *   <li>Registration and configuration of custom items</li>
 *   <li>Retrieval and validation of item instances</li>
 * </ul>
 *
 * <p>Get the instance through {@link org.bukkit.Bukkit#getServicesManager()}.</p>
 *
 * @apiNote Some methods involve persistence operations, please call on the main thread
 * @see ArtisanItem custom Item interface
 */
@ApiStatus.NonExtendable
public interface ItemRegistry {
    /**
     * Registers a custom item.
     *
     * @param artisanItem The custom item instance (must not be null)
     * @throws IllegalArgumentException If the builder is null or contains invalid parameters
     */
    void registerItem(@NotNull ArtisanItem artisanItem);

    /**
     * Registers tags to a material.
     *
     * @param material The material to tag (must not be null)
     * @param tags The tags to associate with the material (must not be null)
     */
    void registerTagToMaterial(@NotNull Material material, @NotNull String... tags);

    /**
     * Resolves the registry ID from an item stack.
     *
     * <p>Vanilla Minecraft items will return their namespace ID.</p>
     *
     * <p>Empty items will return {@link ArtisanItem#EMPTY}.</p>
     *
     * @param itemStack The target item stack (can be null)
     * @return The corresponding registry ID (never null)
     */
    @NotNull NamespacedKey getRegistryId(@Nullable ItemStack itemStack);

    /**
     * Checks if a custom item with the specified ID is registered.
     *
     * <p>This method includes vanilla Minecraft items.</p>
     *
     * @param registryId The item ID to check (can be null)
     * @return true if the item exists, false otherwise
     * @apiNote This method always returns immediately and does not throw exceptions
     */
    boolean hasItem(@Nullable NamespacedKey registryId);

    /**
     * Creates an item stack with the specified quantity of the given namespace ID.
     *
     * <p>This method is compatible with vanilla Minecraft item namespace IDs.</p>
     *
     * @param registryId The item registry ID (must not be null)
     * @param count The item quantity (values exceeding the stack limit will be automatically set to the limit)
     * @return A new item stack instance (never null)
     * @throws IllegalArgumentException If the item is not registered or parameters are invalid
     */
    @NotNull ItemStack getItemStack(NamespacedKey registryId, int count);

    /**
     * Creates an item stack with quantity 1 of the given namespace ID.
     *
     * <p>This method is compatible with vanilla Minecraft item namespace IDs.</p>
     *
     * @param registryId The item registry ID (must not be null)
     * @return A new item stack instance (never null)
     */
    @NotNull ItemStack getItemStack(NamespacedKey registryId);

    /**
     * Validates whether the given ID corresponds to a valid custom item.
     *
     * @param registryId The item ID to check (can be null)
     * @return true if it is a registered custom item, false otherwise
     */
    boolean isArtisanItem(@Nullable NamespacedKey registryId);

    /**
     * Validates whether the given item stack is a custom item.
     *
     * @param itemStack The item stack to check (can be null)
     * @return true if it is a custom item registered by this system, false otherwise
     */
    boolean isArtisanItem(@Nullable ItemStack itemStack);

    /**
     * Gets the item API instance by ID.
     *
     * @param registryId The item registry ID (must not be null)
     * @return The item API interface instance (never null)
     * @throws IllegalArgumentException If the item is not registered
     * @see ArtisanItem
     * @apiNote You should always call {@link ItemRegistry#isArtisanItem(NamespacedKey)} to check validity before calling this method
     */
    @NotNull
    ArtisanItem getArtisanItem(@NotNull NamespacedKey registryId);

    /**
     * Gets the item API instance from an item stack.
     *
     * @param itemStack The target item stack (must not be null)
     * @return The item API interface instance (never null)
     * @throws IllegalArgumentException If it is not a valid custom item
     * @see #getArtisanItem(NamespacedKey)
     * @apiNote You should always call {@link ItemRegistry#isArtisanItem(ItemStack)} to check validity before calling this method
     */
    @NotNull
    ArtisanItem getArtisanItem(@NotNull ItemStack itemStack);

    /**
     * Gets all IDs associated with the specified tag.
     *
     * @param tag The tag to query (must not be null)
     * @return An unmodifiable collection of namespace keys
     */
    @NotNull
    @Unmodifiable
    Collection<NamespacedKey> getIdByTag(@NotNull String tag);

    /**
     * Gets all tags associated with the specified ID.
     *
     * @param id The namespace key to query (must not be null)
     * @return An unmodifiable collection of tags
     */
    @NotNull
    @Unmodifiable
    Collection<String> getTagsById(@NotNull NamespacedKey id);

    /**
     * Gets all tags associated with the specified item stack.
     *
     * @param itemStack The item stack to query (can be null)
     * @return An unmodifiable collection of tags
     */
    @NotNull
    @Unmodifiable
    default Collection<String> getTagsByItemStack(@Nullable ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) return Collections.emptyList();
        return getTagsById(Registries.ITEM.getRegistryId(itemStack));
    }

    /**
     * Gets all registered item IDs.
     *
     * @return An unmodifiable set of all registered namespace keys
     */
    @Unmodifiable
    @NotNull
    Set<NamespacedKey> getAllIds();
}
