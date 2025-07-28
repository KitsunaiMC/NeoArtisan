package io.github.moyusowo.neoartisanapi.api.item;

import io.github.moyusowo.neoartisanapi.api.block.block.base.ArtisanBaseBlock;
import io.github.moyusowo.neoartisanapi.api.item.factory.ItemBuilderFactory;
import io.github.moyusowo.neoartisanapi.api.util.ServiceUtil;
import io.papermc.paper.datacomponent.item.CustomModelData;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Core interface for custom items, providing access to custom item properties and features.
 *
 * <p>This interface represents a registered custom item instance in the system, containing
 * the item's basic information and various extended properties. All custom items should
 * have a unique {@link NamespacedKey} identifier.</p>
 *
 */
@ApiStatus.NonExtendable
public interface ArtisanItem {
    /**
     * Creates a new item builder instance
     *
     * @return an item builder instance for creating custom items
     */
    @NotNull
    static Builder builder() {
        return ServiceUtil.getService(ItemBuilderFactory.class).builder();
    }

    /**
     * Creates a new complex item builder instance
     *
     * @return a complex item builder instance for creating custom items with full control
     */
    @NotNull
    static ComplexBuilder complexBuilder() {
        return ServiceUtil.getService(ItemBuilderFactory.class).complexBuilder();
    }

    /**
     * Empty namespace key, used as item registry ID when item is empty
     */
    NamespacedKey EMPTY = new NamespacedKey("neoartisan", "empty_item_registry_id");

    /**
     * Checks if the given item stack represents this custom item.
     *
     * @param itemStack the item stack to compare (cannot be null)
     * @return true if the item stack represents this custom item, false otherwise
     */
    boolean equals(@NotNull ItemStack itemStack);

    /**
     * Checks if the given registry ID matches this custom item.
     *
     * @param registryId the registry ID to compare (cannot be null)
     * @return true if the ID matches, false otherwise
     */
    boolean equals(@NotNull NamespacedKey registryId);

    /**
     * Gets the unique registry ID of this custom item.
     *
     * @return the namespace key of the item (never null)
     */
    @NotNull NamespacedKey getRegistryId();

    /**
     * Gets the ItemStack of this custom item.
     *
     * @return the ItemStack of the custom item (never null)
     */
    @NotNull ItemStack getItemStack();

    /**
     * Gets the ItemStack of this custom item with specified count.
     *
     * @return the ItemStack of the custom item with specified count (never null)
     */
    @NotNull ItemStack getItemStack(int count);

    /**
     * Checks if this item preserves vanilla crafting recipes.
     *
     * @return true if vanilla crafting is preserved, false otherwise
     */
    boolean hasOriginalCraft();

    /**
     * Gets the attribute system configuration of this item.
     *
     * @return attribute configuration object, never null, can use {@link AttributeProperty#isEmpty()} to check empty
     * @see AttributeProperty
     */
    @NotNull
    AttributeProperty getAttributeProperty();

    /**
     * Gets the block ID that is placed when this item is right-clicked.
     *
     * @return the namespace ID of the block placed by right-clicking this item, or {@code null} if none
     */
    @Nullable NamespacedKey getBlockId();

    /**
     * Checks if this item is for internal use only.
     *
     * <p>internal use item will not show in tab-complete, guide book and collections returned by registry.</p>
     *
     * @return true if this item is for internal use, false otherwise
     */
    boolean isInternal();

    /**
     * Gets the tags associated with this item
     *
     * @return unmodifiable set of tags
     */
    @Unmodifiable
    @NotNull
    Set<String> getTags();

    /**
     * Gets the category of this item
     *
     * @return the category namespace key
     */
    @NotNull
    NamespacedKey getCategory();

    /**
     * Builder interface for complex custom items, allowing full customization of item stack data.
     * <p>Usage example:
     * <pre>{@code
     * ItemStack itemStack = ItemStack.of(Material.DIAMOND_SWORD);
     * itemStack.setData(DataComponentTypes.ITEM_NAME, Component.text("<red>Legendary Sword");
     * ArtisanItem newItem = ArtisanItem.complexBuilder()
     *     .registryId(new NamespacedKey(plugin, "sword"))
     *     .itemStack(itemStack)
     *     .build();
     * }</pre>
     */
    interface ComplexBuilder {

