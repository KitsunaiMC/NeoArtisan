package io.github.moyusowo.neoartisanapi.api.block.crop;

public class TripwireAppearance implements CropAppearance {

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
