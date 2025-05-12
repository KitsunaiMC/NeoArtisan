package io.github.moyusowo.neoartisanapi.api.item;

import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 表示护甲物品的属性配置。
 * <p>使用示例：
 * <pre>{@code
 * // 创建钻石胸甲属性
 * ArmorProperty diamondChestplate = new ArmorProperty(8, 2, EquipmentSlot.CHEST);
 * }</pre>
 *
 * @param armor 护甲值（可选，null表示无护甲加成）
 * @param armorToughness 护甲韧性（可选，null表示无韧性加成）
 * @param slot 装备槽位（必选）
 */
@SuppressWarnings("unused")
public record ArmorProperty(@Nullable Integer armor, @Nullable Integer armorToughness, @NotNull EquipmentSlot slot) {
    /**
     * 空护甲属性配置，表示物品无护甲特性。
     * <p>等效于 {@code new ArmorProperty(null, null, null)}
     */
    public static final ArmorProperty EMPTY = new ArmorProperty(null, null, null);
}
