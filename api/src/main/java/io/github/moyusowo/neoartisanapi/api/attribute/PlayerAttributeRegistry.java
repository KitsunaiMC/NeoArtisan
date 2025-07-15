package io.github.moyusowo.neoartisanapi.api.attribute;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 自定义玩家属性注册表API，用于管理自定义玩家属性。
 */
public interface PlayerAttributeRegistry extends AttributeRegistryBase {
    /**
     * 读取玩家的属性值。
     *
     * @param player 目标玩家（不能为null）
     * @param attributeKey 属性键（不能为null）
     * @return 属性值，如果不存在返回null
     * @param <T> 属性值类型
     * @throws IllegalStateException 如果属性类型不匹配
     * @see #setPlayerAttribute(Player, NamespacedKey, Object)
     * @apiNote 调用该方法之前应该总是调用 {@link #hasAttribute(NamespacedKey)} 以确保属性已注册
     */
    @Nullable <T> T getPlayerAttribute(@NotNull Player player, @NotNull NamespacedKey attributeKey);

    /**
     * 设置玩家的属性值。
     *
     * @param player 目标玩家（不能为null）
     * @param attributeKey 属性键（不能为null）
     * @param value 要设置的值（不能为null）
     * @param <T> 属性值类型
     * @throws IllegalArgumentException 如果属性未注册或值类型无效
     * @see #getPlayerAttribute(Player, NamespacedKey)
     * @apiNote 调用该方法之前应该总是调用 {@link #hasAttribute(NamespacedKey)} 以确保属性已注册
     */
    <T> void setPlayerAttribute(@NotNull Player player, @NotNull NamespacedKey attributeKey, T value);
}
