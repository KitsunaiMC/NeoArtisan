package io.github.moyusowo.neoartisanapi.api.block.crop;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlock;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockState;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ArtisanCrop extends ArtisanBlock {

    static Builder builder() {
        return Bukkit.getServicesManager().load(ArtisanCrop.Builder.class);
    }

    @NotNull ArtisanCropState getState(int n);

    int getBoneMealMinGrowth();

    int getBoneMealMaxGrowth();

    int generateBoneMealGrowth();

    interface Builder {

        Builder blockId(NamespacedKey blockId);

        Builder stages(List<ArtisanCropState> stages);

        Builder boneMealMinGrowth(int boneMealMinGrowth);

        Builder boneMealMaxGrowth(int boneMealMaxGrowth);

        ArtisanBlock build();
    }
}
