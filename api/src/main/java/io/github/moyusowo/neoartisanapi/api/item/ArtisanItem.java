package io.github.moyusowo.neoartisanapi.api.item;

import io.github.moyusowo.neoartisanapi.api.item.factory.ItemBuilderFactory;
import io.papermc.paper.datacomponent.item.CustomModelData;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

/**
 * 自定义物品核心接口，提供对自定义物品属性和特性的访问。
 *
 * <p>此接口代表一个在系统中注册的自定义物品实例，包含物品的基础信息
 * 和各种扩展属性。所有自定义物品都应有唯一的 {@link NamespacedKey} 标识。</p>
 *
 * @since 2.0.0
 */
@SuppressWarnings({"unused"})
public interface ArtisanItem {

    static ItemBuilderFactory factory() {
        return Bukkit.getServicesManager().load(ItemBuilderFactory.class);
    }

    /**
     * 空命名空间键，物品为空时的物品注册ID
     *
     */
    NamespacedKey EMPTY = new NamespacedKey("neoartisan", "empty_item_registry_id");

    /**
     * 通过物品堆判断是否为此自定义物品。
     *
     * @param itemStack 要比较的物品堆（不能为null）
     * @return 如果物品堆代表此自定义物品返回true，否则返回false
     */
    boolean equals(@NotNull ItemStack itemStack);

    /**
     * 通过注册ID判断是否为此自定义物品。
     *
     * @param registryId 要比较的注册ID（不能为null）
     * @return 如果ID匹配返回true，否则返回false
     */
    boolean equals(@NotNull NamespacedKey registryId);

    /**
     * 获取此自定义物品的唯一注册ID。
     *
     * @return 物品的命名空间键（不会为null）
     */
    @NotNull NamespacedKey getRegistryId();

    /**
     * 获取此自定义物品的ItemStack。
     *
     * @return 自定义物品的ItemStack（不会为null）
     */
    @NotNull ItemStack getItemStack();

    /**
     * 获取此自定义物品的指定数量ItemStack。
     *
     * @return 自定义物品的指定数量ItemStack（不会为null）
     */
    @NotNull ItemStack getItemStack(int count);

    /**
     * 检查此物品是否保留原版合成配方。
     *
     * @return 如果保留原版合成返回true，否则返回false
     */
    boolean hasOriginalCraft();

    /**
     * 获取此物品的属性系统配置。
     *
     * @return 属性配置对象，不会返回null，可以使用 {@link AttributeProperty#isEmpty()} 判空
     * @see AttributeProperty
     */
    @NotNull
    AttributeProperty getAttributeProperty();

    /**
     * 获取此物品右键放置出的方块。
     *
     * @return 此物品右键放置出的方块的命名空间ID，如果没有返回 {@code null}
     */
    @Nullable NamespacedKey getBlockId();

    /**
     * 构建复杂自定义物品的构建器接口，可以完全自定义物品堆的各项数据。
     * <p>使用示例：
     * <pre>{@code
     * ItemStack itemStack = ItemStack.of(Material.DIAMOND_SWORD);
     * itemStack.setData(DataComponentTypes.ITEM_NAME, Component.text("<red>传奇之剑");
     * ArtisanItem newItem = ArtisanItem.complexBuilder()
     *     .registryId(new NamespacedKey(plugin, "sword"))
     *     .itemStack(itemStack)
     *     .build();
     * }</pre>
     * @since 2.0.0
     */
    interface ComplexBuilder {

        /**
         * 设置物品的唯一标识键。
         *
         * @param registryId 物品的唯一标识键（不能为null）
         * @return 当前构建器实例
         * @throws IllegalArgumentException 如果registryId为null
         */
        @NotNull ComplexBuilder registryId(@NotNull NamespacedKey registryId);

        /**
         * 传入自定义物品的ItemStack Supplier，物品数量可任意。
         *
         * <p>传入Supplier后每次请求会调用 {@link Supplier#get()} 以动态生成ItemStack实例。</p>
         *
         * @param itemStackSupplier 自定义物品的ItemStack的提供者（不能为null）
         * @return 当前构建器实例
         * @throws IllegalArgumentException 如果rawMaterial为null
         * @see ItemStack
         */
        @NotNull ComplexBuilder itemStack(@NotNull Supplier<ItemStack> itemStackSupplier);

        /**
         * 设置保留原版合成配方。
         *
         * @return 当前构建器实例
         */
        @NotNull ComplexBuilder hasOriginalCraft();

        /**
         * 设置物品的属性系统配置。
         *
         * @param attributeProperty 属性系统配置（不能为null，使用 {@link AttributeProperty#empty()} 表示无属性）
         * @return 当前构建器实例
         * @throws IllegalArgumentException 如果attributeProperty为null
         * @see AttributeProperty
         */
        @NotNull ComplexBuilder attributeProperty(@NotNull AttributeProperty attributeProperty);

        /**
         * 设置物品右键关联放置的自定义方块。
         *
         * @param blockId 方块ID（不能为null）
         * @return 当前构建器实例
         * @see io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlock
         */
        @NotNull ComplexBuilder blockId(@NotNull NamespacedKey blockId);

        /**
         * 按照所给的参数构建自定义物品。
         *
         * @return 构建的自定义物品实例
         */
        @NotNull ArtisanItem build();

    }

