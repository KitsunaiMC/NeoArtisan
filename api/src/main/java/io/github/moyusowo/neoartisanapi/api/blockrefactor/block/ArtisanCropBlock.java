package io.github.moyusowo.neoartisanapi.api.blockrefactor.block;

import io.github.moyusowo.neoartisanapi.api.block.base.sound.SoundProperty;
import io.github.moyusowo.neoartisanapi.api.blockrefactor.blockstate.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.util.BuilderFactoryUtil;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ArtisanCropBlock extends ArtisanBaseBlock {
    /**
     * 获取工厂服务实例
     * <p>
     * 此工厂用于创建全新的 {@link Builder} 实例，确保每次构建过程独立且线程安全。
     * </p>
     *
     * @return 建造器工厂实例（非null）
     * @throws IllegalStateException 如果工厂服务未注册
     * @see Builder 构建器接口
     */
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

    interface Builder {
        @NotNull
        Builder blockId(@NotNull NamespacedKey blockId);

        @NotNull
        Builder stages(@NotNull List<ArtisanBaseBlockState> stages);

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
