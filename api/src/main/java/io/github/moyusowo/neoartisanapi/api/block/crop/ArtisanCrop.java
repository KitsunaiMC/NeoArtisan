package io.github.moyusowo.neoartisanapi.api.block.crop;

import io.github.moyusowo.neoartisanapi.api.block.base.sound.SoundProperty;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlock;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 表示一个自定义作物方块的接口，扩展基础 {@link ArtisanBlock} 功能。
 * <p>
 * 提供作物特有的生长控制功能，包括骨粉加速生长行为配置。
 * 所有实例应通过 {@link Builder} 构建，禁止外部直接实现此接口。
 * </p>
 *
 * @see ArtisanBlock 基础方块接口
 */
public interface ArtisanCrop extends ArtisanBlock {

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
    static BuilderFactory factory() {
        BuilderFactory builderFactory = Bukkit.getServicesManager().load(BuilderFactory.class);
        if (builderFactory == null) throw new IllegalStateException("factory has not yet registered.");
        return builderFactory;
    }

    interface BuilderFactory {
        @NotNull
        Builder builder();
    }

    @Override
    @NotNull
    ArtisanCropState getState(int n);

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

    interface Builder extends BaseBuilder {

        @NotNull Builder blockId(@NotNull NamespacedKey blockId);

        @NotNull Builder stages(@NotNull List<ArtisanCropState> stages);

        @NotNull Builder placeSound(@NotNull SoundProperty placeSoundProperty);

        @NotNull Builder breakSound(@NotNull SoundProperty breakSoundProperty);

        @NotNull Builder boneMealMinGrowth(int boneMealMinGrowth);

        @NotNull Builder boneMealMaxGrowth(int boneMealMaxGrowth);

        @NotNull ArtisanBlock build();
    }
}
