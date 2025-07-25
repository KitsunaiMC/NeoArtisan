package io.github.moyusowo.neoartisan.item;

import com.destroystokyo.paper.profile.ProfileProperty;
import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.item.property.ArmorProperty;
import io.github.moyusowo.neoartisan.item.property.FoodProperty;
import io.github.moyusowo.neoartisan.item.property.WeaponProperty;
import io.github.moyusowo.neoartisan.util.data.NamespacedKeyDataType;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import io.github.moyusowo.neoartisanapi.api.item.AttributeProperty;
import io.github.moyusowo.neoartisanapi.api.item.factory.ItemBuilderFactory;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.*;
import io.papermc.paper.datacomponent.item.consumable.ConsumeEffect;
import io.papermc.paper.datacomponent.item.consumable.ItemUseAnimation;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Supplier;

final class ArtisanItemImpl implements ArtisanItem {

    @InitMethod(priority = InitPriority.REGISTRAR)
    public static void init() {
        Bukkit.getServicesManager().register(
                ItemBuilderFactory.class,
                new ItemBuilderFactory() {
                    @Override
                    public @NotNull ArtisanItem.ComplexBuilder complexBuilder() {
                        return new ComplexBuilderImpl();
                    }

                    @Override
                    public @NotNull ArtisanItem.Builder builder() {
                        return new BuilderImpl();
                    }
                },
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private final NamespacedKey registryId;
    private final boolean hasOriginalCraft;
    private final AttributeProperty attributeProperty;
    private final NamespacedKey blockId;
    private final ItemStack itemStack;
    private final Supplier<ItemStack> itemStackSupplier;
    private final boolean isInternal;
    private final Set<String> tags;

    ArtisanItemImpl(
            NamespacedKey registryId,
            ItemStack itemStack,
            boolean hasOriginalCraft,
            @NotNull AttributeProperty attributeProperty,
            @Nullable NamespacedKey blockId,
            boolean isInternal,
            @NotNull Set<String> tags
    ) {
        this.registryId = registryId;
        this.itemStack = itemStack.clone();
        this.hasOriginalCraft = hasOriginalCraft;
        this.attributeProperty = attributeProperty;
        this.blockId = blockId;
        addIdAndAttribute(this.itemStack);
        this.itemStackSupplier = null;
        this.isInternal = isInternal;
        this.tags = new HashSet<>(tags);
    }

    ArtisanItemImpl(
            NamespacedKey registryId,
            Supplier<ItemStack> itemStackSupplier,
            boolean hasOriginalCraft,
            @NotNull AttributeProperty attributeProperty,
            @Nullable NamespacedKey blockId,
            boolean isInternal,
            @NotNull Set<String> tags
    ) {
        this.registryId = registryId;
        this.itemStack = null;
        this.hasOriginalCraft = hasOriginalCraft;
        this.attributeProperty = attributeProperty;
        this.blockId = blockId;
        this.itemStackSupplier = itemStackSupplier;
        this.isInternal = isInternal;
        this.tags = new HashSet<>(tags);
    }

    public @NotNull ItemStack getItemStack(int count) {
        final ItemStack itemStack;
        if (this.itemStack != null) itemStack = this.itemStack.clone();
        else {
            itemStack = this.itemStackSupplier.get();
            addIdAndAttribute(itemStack);
        }
        itemStack.setAmount(Math.max(0, Math.min(count, itemStack.getMaxStackSize())));
        return itemStack;
    }

    public @NotNull ItemStack getItemStack() {
        return this.getItemStack(1);
    }

    @Override
    public boolean equals(Object object) {
        return super.equals(object);
    }

    @Override
    public boolean equals(@NotNull ItemStack itemStack) {
        if (!itemStack.getItemMeta().getPersistentDataContainer().has(NeoArtisan.getArtisanItemIdKey())) return false;
        NamespacedKey key = itemStack.getItemMeta().getPersistentDataContainer().get(NeoArtisan.getArtisanItemIdKey(), NamespacedKeyDataType.NAMESPACED_KEY);
        assert key != null;
        return key.equals(this.registryId);
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
    public boolean hasOriginalCraft() {
        return this.hasOriginalCraft;
    }

    @Override
    public @NotNull AttributeProperty getAttributeProperty() {
        return this.attributeProperty;
    }

    @Override
    public @Nullable NamespacedKey getBlockId() {
        return this.blockId;
    }

    @Override
    public boolean isInternal() {
        return this.isInternal;
    }

    @Override
    public @Unmodifiable @NotNull Set<String> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    private void addIdAndAttribute(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (!this.attributeProperty.isEmpty()) {
            this.attributeProperty.setPersistenceDataContainer(itemMeta.getPersistentDataContainer());
        }
        itemMeta.getPersistentDataContainer().set(NeoArtisan.getArtisanItemIdKey(), NamespacedKeyDataType.NAMESPACED_KEY, this.registryId);
        itemStack.setItemMeta(itemMeta);
    }

    private static final class ComplexBuilderImpl implements ComplexBuilder {
        private NamespacedKey registryId;
        private boolean hasOriginalCraft;
        private AttributeProperty attributeProperty;
        private NamespacedKey blockId;
        private Supplier<ItemStack> itemStackSupplier;
        private boolean isInternal;
        private Set<String> tags;

        private ComplexBuilderImpl() {
            this.registryId = null;
            this.hasOriginalCraft = false;
            this.attributeProperty = new AttributePropertyImpl();
            this.blockId = null;
            this.itemStackSupplier = null;
            this.isInternal = false;
            this.tags = new HashSet<>();
        }

        @NotNull
        @Override
        public ComplexBuilder registryId(@NotNull NamespacedKey registryId) {
            this.registryId = Objects.requireNonNull(registryId);
            return this;
        }

        @Override
        public @NotNull ComplexBuilder itemStack(@NotNull Supplier<ItemStack> itemStackSupplier) {
            this.itemStackSupplier = itemStackSupplier;
            return this;
        }

        @Override
        public @NotNull ComplexBuilder hasOriginalCraft() {
            this.hasOriginalCraft = true;
            return this;
        }

        @NotNull
        @Override
        public ComplexBuilder attributeProperty(@Nullable AttributeProperty attributeProperty) {
            this.attributeProperty = attributeProperty;
            return this;
        }

        @NotNull
        @Override
        public ComplexBuilder blockId(@Nullable NamespacedKey cropId) {
            this.blockId = cropId;
            return this;
        }

        @Override
        public @NotNull ComplexBuilder internalUse() {
            this.isInternal = true;
            return this;
        }

        @Override
        public @NotNull ComplexBuilder tags(@NotNull Set<String> tags) {
            this.tags = tags;
            return this;
        }

        @Override
        @NotNull
        public ArtisanItem build() {
            if (registryId == null || itemStackSupplier == null) {
                throw new IllegalArgumentException("You should at least provide registryId and rawMaterial!");
            }
            return new ArtisanItemImpl(
                    registryId,
                    itemStackSupplier,
                    hasOriginalCraft,
                    attributeProperty,
                    blockId,
                    isInternal,
                    tags
            );
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    private static final class BuilderImpl implements Builder {
        private NamespacedKey registryId;
        private Material rawMaterial;
        private boolean hasOriginalCraft;
        private CustomModelData customModelData;
        private Component displayName;
        private List<Component> lore;
        private Integer maxDurability;
        private AttributePropertyImpl attributeProperty;
        private NamespacedKey blockId;
        private NamespacedKey itemModel;
        private FoodProperty foodProperty;
        private WeaponProperty weaponProperty;
        private ArmorProperty armorProperty;
        private String skullTextureUrlBase64;
        private boolean isInternal;
        private Set<String> tags;

        private BuilderImpl() {
            this.registryId = null;
            this.rawMaterial = null;
            this.hasOriginalCraft = false;
            this.customModelData = null;
            this.displayName = null;
            this.lore = new ArrayList<>();
            this.maxDurability = null;
            this.attributeProperty = new AttributePropertyImpl();
            this.blockId = null;
            this.itemModel = null;
            this.foodProperty = FoodProperty.EMPTY;
            this.armorProperty = ArmorProperty.EMPTY;
            this.weaponProperty = WeaponProperty.EMPTY;
            this.skullTextureUrlBase64 = null;
            this.isInternal = false;
            this.tags = new HashSet<>();
        }

        @NotNull
        @Override
        public Builder registryId(@NotNull NamespacedKey registryId) {
            this.registryId = Objects.requireNonNull(registryId);
            return this;
        }

        @NotNull
        @Override
        public Builder rawMaterial(@NotNull Material rawMaterial) {
            this.rawMaterial = Objects.requireNonNull(rawMaterial);
            return this;
        }

        @NotNull
        @Override
        public Builder hasOriginalCraft(boolean hasOriginalCraft) {
            this.hasOriginalCraft = hasOriginalCraft;
            return this;
        }

        @NotNull
        @Override
        public Builder customModelData(@Nullable CustomModelData customModelData) {
            this.customModelData = customModelData;
            return this;
        }

        @NotNull
        @Override
        public Builder displayName(@Nullable String displayName) {
            this.displayName = toNameComponent(Objects.requireNonNull(displayName));
            return this;
        }

        @NotNull
        @Override
        public Builder displayName(@Nullable Component component) {
            this.displayName = Objects.requireNonNull(component);
            return this;
        }

        @NotNull
        @Override
        public Builder lore(@NotNull List<String> lore) {
            this.lore = toLoreComponentList(Objects.requireNonNull(lore));
            return this;
        }

        @NotNull
        @Override
        public Builder loreComponent(@NotNull List<Component> lore) {
            this.lore = Objects.requireNonNull(lore);
            return this;
        }

        @NotNull
        @Override
        public Builder foodProperty(int nutrition, float saturation, boolean canAlwaysEat) {
            this.foodProperty = FoodProperty.create(nutrition, saturation, canAlwaysEat);
            return this;
        }

        @Override
        public @NotNull Builder foodProperty(int nutrition, float saturation, boolean canAlwaysEat, @NotNull Map<PotionEffect, Float> effectChance, float consumeSeconds) {
            this.foodProperty = FoodProperty.create(nutrition, saturation, canAlwaysEat, effectChance, consumeSeconds);
            return this;
        }

        @NotNull
        @Override
        public Builder weaponProperty(float speed, float knockback, float damage) {
            this.weaponProperty = new WeaponProperty(speed, knockback, damage);
            return this;
        }

        @NotNull
        @Override
        public Builder maxDurability(int maxDurability) {
            this.maxDurability = maxDurability;
            return this;
        }

        @NotNull
        @Override
        public Builder armorProperty(int armor, int armorToughness, @Nullable EquipmentSlot slot) {
            this.armorProperty = new ArmorProperty(armor, armorToughness, slot);
            return this;
        }

        @NotNull
        @Override
        public Builder attributeProperty(@NotNull AttributeProperty attributeProperty) {
            this.attributeProperty = Objects.requireNonNull((AttributePropertyImpl) attributeProperty);
            return this;
        }

        @NotNull
        @Override
        public Builder blockId(@Nullable NamespacedKey cropId) {
            this.blockId = cropId;
            return this;
        }

        @NotNull
        @Override
        public Builder itemModel(@Nullable NamespacedKey itemModel) {
            this.itemModel = itemModel;
            return this;
        }

        @Override
        public @NotNull Builder skullProperty(@NotNull String textureUrl, boolean isBase64) {
            if (isBase64) {
                this.skullTextureUrlBase64 = textureUrl;
            } else {
                this.skullTextureUrlBase64 = Base64.getUrlEncoder().encodeToString(textureUrl.getBytes(StandardCharsets.UTF_8));
            }
            return this;
        }

        @Override
        public @NotNull Builder internalUse() {
            this.isInternal = true;
            return this;
        }

        @Override
        public @NotNull Builder tags(@NotNull Set<String> tags) {
            this.tags = tags;
            return this;
        }

        @Override
        @NotNull
        public ArtisanItem build() {
            if (registryId == null || rawMaterial == null) {
                throw new IllegalArgumentException("You should at least provide registryId and rawMaterial!");
            }
            return new ArtisanItemImpl(
                    registryId,
                    createNewItemStack(),
                    hasOriginalCraft,
                    attributeProperty,
                    blockId,
                    isInternal,
                    tags
            );
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
            if (this.skullTextureUrlBase64 != null) {
                itemStack.setData(DataComponentTypes.PROFILE, ResolvableProfile.resolvableProfile().name(null).uuid(null).addProperty(new ProfileProperty("textures", this.skullTextureUrlBase64)));
            }
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
                if (this.foodProperty.getEffectChances().isPresent() && this.foodProperty.getConsumeSeconds().isPresent()) {
                    final Map<PotionEffect, Float> effectChances = this.foodProperty.getEffectChances().get();
                    final float consumeSeconds = this.foodProperty.getConsumeSeconds().get();
                    Consumable.Builder consumableBuilder = Consumable.consumable();
                    consumableBuilder.consumeSeconds(consumeSeconds).animation(ItemUseAnimation.EAT).hasConsumeParticles(true);
                    final List<ConsumeEffect> consumeEffects = new ArrayList<>();
                    effectChances.forEach(
                            (effect, chance) -> consumeEffects.add(
                                    ConsumeEffect.applyStatusEffects(List.of(effect), chance)
                            )
                    );
                    consumableBuilder.addEffects(consumeEffects);
                    itemStack.setData(DataComponentTypes.CONSUMABLE, consumableBuilder.build());
                }
            }
            if (this.weaponProperty != WeaponProperty.EMPTY) {
                builder.addModifier(
                        Attribute.ATTACK_DAMAGE,
                        new AttributeModifier(
                                NeoArtisan.getArtisanItemAttackDamageKey(),
                                weaponProperty.damage(),
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlotGroup.MAINHAND

                        ));
                builder.addModifier(
                        Attribute.ATTACK_SPEED,
                        new AttributeModifier(
                                NeoArtisan.getArtisanItemAttackSpeedKey(),
                                weaponProperty.speed(),
                                AttributeModifier.Operation.ADD_NUMBER,
                                EquipmentSlotGroup.MAINHAND
                        )
                );
                builder.addModifier(
                        Attribute.ATTACK_KNOCKBACK,
                        new AttributeModifier(
                                NeoArtisan.getArtisanItemAttackKnockbackKey(),
                                weaponProperty.knockback(),
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
                    var slots = this.rawMaterial.getEquipmentSlot();
                    if (slots == EquipmentSlot.HAND) throw new IllegalArgumentException("You can not set null slot in a unequipped item!");
                    builder.addModifier(
                            Attribute.ARMOR,
                            new AttributeModifier(
                                    this.registryId,
                                    this.armorProperty.armor(),
                                    AttributeModifier.Operation.ADD_NUMBER,
                                    slots.getGroup()
                            )
                    );
                    builder.addModifier(
                            Attribute.ARMOR_TOUGHNESS,
                            new AttributeModifier(
                                    this.registryId,
                                    this.armorProperty.armorToughness(),
                                    AttributeModifier.Operation.ADD_NUMBER,
                                    slots.getGroup()
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
            return itemStack;
        }

        private static Component toNameComponent(String s) {
            s = "<white><italic:false>" + s;
            return MiniMessage.miniMessage().deserialize(s);
        }

        private static List<Component> toLoreComponentList(List<String> list) {
            List<Component> newList = new ArrayList<>();
            for (String s : list) {
                newList.add(
                        MiniMessage.miniMessage().deserialize(
                                "<gray><italic:false>" + s
                        )
                );
            }
            return newList;
        }
    }

}