        /**
         * Sets the unique identifier key for the item.
         *
         * @param registryId the unique identifier key for the item (cannot be null)
         * @return current builder instance
         * @throws IllegalArgumentException if registryId is null
         */
        @NotNull ComplexBuilder registryId(@NotNull NamespacedKey registryId);

        /**
         * Provides the ItemStack Supplier for the custom item. Item count can be arbitrary.
         *
         * <p>After providing Supplier, {@link Supplier#get()} will be called each time to
         * dynamically generate ItemStack instances.</p>
         *
         * @param itemStackSupplier the provider of the custom item's ItemStack (cannot be null)
         * @return current builder instance
         * @throws IllegalArgumentException if rawMaterial is null
         * @see ItemStack
         */
        @NotNull ComplexBuilder itemStack(@NotNull Supplier<ItemStack> itemStackSupplier);

        /**
         * Sets whether to preserve vanilla crafting recipes.
         *
         * @return current builder instance
         */
        @NotNull ComplexBuilder hasOriginalCraft();

        /**
         * Sets the attribute system configuration for the item.
         *
         * @param attributeProperty attribute system configuration (cannot be null, use {@link AttributeProperty#empty()} for no attributes)
         * @return current builder instance
         * @throws IllegalArgumentException if attributeProperty is null
         * @see AttributeProperty
         */
        @NotNull ComplexBuilder attributeProperty(@Nullable AttributeProperty attributeProperty);

        /**
         * Sets the custom block that is placed when the item is right-clicked.
         *
         * @param blockId block ID (cannot be null)
         * @return current builder instance
         * @see ArtisanBaseBlock
         */
        @NotNull ComplexBuilder blockId(@Nullable NamespacedKey blockId);

        /**
         * Marks as internal use only. When marked, this item will not appear in tab-complete, guide book and collections returned by registry.
         *
         * @return current builder instance
         */
        @NotNull ComplexBuilder internalUse();

        /**
         * Sets the tags for this item
         *
         * @param tags set of tags (cannot be null)
         * @return current builder instance
         */
        @NotNull ComplexBuilder tags(@NotNull Set<String> tags);

        /**
         * Sets the category for this item
         *
         * @param category category namespace key (cannot be null)
         * @return current builder instance
         */
        @NotNull ComplexBuilder category(@NotNull NamespacedKey category);

        /**
         * Builds the custom item with the given parameters.
         *
         * @return the built custom item instance
         */
        @NotNull ArtisanItem build();

    }

    /**
     * Builder interface for simple custom items.
     * <p>Usage example:
     * <pre>{@code
     * ArtisanItem newItem = ArtisanItem.builder()
     *     .registryId(new NamespacedKey(plugin, "sword"))
     *     .rawMaterial(Material.DIAMOND_SWORD)
     *     .displayName("Legendary Sword")
     *     .build();
     * }</pre>
     */
    @SuppressWarnings("UnstableApiUsage")
    interface Builder {
        /**
         * Sets the unique identifier key for the item.
         *
         * @param registryId the unique identifier key for the item (cannot be null)
         * @return current builder instance
         * @throws IllegalArgumentException if registryId is null
         */
        @NotNull Builder registryId(@NotNull NamespacedKey registryId);

        /**
         * Sets the base material type for the item.
         *
         * @param rawMaterial base material type (cannot be null)
         * @return current builder instance
         * @throws IllegalArgumentException if rawMaterial is null
         * @see Material
         */
        @NotNull Builder rawMaterial(@NotNull Material rawMaterial);

        /**
         * Sets whether to preserve vanilla crafting recipes.
         *
         * @param hasOriginalCraft true to preserve vanilla crafting, false to disable (disabled by default)
         * @return current builder instance
         */
        @NotNull Builder hasOriginalCraft(boolean hasOriginalCraft);

        /**
         * Sets the custom model data for the item.
         *
         * @param customModelData custom model data ID (must be greater than 0)
         * @return current builder instance
         * @throws IllegalArgumentException if customModelData ≤ 0
         */
        @NotNull Builder customModelData(@Nullable CustomModelData customModelData);

        /**
         * Sets the display name for the item (support minimessage format).
         *
         * @param displayName item display name (cannot be null or empty string)
         * @return current builder instance
         * @throws IllegalArgumentException if displayName is null or empty
         */
        @NotNull Builder displayName(@Nullable String displayName);

