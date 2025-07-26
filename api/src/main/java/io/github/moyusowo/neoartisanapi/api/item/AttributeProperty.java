package io.github.moyusowo.neoartisanapi.api.item;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Collection;
import java.util.List;

/**
 * 物品属性存储容器接口，用于管理物品的初始属性。
 *
 * <p>所有属性都会在物品的 `ItemStack` 创建时被写入内部的 {@link org.bukkit.persistence.PersistentDataContainer} 数据容器中</p>
 *
 * @see #empty()
 */
@ApiStatus.NonExtendable
public interface AttributeProperty {

    /**
     * 获取新的自定义物品属性配置实例。
     *
     * @return 新的自定义物品属性配置实例
     */
    static AttributeProperty empty() {
        return Bukkit.getServicesManager().load(AttributeProperty.class);
    }

    /**
     * 添加属性。
     *
     * @param key 属性标识（不可为null）
     * @param type 属性类型（不可为null）
     * @param value 属性值（不可为null）
     * @throws IllegalArgumentException 如果参数为null或值类型不合法
     */
    <P, C> AttributeProperty addAttribute(@NotNull NamespacedKey key, @NotNull PersistentDataType<P, C> type, @NotNull C value);

    /**
     * 检查是否存在指定的属性。
     *
     * @param key 要检查的属性标识
     * @return 如果存在该属性返回true
     */
    boolean hasAttribute(@NotNull NamespacedKey key);

    /**
     * 获取全局属性值。
     *
     * @param <T> 返回值类型
     * @param key 属性标识（不可为null）
     * @param type 属性类型（不可为null）
     * @return 存储的属性值（不会为null）
     * @throws IllegalArgumentException 如果属性不存在或类型不匹配
     */
    @NotNull <T> T getAttribute(@NotNull NamespacedKey key, @NotNull Class<T> type);

    /**
     * 判断当前容器是否为空配置。
     *
     * @return 如果未设置任何属性（全局/物品）返回true
     */
    boolean isEmpty();

    /**
     * 获取所有已注册的所有属性的 `NamespacedKey`。
     *
     * @return 物品属性键列表
     */
    @Unmodifiable
    @NotNull
    Collection<NamespacedKey> getAttributeKeys();

    void setPersistenceDataContainer(@NotNull PersistentDataContainer persistenceDataContainer);
}
