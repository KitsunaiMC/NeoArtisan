package io.github.moyusowo.neoartisanapi.api.block.crop;

/**
 * 绊线型作物外观配置
 * <p>
 * <b>状态组合规则：</b>
 * <ul>
 *   <li>disarmed与powered不能同为true，因为原版外观需要正确映射</li>
 *   <li>其他方向连接状态可自由组合</li>
 * </ul>
 *
 * @implNote 需确保资源包中对应状态有特殊纹理
 */
public final class TripwireAppearance implements CropAppearance {

    private final boolean[] b;

    public TripwireAppearance(boolean attached, boolean disarmed, boolean powered, boolean east, boolean south, boolean west, boolean north) {
        if (disarmed && powered) throw new IllegalArgumentException();
        b = new boolean[]{attached, disarmed, powered, east, south, west, north};
    }

    public boolean get(BlockStateProperty blockStateProperty) {
        return b[blockStateProperty.n];
    }

    public enum BlockStateProperty {
        ATTACHED(0),
        DISARMED(1),
        POWERED(2),
        EAST(3),
        SOUTH(4),
        WEST(5),
        NORTH(6);

        private final int n;

        BlockStateProperty(int n) {
            this.n = n;
        }
    }
}
