package io.github.moyusowo.neoartisan.item;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisanapi.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.item.*;
import io.github.moyusowo.neoartisan.util.NamespacedKeyDataType;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.*;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings({"unused", "UnstableApiUsage"})
class ArtisanItemImpl implements ArtisanItem {
    private final NamespacedKey registryId;
    private final Material rawMaterial;
    private final boolean hasOriginalCraft;
    private final CustomModelData customModelData;
    private final Component displayName;
    private final List<Component> lore;
    private final FoodProperty foodProperty;
    private final WeaponProperty weaponProperty;
    private final Integer maxDurability;
    private final ArmorProperty armorProperty;
    private final AttributePropertyImpl attributeProperty;
    private final NamespacedKey cropId;
    private final NamespacedKey itemModel;
    private final ItemStack cachedItemStack;

    protected ArtisanItemImpl(
            NamespacedKey registryId,
            Material rawMaterial,
            boolean hasOriginalCraft,
            @Nullable CustomModelData customModelData,
            Component displayName,
            List<Component> lore,
            @NotNull FoodProperty foodProperty,
            @NotNull WeaponProperty weaponProperty,
            @Nullable Integer maxDurability,
            @NotNull ArmorProperty armorProperty,
            @NotNull AttributePropertyImpl attributeProperty,
            @Nullable NamespacedKey cropId,
            @Nullable NamespacedKey itemModel
    ) {
        this.registryId = registryId;
        this.rawMaterial = rawMaterial;
        this.hasOriginalCraft = hasOriginalCraft;
        this.customModelData = customModelData;
        this.displayName = displayName;
        this.lore = lore;
        this.foodProperty = foodProperty;
        this.weaponProperty = weaponProperty;
        this.maxDurability = maxDurability;
        this.armorProperty = armorProperty;
        this.attributeProperty = attributeProperty;
        this.cropId = cropId;
        this.itemModel = itemModel;
        this.cachedItemStack = createNewItemStack();
    }

    protected ItemStack getItemStack(int count) {
        ItemStack itemStack = this.cachedItemStack.clone();
        itemStack.setAmount(Math.min(count, itemStack.getMaxStackSize()));
        return itemStack;
    }

    protected ItemStack getItemStack() {
        return this.getItemStack(1);
    }

    @Override
    public boolean equals(@NotNull ItemStack itemStack) {
        if (!itemStack.getItemMeta().getPersistentDataContainer().has(NeoArtisan.getArtisanItemIdKey())) return false;
        return itemStack.getItemMeta().getPersistentDataContainer().get(NeoArtisan.getArtisanItemIdKey(), NamespacedKeyDataType.TYPE).equals(this.registryId);
    }

    @Override
    public boolean equals(@NotNull NamespacedKey registryId) {
        return registryId.equals(this.registryId);
    }

    @Override
    public @NotNull NamespacedKey getRegistryId() {
        return this.registryId;
    }

    @Override
    public @NotNull Material getRawMaterial() {
        return this.rawMaterial;
    }

    @Override
    public boolean hasOriginalCraft() {
        return this.hasOriginalCraft;
    }

    @Override
    public CustomModelData getCustomModelData() {
        return this.customModelData;
    }

    @Override
    public @NotNull FoodProperty getFoodProperty() {
        return this.foodProperty;
    }

    @Override
    public @NotNull WeaponProperty getWeaponProperty() {
        return this.weaponProperty;
    }

    @Override
    public Integer getMaxDurability() {
        return this.maxDurability;
    }

    @Override
    public @NotNull AttributeProperty getAttributeProperty() {
        return this.attributeProperty;
    }

    @Override
    public @NotNull ArmorProperty getArmorProperty() {
        return this.armorProperty;
    }

    @Override
    public @Nullable NamespacedKey getCropId() {
        return this.cropId;
    }

    @Override
    public @Nullable NamespacedKey getItemModel() {
        return this.itemModel;
    }

