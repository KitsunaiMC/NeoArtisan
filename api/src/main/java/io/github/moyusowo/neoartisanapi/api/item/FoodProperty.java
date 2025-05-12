package io.github.moyusowo.neoartisanapi.api.item;

import org.jetbrains.annotations.NotNull;

/**
 * 表示食物物品的属性配置。
 * <p>注意事项：
 * <ul>
 *   <li>nutrition（营养值）对应原版的饥饿值恢复量</li>
 *   <li>saturation（饱和度）决定饱食度衰减速度</li>
 * </ul>
 * 示例：
 * <pre>{@code
 * // 创建金苹果食物属性
 * FoodProperty goldenApple = new FoodProperty(4, 9.6f, false);
 * }</pre>
 *
 * @param nutrition 营养值（不能为null）
 * @param saturation 饱和度（不能为null）
 * @param canAlwaysEat 是否在饱食时仍可食用
 */
@SuppressWarnings("unused")
public record FoodProperty(@NotNull Integer nutrition, @NotNull Float saturation, boolean canAlwaysEat) {
    /**
     * 空食物属性配置，表示物品不可食用。
     * <p>等效于 {@code new FoodProperty(null, null, false)}
     */
    public static final FoodProperty EMPTY = new FoodProperty(null, null, false);
}
