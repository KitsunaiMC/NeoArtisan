package io.github.moyusowo.neoartisanapi.api.attribute;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 物品堆属性注册表API，用于管理自定义物品堆属性。
 */
public interface ItemStackAttributeRegistry extends AttributeRegistryBase {
    /**
     * 读取物品堆上的动态属性值。
     *
     * @param itemStack 目标物品堆（不能为null）
     * @param attributeKey 属性键（不能为null）
     * @return 属性值，如果不存在返回null
     * @param <T> 属性值类型
     * @throws IllegalStateException 如果属性类型不匹配
     * @see #setItemStackAttributeValue(ItemStack, NamespacedKey, Object)
     * @apiNote 调用该方法之前应该总是调用 {@link #hasAttribute(NamespacedKey)} 以确保属性已注册
     */
    @Nullable
    <T> T getItemStackAttributeValue(@NotNull ItemStack itemStack, @NotNull NamespacedKey attributeKey);

    /**
     * 设置物品堆上的动态属性值。
     *
     * @param itemStack 目标物品堆（不能为null）
     * @param attributeKey 属性键（不能为null）
     * @param value 要设置的值（不能为null）
     * @param <T> 属性值类型
     * @throws IllegalArgumentException 如果属性未注册或值类型无效
     * @see #getItemStackAttributeValue(ItemStack, NamespacedKey)
     * @apiNote 调用该方法之前应该总是调用 {@link #hasAttribute(NamespacedKey)} 以确保属性已注册
     */
    <T> void setItemStackAttributeValue(@NotNull ItemStack itemStack, @NotNull NamespacedKey attributeKey, @NotNull T value);
}
