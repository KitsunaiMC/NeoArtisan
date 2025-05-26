package io.github.moyusowo.neoartisanapi.api.block.thin;

/**
 * 薄型自定义方块外观配置（利用原版压力板未使用的状态）
 * <p>
 * <b>原版状态映射策略：</b>
 * <ul>
 *   <li>原版轻重压力板始终使用power=0/power=1，2-15为未使用状态</li>
 *   <li>薄型自定义方块可安全占用2-15作为不同方块状态</li>
 *   <li>传入power=0/power=1将抛出异常（避免与原版冲突）</li>
 * </ul>
 *
 * @implNote 实际客户端显示需通过资源包覆盖
 */
public final class ThinBlockAppearance {

    public final PressurePlateAppearance appearance;
    public final int power;

    public ThinBlockAppearance(PressurePlateAppearance appearance, int power) {
        if (power <= 1 || power > 15) throw new IllegalArgumentException();
        this.appearance = appearance;
        this.power = power;
    }

    public enum PressurePlateAppearance {
        LIGHT_WEIGHTED_PRESSURE_PLATE,
        HEAVY_WEIGHTED_PRESSURE_PLATE
    }
}
