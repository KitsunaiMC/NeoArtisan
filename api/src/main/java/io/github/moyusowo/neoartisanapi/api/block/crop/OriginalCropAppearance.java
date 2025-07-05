package io.github.moyusowo.neoartisanapi.api.block.crop;

import org.jetbrains.annotations.NotNull;

/**
 * 原版作物外观配置
 *
 * <p>不能用资源包覆盖！该选项是给使用原版贴图的作物准备的！</p>
 *
 */
public final class OriginalCropAppearance implements CropAppearance {

    public final CropType cropType;
    public final int age;

    public OriginalCropAppearance(@NotNull CropType cropType, int age) {
        this.cropType = cropType;
        if (age < 0 || age >= cropType.maxAge) throw new IllegalArgumentException("Age can not be larger than maxAge!");
        this.age = age;
    }

    public enum CropType {
        WHEAT(8),
        POTATO(8),
        CARROT(8),
        BEETROOT(4),
        PITCHER_PLANT(5),
        TORCH_FLOWER(3);

        private final int maxAge;

        CropType(int maxAge) {
            this.maxAge = maxAge;
        }
    }
}
