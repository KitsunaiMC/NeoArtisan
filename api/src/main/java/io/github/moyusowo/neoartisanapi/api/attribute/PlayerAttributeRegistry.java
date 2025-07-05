package io.github.moyusowo.neoartisanapi.api.attribute;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 自定义玩家属性注册表API，用于管理自定义玩家属性。
 */
public interface PlayerAttributeRegistry extends AttributeRegistryBase {

    @Nullable <T> T getPlayerAttribute(@NotNull Player player, @NotNull NamespacedKey attributeKey);

    <T> void setPlayerAttribute(@NotNull Player player, @NotNull NamespacedKey attributeKey, T value);
}
