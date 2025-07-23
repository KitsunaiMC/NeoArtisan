package io.github.moyusowo.neoartisanapi.api.item;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;

/**
 * 自定义物品注册表核心接口，提供完整的自定义物品生命周期管理。
 *
 * <p>此接口负责：</p>
 * <ul>
 *   <li>自定义物品的注册与配置</li>
 *   <li>物品实例的获取与验证</li>
 * </ul>
 *
 * <p>通过 {@link org.bukkit.Bukkit#getServicesManager()} 获取实例。</p>
 *
 * @apiNote 部分方法涉及持久化操作，请在主线程调用
 * @see ArtisanItem
 * @see org.bukkit.plugin.ServicesManager
 */
public interface ItemRegistry {

    /**
     * 注册自定义物品。
     *
     * @param artisanItem 自定义物品实例（不能为null）
     * @throws IllegalArgumentException 如果builder为null或包含无效参数
     */
    void registerItem(@NotNull ArtisanItem artisanItem);

    void registerTagToMaterial(@NotNull Material material, @NotNull String... tags);

    /**
     * 从物品堆解析注册ID。
     *
     * <p>minecraft原版物品会返回原版物品的命名空间ID。</p>
     *
     * <p>空物品会返回 {@link ArtisanItem#EMPTY}。</p>
     *
     * @param itemStack 目标物品堆（可为null）
     * @return 对应的注册ID（不会为null）
     */
    @NotNull NamespacedKey getRegistryId(@Nullable ItemStack itemStack);

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

    @NotNull
    @Unmodifiable
    Collection<NamespacedKey> getIdByTag(String tag);

    @NotNull
    @Unmodifiable
    Collection<String> getTagsById(NamespacedKey id);
}
