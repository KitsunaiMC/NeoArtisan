package io.github.moyusowo.neoartisanapi.api.item;

import io.github.moyusowo.neoartisanapi.api.util.ServiceUtil;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;

/**
 * Item attribute storage container interface, used to manage item initial attributes.
 *
 * <p>All attributes will be written to the internal {@link org.bukkit.persistence.PersistentDataContainer}
 * data container when the item's [ItemStack](file://org\bukkit\inventory\ItemStack.java#L37-L170) is created</p>
 *
 * @see #empty()
 */
@ApiStatus.NonExtendable
public interface AttributeProperty {
    /**
     * Gets a new custom item attribute configuration instance.
     *
     * @return a new custom item attribute configuration instance
     */
    @NotNull
    static AttributeProperty empty() {
        return ServiceUtil.getService(AttributeProperty.class);
    }

    /**
     * Adds an attribute.
     *
     * @param key attribute identifier (cannot be null)
     * @param type attribute type (cannot be null)
     * @param value attribute value (cannot be null)
     * @throws IllegalArgumentException if parameters are null or value type is invalid
     */
    <P, C> AttributeProperty addAttribute(@NotNull NamespacedKey key, @NotNull PersistentDataType<P, C> type, @NotNull C value);

    /**
     * Checks if a specified attribute exists.
     *
     * @param key the attribute identifier to check
     * @return true if the attribute exists
     */
    boolean hasAttribute(@NotNull NamespacedKey key);

    /**
     * Gets a global attribute value.
     *
     * @param <T> return value type
     * @param key attribute identifier (cannot be null)
     * @param type attribute type (cannot be null)
     * @return the stored attribute value (never null)
     * @throws IllegalArgumentException if the attribute does not exist or type does not match
     */
    @NotNull <T> T getAttribute(@NotNull NamespacedKey key, @NotNull Class<T> type);

    /**
     * Checks if the current container is an empty configuration.
     *
     * @return true if no attributes (global/item) are set
     */
    boolean isEmpty();

    /**
     * Gets all registered attribute `NamespacedKey`s.
     *
     * @return list of item attribute keys
     */
    @Unmodifiable
    @NotNull
    Collection<NamespacedKey> getAttributeKeys();

    /**
     * write this attribute property into the provided persistence data container
     *
     * @param persistenceDataContainer the persistence data container (cannot be null)
     */
    void setPersistenceDataContainer(@NotNull PersistentDataContainer persistenceDataContainer);
}