        /**
         * Sets the display name for the item using adventure API text component.
         *
         * @param component item display name (cannot be null or empty string)
         * @return current builder instance
         * @throws IllegalArgumentException if displayName is null or empty
         */
        @NotNull Builder displayName(@Nullable Component component);

        /**
         * Sets the Lore description for the item.
         *
         * @param lore item description text list
         * @return current builder instance
         * @throws IllegalArgumentException if lore is null
         */
        @NotNull Builder lore(@NotNull List<String> lore);

        /**
         * Sets the Lore description for the item using adventure API text components.
         *
         * @param lore item description text list
         * @return current builder instance
         * @throws IllegalArgumentException if lore is null
         */
        @NotNull Builder loreComponent(@NotNull List<Component> lore);

        /**
         * Sets the food properties for the item.
         *
         * @param nutrition nutrition value
         * @param saturation saturation level
         * @param canAlwaysEat whether it can be eaten when full
         * @return current builder instance
         */
        @NotNull Builder foodProperty(int nutrition, float saturation, boolean canAlwaysEat);

        /**
         * Sets the food properties for the item with effects.
         *
         * @param nutrition nutrition value
         * @param saturation saturation level
         * @param canAlwaysEat whether it can be eaten when full
         * @param effectChance map of potion effects and their chances
         * @param consumeSeconds time to consume in seconds
         * @return current builder instance
         */
        @NotNull Builder foodProperty(int nutrition, float saturation, boolean canAlwaysEat, @NotNull Map<PotionEffect, Float> effectChance, float consumeSeconds);

        /**
         * Sets the weapon properties for the item.
         *
         * @param speed attack speed
         * @param knockback knockback strength
         * @param damage base damage value
         * @return current builder instance
         */
        @NotNull Builder weaponProperty(float speed, float knockback, float damage);

        /**
         * Sets the maximum durability value for the item.
         *
         * @param maxDurability maximum durability value (must be greater than 0)
         * @return current builder instance
         * @throws IllegalArgumentException if maxDurability ≤ 0
         */
        @NotNull Builder maxDurability(int maxDurability);

        /**
         * Sets the armor property configuration for the item.
         *
         * @param armor armor value
         * @param armorToughness armor toughness
         * @param slot equipment slot (null inherits template item's wear position, if none then cannot be null)
         * @return current builder instance
         */
        @NotNull Builder armorProperty(int armor, int armorToughness, @Nullable EquipmentSlot slot);

        /**
         * Sets the attribute system configuration for the item.
         *
         * @param attributeProperty attribute system configuration (cannot be null, use {@link AttributeProperty#empty()} for no attributes)
         * @return current builder instance
         * @throws IllegalArgumentException if attributeProperty is null
         * @see AttributeProperty
         */
        @NotNull Builder attributeProperty(@NotNull AttributeProperty attributeProperty);

        /**
         * Sets the custom block that is placed when the item is right-clicked.
         *
         * @param blockId block ID (cannot be null)
         * @return current builder instance
         * @see ArtisanBaseBlock
         */
        @NotNull Builder blockId(@Nullable NamespacedKey blockId);

        /**
         * Sets the custom model for the item.
         *
         * @param itemModel the namespace key of the custom model (cannot be null)
         * @return current builder instance
         */
        @NotNull Builder itemModel(@Nullable NamespacedKey itemModel);

        /**
         * If the item's Material is PLAYER_HEAD, this method can be used to set the skull texture.
         *
         * <p>If the item's Material is not PLAYER_HEAD, this method has no effect</p>
         *
         * @param textureUrl skull texture URL or corresponding Base64 encoding (cannot be null)
         * @param isBase64 whether the provided string is Base64 encoded
         * @return current builder instance
         */
        @NotNull Builder skullProperty(@NotNull String textureUrl, boolean isBase64);

        /**
         * Marks as internal use only. When marked, this item will not appear in tab-complete, guide book and collections returned by registry.
         *
         * @return current builder instance
         */
        @NotNull Builder internalUse();

        /**
         * Sets the tags for this item
         *
         * @param tags set of tags (cannot be null)
         * @return current builder instance
         */
        @NotNull Builder tags(@NotNull Set<String> tags);

        /**
         * Sets the category for this item
         *
         * @param category category namespace key (cannot be null)
         * @return current builder instance
         */
        @NotNull Builder category(@NotNull NamespacedKey category);

        /**
         * Builds the custom item with the given parameters.
         *
         * @return the built custom item instance
         */
        @NotNull ArtisanItem build();

    }
}