    private ItemStack createNewItemStack() {
        ItemStack itemStack = ItemStack.of(this.rawMaterial);
        var unmodifiableModifiers = itemStack.getDataOrDefault(DataComponentTypes.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.itemAttributes().build());
        final var builder = ItemAttributeModifiers.itemAttributes();
        unmodifiableModifiers.modifiers().forEach(entry -> {
            if (
                    entry.attribute() != Attribute.ATTACK_DAMAGE &&
                    entry.attribute() != Attribute.ATTACK_SPEED &&
                    entry.attribute() != Attribute.ATTACK_KNOCKBACK &&
                    entry.attribute() != Attribute.ARMOR &&
                    entry.attribute() != Attribute.ARMOR_TOUGHNESS
            ) {
                builder.addModifier(entry.attribute(), entry.modifier(), entry.getGroup());
            }
        });
        if (this.displayName != null) {
            itemStack.setData(DataComponentTypes.ITEM_NAME, this.displayName);
        }
        if (this.lore != null) {
            ItemLore itemLore = ItemLore.lore().addLines(this.lore).build();
            itemStack.setData(DataComponentTypes.LORE, itemLore);
        }
        if (this.customModelData != null) {
            itemStack.setData(DataComponentTypes.CUSTOM_MODEL_DATA, this.customModelData);
        }
        if (this.foodProperty != FoodProperty.EMPTY) {
            FoodProperties foodProperties = FoodProperties.food()
                    .canAlwaysEat(this.foodProperty.canAlwaysEat())
                    .nutrition(this.foodProperty.nutrition())
                    .saturation(this.foodProperty.saturation())
                    .build();
            itemStack.setData(DataComponentTypes.FOOD, foodProperties);
        }
        if (this.weaponProperty != WeaponProperty.EMPTY) {
            builder.addModifier(
                Attribute.ATTACK_DAMAGE,
                new AttributeModifier(
                        NeoArtisan.getArtisanItemAttackDamageKey(),
                        this.weaponProperty.damage(),
                        AttributeModifier.Operation.ADD_NUMBER,
                        EquipmentSlotGroup.MAINHAND

            ));
            builder.addModifier(
                Attribute.ATTACK_SPEED,
                new AttributeModifier(
                        NeoArtisan.getArtisanItemAttackSpeedKey(),
                        this.weaponProperty.speed(),
                        AttributeModifier.Operation.ADD_NUMBER,
                        EquipmentSlotGroup.MAINHAND
                )
            );
            builder.addModifier(
                Attribute.ATTACK_KNOCKBACK,
                new AttributeModifier(
                        NeoArtisan.getArtisanItemAttackKnockbackKey(),
                        this.weaponProperty.knockback(),
                        AttributeModifier.Operation.ADD_NUMBER,
                        EquipmentSlotGroup.MAINHAND
                )
            );
        } else {
            unmodifiableModifiers.modifiers().forEach(entry -> {
                if (
                        entry.attribute() == Attribute.ATTACK_DAMAGE ||
                        entry.attribute() == Attribute.ATTACK_SPEED ||
                        entry.attribute() == Attribute.ATTACK_KNOCKBACK
                ) {
                    builder.addModifier(entry.attribute(), entry.modifier(), entry.getGroup());
                }
            });
        }
        if (this.armorProperty != ArmorProperty.EMPTY) {
            if (this.armorProperty.slot() != null) {
                itemStack.setData(DataComponentTypes.EQUIPPABLE, Equippable.equippable(this.armorProperty.slot()));
                builder.addModifier(
                    Attribute.ARMOR,
                    new AttributeModifier(
                            this.registryId,
                            this.armorProperty.armor(),
                            AttributeModifier.Operation.ADD_NUMBER,
                            this.armorProperty.slot().getGroup()
                    )
                );
                builder.addModifier(
                    Attribute.ARMOR_TOUGHNESS,
                    new AttributeModifier(
                            this.registryId,
                            this.armorProperty.armorToughness(),
                            AttributeModifier.Operation.ADD_NUMBER,
                            this.armorProperty.slot().getGroup()
                    )
                );
            } else {
                var equippable = itemStack.getData(DataComponentTypes.EQUIPPABLE);
                if (equippable == null) throw new IllegalArgumentException("You can not set null slot in a unequipped item!");
                builder.addModifier(
                    Attribute.ARMOR,
                    new AttributeModifier(
                            this.registryId,
                            this.armorProperty.armor(),
                            AttributeModifier.Operation.ADD_NUMBER,
                            equippable.slot().getGroup()
                    )
                );
                builder.addModifier(
                    Attribute.ARMOR_TOUGHNESS,
                    new AttributeModifier(
                            this.registryId,
                            this.armorProperty.armorToughness(),
                            AttributeModifier.Operation.ADD_NUMBER,
                            equippable.slot().getGroup()
                    )
                );
            }
        } else {
            unmodifiableModifiers.modifiers().forEach(entry -> {
                if (
                        entry.attribute() == Attribute.ARMOR ||
                        entry.attribute() == Attribute.ARMOR_TOUGHNESS
                ) {
                    builder.addModifier(entry.attribute(), entry.modifier(), entry.getGroup());
                }
            });
        }
        if (this.maxDurability != null) {
            itemStack.setData(DataComponentTypes.MAX_DAMAGE, this.maxDurability);
        }
        if (this.itemModel != null) {
            itemStack.setData(DataComponentTypes.ITEM_MODEL, this.itemModel);
        }
        itemStack.setData(DataComponentTypes.ATTRIBUTE_MODIFIERS, builder.build());
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!this.attributeProperty.isEmpty()) {
            NamespacedKey[] keys = this.attributeProperty.getItemStackAttributeKeys();
            for (NamespacedKey key : keys) {
                PersistentDataType<?, ?> PDCType = NeoArtisanAPI.getItemStackAttributeRegistry().getAttributePDCType(key);
                itemMeta.getPersistentDataContainer().set(key, PDCType, this.attributeProperty.getItemStackAttributeValue(key));
            }
        }
        itemMeta.getPersistentDataContainer().set(NeoArtisan.getArtisanItemIdKey(), NamespacedKeyDataType.TYPE, this.registryId);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