    /**
     * 构建简单自定义物品的构建器接口。
     * <p>使用示例：
     * <pre>{@code
     * ArtisanItem newItem = ArtisanItem.builder()
     *     .registryId(new NamespacedKey(plugin, "sword"))
     *     .rawMaterial(Material.DIAMOND_SWORD)
     *     .displayName("传奇之剑")
     *     .build();
     * }</pre>
     */
    @SuppressWarnings("UnstableApiUsage")
    interface Builder {

        /**
         * 设置物品的唯一标识键。
         *
         * @param registryId 物品的唯一标识键（不能为null）
         * @return 当前构建器实例
         * @throws IllegalArgumentException 如果registryId为null
         */
        @NotNull Builder registryId(@NotNull NamespacedKey registryId);

        /**
         * 设置物品的基础材质类型。
         *
         * @param rawMaterial 基础材质类型（不能为null）
         * @return 当前构建器实例
         * @throws IllegalArgumentException 如果rawMaterial为null
         * @see Material
         */
        @NotNull Builder rawMaterial(@NotNull Material rawMaterial);

        /**
         * 设置是否保留原版合成配方。
         *
         * @param hasOriginalCraft true表示保留原版合成，false表示禁用（默认禁用）
         * @return 当前构建器实例
         */
        @NotNull Builder hasOriginalCraft(boolean hasOriginalCraft);

        /**
         * 设置物品的自定义模型数据。
         *
         * @param customModelData 自定义模型数据ID（必须大于0）
         * @return 当前构建器实例
         * @throws IllegalArgumentException 如果customModelData ≤ 0
         */
        @NotNull Builder customModelData(@NotNull CustomModelData customModelData);

        /**
         * 设置物品的显示名称。
         *
         * @param displayName 物品显示名称（不能为null或空字符串）
         * @return 当前构建器实例
         * @throws IllegalArgumentException 如果displayName为null或空
         */
        @NotNull Builder displayName(@NotNull String displayName);

        /**
         * 用adventure API的文本组件设置物品的显示名称。
         *
         * @param component 物品显示名称（不能为null或空字符串）
         * @return 当前构建器实例
         * @throws IllegalArgumentException 如果displayName为null或空
         */
        @NotNull Builder displayName(@NotNull Component component);

        /**
         * 设置物品的Lore描述。
         *
         * @param lore 物品描述文本列表
         * @return 当前构建器实例
         * @throws IllegalArgumentException 如果lore为null
         */
        @NotNull Builder lore(@NotNull List<String> lore);

        /**
         * 用adventure API的文本组件设置物品的Lore描述。
         *
         * @param lore 物品描述文本列表
         * @return 当前构建器实例
         * @throws IllegalArgumentException 如果lore为null
         */
        @NotNull Builder loreComponent(@NotNull List<Component> lore);

        /**
         * 设置物品的食物属性。
         *
         * @param nutrition 营养值
         * @param saturation 饱和度
         * @param canAlwaysEat 是否在饱食时仍可食用
         * @return 当前构建器实例
         */
        @NotNull Builder foodProperty(int nutrition, float saturation, boolean canAlwaysEat);

        /**
         * 设置物品的武器属性。
         *
         * @param speed 攻击速度
         * @param knockback 击退强度
         * @param damage 基础伤害值
         * @return 当前构建器实例
         */
        @NotNull Builder weaponProperty(float speed, float knockback, float damage);

        /**
         * 设置物品的最大耐久值。
         *
         * @param maxDurability 最大耐久值（必须大于0）
         * @return 当前构建器实例
         * @throws IllegalArgumentException 如果maxDurability ≤ 0
         */
        @NotNull Builder maxDurability(int maxDurability);

        /**
         * 设置物品的护甲属性配置。
         *
         * @param armor 护甲值
         * @param armorToughness 护甲韧性
         * @param slot 装备槽位（null则继承模板物品的穿戴位置，如果没有则不能为null）
         * @return 当前构建器实例
         */
        @NotNull Builder armorProperty(int armor, int armorToughness, @Nullable EquipmentSlot slot);

        /**
         * 设置物品的属性系统配置。
         *
         * @param attributeProperty 属性系统配置（不能为null，使用 {@link AttributeProperty#empty()} 表示无属性）
         * @return 当前构建器实例
         * @throws IllegalArgumentException 如果attributeProperty为null
         * @see AttributeProperty
         */
        @NotNull Builder attributeProperty(@NotNull AttributeProperty attributeProperty);

        /**
         * 设置物品右键关联放置的自定义方块。
         *
         * @param blockId 方块ID（不能为null）
         * @return 当前构建器实例
         * @see io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlock
         */
        @NotNull Builder blockId(@NotNull NamespacedKey blockId);

        /**
         * 设置物品的自定义模型。
         *
         * @param itemModel 自定义模型的命名空间键（不能为null）
         * @return 当前构建器实例
         */
        @NotNull Builder itemModel(@NotNull NamespacedKey itemModel);

        /**
         * 若该物品的Material为PLAYER_HEAD，则可用该方法设置头颅的贴图。
         *
         * <p>若该物品的Material不为PLAYER_HEAD，则该方法不起作用</p>
         *
         * @param textureUrl 头颅贴图的URL链接或对应的Base64编码（不能为null）
         * @param isBase64 提供的字符串是否为Base64编码
         * @return 当前构建器实例
         */
        @NotNull Builder skullProperty(@NotNull String textureUrl, boolean isBase64);

        /**
         * 按照所给的参数构建自定义物品。
         *
         * @return 构建的自定义物品实例
         */
        @NotNull ArtisanItem build();

    }
}
