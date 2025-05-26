package io.github.moyusowo.neoartisanapi.api.attribute;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

@ApiStatus.AvailableSince(value = "1.0.0")
interface AttributeRegistryBase {

    /**
     * 注册一个属性。
     *
     *
     * @param attributeKey 属性的命名空间键（不能为null）
     * @param pdcType PDC属性类型类（不能为null或空）
     * @throws IllegalArgumentException 如果属性已注册或参数无效
     */
    void registerAttribute(@NotNull NamespacedKey attributeKey, @NotNull PersistentDataType<?, ?> pdcType);

    /**
     * 检查是否已注册指定的属性。
     *
     * @param attributeKey 要检查的属性键（不能为null）
     * @return 如果属性已注册返回true，否则返回false
     */
    boolean hasAttribute(@NotNull NamespacedKey attributeKey);

    /**
     * 获取属性对应的Java原生类型。
     *
     * @param attributeKey 要查询的属性键（不可为null）
     * @return 属性的Java类型（不会返回null）
     * @throws IllegalArgumentException 如果属性未注册
     */
    @NotNull Class<?> getAttributeJavaType(@NotNull NamespacedKey attributeKey);

    /**
     * 获取属性对应的持久化数据类型。
     * <p>
     * 返回的 {@link PersistentDataType} 必须与注册时提供的类型严格一致。
     *
     * @param attributeKey 要查询的属性键（不可为null）
     * @return 属性的持久化数据类型实例（不会返回null）
     * @throws IllegalArgumentException 如果属性未注册
     * @see PersistentDataType
     */
    @NotNull PersistentDataType<?, ?> getAttributePDCType(@NotNull NamespacedKey attributeKey);
}
