package io.github.moyusowo.neoartisanapi.api.block.block;

import io.github.moyusowo.neoartisanapi.api.block.block.base.ArtisanBaseBlock;
import io.github.moyusowo.neoartisanapi.api.block.block.base.ArtisanBlocks;
import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.util.SoundProperty;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.util.BuilderFactoryUtil;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 表示一个自定义作物方块的接口，扩展基础 {@link ArtisanBaseBlock} 功能。
 *
 * <p>
 * 提供作物特有的生长控制功能，包括骨粉加速生长行为配置。
 * </p>
 *
 * @see ArtisanBaseBlock 基础方块接口
 */
public interface ArtisanCropBlock extends ArtisanBaseBlock {
    @NotNull
    static Builder builder() {
        return BuilderFactoryUtil.getBuilder(BuilderFactory.class).builder();
    }

    interface BuilderFactory {
        @NotNull
        Builder builder();
    }

    /**
     * 获取骨粉作用的最小生长增量
     *
     * @return 每次骨粉使用至少推进的生长阶段数 (≥0)
     * @implNote 应与 {@link #getBoneMealMaxGrowth()} 构成有效范围
     */
    int getBoneMealMinGrowth();

    /**
     * 获取骨粉作用的最大生长增量
     *
     * @return 每次骨粉使用最多推进的生长阶段数 (≥minGrowth)
     */
    int getBoneMealMaxGrowth();

    /**
     * 生成随机的骨粉生长增量
     *
     * @return 介于 minGrowth 和 maxGrowth 之间的随机值
     * @implSpec 满足 minGrowth ≤ 返回值 ≤ maxGrowth
     */
    int generateBoneMealGrowth();

    void onRandomTick(ArtisanBlockData cropData);

    @Override
    default boolean hasBlockEntity() {
        return false;
    }

    @Override
    @NotNull
    default ArtisanBlocks getType() {
        return ArtisanBlocks.CROP;
    }

    interface Builder {
        @NotNull
        Builder blockId(@NotNull NamespacedKey blockId);

        @NotNull
        Builder states(@NotNull List<ArtisanBaseBlockState> states);

        @NotNull
        Builder placeSound(@NotNull SoundProperty placeSoundProperty);

        @NotNull
        Builder boneMealMinGrowth(int boneMealMinGrowth);

        @NotNull
        Builder boneMealMaxGrowth(int boneMealMaxGrowth);

        @NotNull
        ArtisanCropBlock build();
    }
}
