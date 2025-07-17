package io.github.moyusowo.neoartisanapi.api.block.state.appearance.crop;

import io.github.moyusowo.neoartisanapi.api.block.state.appearance.CropAppearance;
import org.jetbrains.annotations.NotNull;

public record OriginalCropAppearance(
        @NotNull OriginalCropMaterial originalCropMaterial,
        int age
) implements CropAppearance {

    public OriginalCropAppearance {
        if (age < 0 || age >= originalCropMaterial.maxAge) throw new IllegalArgumentException("Age can not be larger than maxAge!");
    }

    public enum OriginalCropMaterial {
        WHEAT(8),
        POTATO(8),
        CARROT(8),
        BEETROOT(4),
        PITCHER_PLANT(5),
        TORCH_FLOWER(3);

        private final int maxAge;

        OriginalCropMaterial(int maxAge) {
            this.maxAge = maxAge;
        }
    }
}
