package io.github.moyusowo.neoartisanapi.api.item;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * 物品属性存储容器接口，用于管理物品的全局默认属性和实例初始属性。
 *
 * <p>本接口实例通过 {@link #createAttributeProperty()} 工厂方法获取，通常不应直接实现。</p>
 *
 * <h3>属性存储机制：</h3>
 * <ul>
 *   <li><b>全局属性</b> - 作为模板值存储，修改将影响所有关联物品</li>
 *   <li><b>物品堆属性</b> - 作为新建物品实例的初始值存储，不影响已有实例</li>
 * </ul>
 *
 * @see #createAttributeProperty()
 */
@SuppressWarnings("unused")
public interface AttributeProperty {

    /**
     * 获取新的自定义物品属性配置实例。
     *
     * @return 新的自定义物品属性配置实例
     */
    static AttributeProperty createAttributeProperty() {
        return Bukkit.getServicesManager().load(AttributeProperty.class);
    }

    /**
     * 获取空属性容器（所有属性未设置状态）。
     *
     * @return 静态空实例，不包含任何属性配置
     */
    AttributeProperty empty();

    /**
     * 添加/更新全局模板属性。
     *
     * @param attributeKey 属性标识（不可为null）
     * @param value 属性值（不可为null）
     * @throws IllegalArgumentException 如果参数为null或值类型不合法
     */
    void addGlobalAttribute(@NotNull NamespacedKey attributeKey, @NotNull Object value);

    /**
     * 添加/更新物品实例初始属性。
     *
     * @param attributeKey 属性标识（不可为null）
     * @param value 新建物品时的默认值（不可为null）
     * @throws IllegalArgumentException 如果参数为null或值类型不合法
     */
    void addItemstackAttribute(@NotNull NamespacedKey attributeKey, @NotNull Object value);

    /**
     * 检查是否存在指定的全局属性。
     *
     * @param attributeKey 要检查的属性标识
     * @return 如果存在该全局属性返回true
     */
    boolean hasGlobalAttribute(@NotNull NamespacedKey attributeKey);

    /**
     * 检查是否存在指定的物品实例属性。
     *
     * @param attributeKey 要检查的属性标识
     * @return 如果存在该物品属性返回true
     */
    boolean hasItemstackAttribute(@NotNull NamespacedKey attributeKey);

    /**
     * 获取全局属性值。
     *
     * @param <T> 返回值类型
     * @param attributeKey 属性标识（不可为null）
     * @return 存储的属性值（不会为null）
     * @throws IllegalArgumentException 如果属性不存在或类型不匹配
     */
    @NotNull <T> T getGlobalAttributeValue(@NotNull NamespacedKey attributeKey);

    /**
     * 获取物品实例属性初始值。
     *
     * @param <T> 返回值类型
     * @param attributeKey 属性标识（不可为null）
     * @return 存储的属性值（不会为null）
     * @throws IllegalArgumentException 如果属性不存在或类型不匹配
     */
    @NotNull <T> T getItemstackAttributeValue(@NotNull NamespacedKey attributeKey);

    /**
     * 判断当前容器是否为空配置。
     *
     * @return 如果未设置任何属性（全局/物品）返回true
     */
    boolean isEmpty();

    /**
     * 获取所有已注册的物品实例属性键。
     *
     * @return 物品属性键数组，无属性时返回空数组（不会为null）
     */
    NamespacedKey[] getItemstackAttributeKeys();
}
