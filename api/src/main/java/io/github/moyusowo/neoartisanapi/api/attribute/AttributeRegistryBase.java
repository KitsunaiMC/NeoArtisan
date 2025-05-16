package io.github.moyusowo.neoartisanapi.api.attribute;

import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

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

    @NotNull Class<?> getAttributeJavaType(@NotNull NamespacedKey attributeKey);

    @NotNull PersistentDataType<?, ?> getAttributePDCType(@NotNull NamespacedKey attributeKey);
}
