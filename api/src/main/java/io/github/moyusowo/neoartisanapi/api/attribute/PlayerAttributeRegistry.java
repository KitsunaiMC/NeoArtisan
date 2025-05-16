package io.github.moyusowo.neoartisanapi.api.attribute;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
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
public interface PlayerAttributeRegistry extends AttributeRegistryBase {

    @ApiStatus.Experimental
    @Nullable <T> T getPlayerAttribute(@NotNull Player player, @NotNull NamespacedKey attributeKey);

    @ApiStatus.Experimental
    <T> void setPlayerAttribute(@NotNull Player player, @NotNull NamespacedKey attributeKey, T value);
}
