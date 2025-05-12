package io.github.moyusowo.neoartisanapi.api.item;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 表示武器物品的战斗属性配置。
 * <p>属性说明：
 * <ul>
 *   <li>speed（攻击速度） - 数值越小攻击间隔越短</li>
 *   <li>knockback（击退） - 击退距离倍数（默认武器无此属性）</li>
 *   <li>damage（伤害） - 基础伤害值</li>
 * </ul>
 * 示例：
 * <pre>{@code
 * // 创建钻石剑武器属性
 * WeaponProperty diamondSword = new WeaponProperty(1.6f, 1.0f, 7.0f);
 * }</pre>
 *
 * @param speed 攻击速度（不可为null）
 * @param knockback 击退强度（可可为null）
 * @param damage 基础伤害值（可为null）
 */
@SuppressWarnings("unused")
public record WeaponProperty(@NotNull Float speed, @NotNull Float knockback, @NotNull Float damage) {
    public static final WeaponProperty EMPTY = new WeaponProperty(null, null, null);
}
