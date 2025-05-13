package io.github.moyusowo.neoartisanapi.api.attribute;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 自定义玩家属性注册表API，用于管理自定义玩家属性。
 *
 * <p>通过此接口可以注册、获取和管理服务器中属性系统的玩家属性。</p>
 *
 * @apiNote 不保证API的线程安全
 */
@SuppressWarnings("unused")
public interface PlayerAttributeRegistry {

    /**
     * 获取玩家属性注册表的实例。
     *
     * @return 玩家属性注册表的实例
     */
    static PlayerAttributeRegistry getPlayerAttributeRegistryManager() {
        return Bukkit.getServicesManager().load(PlayerAttributeRegistry.class);
    }

    /**
     * 注册一个玩家属性。
     *
     *
     * @param attributeKey 属性的命名空间键（不能为null）
     * @param typeName 属性类型名称（不能为null或空）
     * @throws IllegalArgumentException 如果属性已注册或参数无效
     */
    void registerPlayerAttribute(@NotNull NamespacedKey attributeKey, @NotNull String typeName);

    /**
     * 检查是否已注册指定的玩家属性。
     *
     * @param attributeKey 要检查的属性键（不能为null）
     * @return 如果属性已注册返回true，否则返回false
     */
    boolean hasPlayerAttribute(@NotNull NamespacedKey attributeKey);

    /**
     * 获取玩家属性的类型名称。
     *
     * @param attributeKey 要查询的属性键（不能为null）
     * @return 注册时指定的类型名称（不会为null）
     * @throws IllegalArgumentException 如果属性未注册
     * @apiNote 调用该方法之前应该总是调用 {@link AttributeRegistry#hasGlobalAttribute(NamespacedKey)}
     */
    @NotNull String getPlayerAttributeTypeName(@NotNull NamespacedKey attributeKey);

    @ApiStatus.Experimental
    @Nullable <T> T getPlayerAttribute(@NotNull Player player, @NotNull NamespacedKey attributeKey);

    @ApiStatus.Experimental
    <T> void setPlayerAttribute(@NotNull Player player, @NotNull NamespacedKey attributeKey, T value);
}
