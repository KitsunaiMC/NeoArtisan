package io.github.moyusowo.neoartisanapi.api.item;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 自定义物品注册表核心接口，提供完整的自定义物品生命周期管理。
 *
 * <p>此接口负责：</p>
 * <ul>
 *   <li>自定义物品的注册与配置</li>
 *   <li>物品实例的获取与验证</li>
 *   <li>物品属性的动态读写</li>
 * </ul>
 *
 * <p>通过 {@link org.bukkit.Bukkit#getServicesManager()} 获取实例。</p>
 *
 * @apiNote 部分方法涉及持久化操作，请在主线程调用
 * @see ArtisanItem
 * @see org.bukkit.plugin.ServicesManager
 */
@SuppressWarnings("unused")
public interface ItemRegistry {

    /**
     * 获取自定义物品注册表管理器的实例。
     *
     * @return 自定义物品注册表管理器的实例
     */
    static ItemRegistry getItemRegistryManager() {
        return Bukkit.getServicesManager().load(ItemRegistry.class);
    }

    /**
     * 构建自定义物品的构建器接口。
     * <p>使用示例：
     * <pre>{@code
     * ItemRegistryAPI api; //先获取注册表实例
     * api.builder()
     *     .registryId(new NamespacedKey(plugin, "sword"))
     *     .rawMaterial(Material.DIAMOND_SWORD)
     *     .displayName("传奇之剑");
     * }</pre>
     */
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
        @NotNull Builder customModelData(int customModelData);

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
         * 设置物品的食物属性配置。
         *
         * @param foodProperty 食物属性配置（不能为null，使用 {@link FoodProperty#EMPTY} 表示无属性）
         * @return 当前构建器实例
         * @throws IllegalArgumentException 如果foodProperty为null
         * @see FoodProperty
         */
        @NotNull Builder foodProperty(@NotNull FoodProperty foodProperty);

        /**
         * 设置物品的武器属性配置。
         *
         * @param weaponProperty 武器属性配置（不能为null，使用 {@link WeaponProperty#EMPTY} 表示无属性）
         * @return 当前构建器实例
         * @throws IllegalArgumentException 如果weaponProperty为null
         * @see WeaponProperty
         */
        @NotNull Builder weaponProperty(@NotNull WeaponProperty weaponProperty);

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
         * @param armorProperty 护甲属性配置（不能为null，使用 {@link ArmorProperty#EMPTY} 表示无属性）
         * @return 当前构建器实例
         * @throws IllegalArgumentException 如果armorProperty为null
         * @see ArmorProperty
         */
        @NotNull Builder armorProperty(ArmorProperty armorProperty);

        /**
         * 设置物品的属性系统配置。
         *
         * @param attributeProperty 属性系统配置（不能为null，使用 {@link AttributeProperty#empty()} 表示无属性）
         * @return 当前构建器实例
         * @throws IllegalArgumentException 如果attributeProperty为null
         * @see AttributeProperty
         */
        @NotNull Builder attributeProperty(AttributeProperty attributeProperty);

        @NotNull Builder cropId(NamespacedKey blockKey);

    }

    /**
     * 创建一个新的物品构建器实例。
     *
     * @return 新的物品构建器实例（不会为null）
     */
    @NotNull Builder builder();

    /**
     * 通过构建器直接注册物品（推荐使用）。
     *
     * @param builder 物品构建器实例（不能为null）
     * @throws IllegalArgumentException 如果builder为null或包含无效参数
     */
    void registerItem(@NotNull Builder builder);

    /**
     * 从物品堆解析注册ID。
     *
     * <p>minecraft原版物品会返回原版物品的命名空间ID。</p>
     *
     * @param itemStack 目标物品堆（不能为null）
     * @return 对应的注册ID（不会为null）
     */
    @NotNull NamespacedKey getRegistryId(@NotNull ItemStack itemStack);

    /**
     * 检查指定ID的自定义物品是否已注册。
     *
     * <p>该方法包括minecraft原版物品</p>
     *
     * @param registryId 要检查的物品ID（可为null）
     * @return 如果物品存在则返回true，否则返回false
     * @apiNote 此方法总是立即返回，不会抛出异常
     */
    boolean hasItem(@Nullable NamespacedKey registryId);

    /**
     * 创建指定数量的指定命名空间ID的物品堆。
     *
     * <p>该方法兼容minecraft原版物品命名空间ID</p>
     *
     * @param registryId 物品注册ID（不能为null）
     * @param count 物品数量（超过堆叠上限的数值会自动变为上限值）
     * @return 新的物品堆实例（不会为null）
     * @throws IllegalArgumentException 如果物品未注册或参数无效
     */
    @NotNull ItemStack getItemStack(NamespacedKey registryId, int count);

    /**
     * 创建数量为1的指定命名空间ID的物品堆。
     *
     * <p>该方法兼容minecraft原版物品命名空间ID</p>
     *
     * @param registryId 物品注册ID（不能为null）
     * @return 新的物品堆实例（不会为null）
     */
    @NotNull ItemStack getItemStack(NamespacedKey registryId);

    /**
     * 通过ID验证是否为有效自定义物品。
     *
     * @param registryId 要检查的物品ID（可为null）
     * @return 如果是注册的自定义物品返回true
     */
    boolean isArtisanItem(@Nullable NamespacedKey registryId);

    /**
     * 通过物品堆验证是否为自定义物品。
     *
     * @param itemStack 要检查的物品堆（可为null）
     * @return 如果是本系统注册的自定义物品返回true
     */
    boolean isArtisanItem(@Nullable ItemStack itemStack);

    /**
     * 通过ID获取物品API实例。
     *
     * @param registryId 物品注册ID（不能为null）
     * @return 物品API接口实例（不会为null）
     * @throws IllegalArgumentException 如果物品未注册
     * @see ArtisanItem
     * @apiNote 调用该方法之前应该总是调用 {@link ItemRegistry#isArtisanItem(NamespacedKey)} 以检查有效性
     */
    @NotNull
    ArtisanItem getArtisanItem(@NotNull NamespacedKey registryId);

    /**
     * 通过物品堆获取物品API实例。
     *
     * @param itemStack 目标物品堆（不能为null）
     * @return 物品API接口实例（不会为null）
     * @throws IllegalArgumentException 如果不是有效自定义物品
     * @see #getArtisanItem(NamespacedKey)
     * @apiNote 调用该方法之前应该总是调用 {@link ItemRegistry#isArtisanItem(ItemStack)} 以检查有效性
     */
    @NotNull
    ArtisanItem getArtisanItem(ItemStack itemStack);

    /**
     * 读取物品堆上的动态属性值。
     *
     * @param itemStack 目标物品堆（不能为null）
     * @param attributeKey 属性键（不能为null）
     * @return 属性值，如果不存在返回null
     * @param <T> 属性值类型
     * @throws IllegalStateException 如果属性类型不匹配
     * @see #setItemstackAttributeValue(ItemStack, NamespacedKey, Object)
     */
    @Nullable <T> T getItemstackAttributeValue(@NotNull ItemStack itemStack, @NotNull NamespacedKey attributeKey);

    /**
     * 设置物品堆上的动态属性值。
     *
     * @param itemStack 目标物品堆（不能为null）
     * @param attributeKey 属性键（不能为null）
     * @param value 要设置的值（不能为null）
     * @param <T> 属性值类型
     * @throws IllegalArgumentException 如果属性未注册或值类型无效
     * @see #getItemstackAttributeValue(ItemStack, NamespacedKey)
     */
    <T> void setItemstackAttributeValue(@NotNull ItemStack itemStack, @NotNull NamespacedKey attributeKey, @NotNull T value);
}
