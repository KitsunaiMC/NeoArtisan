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
     * @return 作物建造器工厂实例（非null）
     * @throws IllegalStateException 如果工厂服务未注册
     * @see Builder 构建器接口
     * @since 2.0.0
     */
    static BuilderFactory factory() {
        return Bukkit.getServicesManager().load(BuilderFactory.class);
    }

    /**
     * 建造器工厂接口
     * <p>
     * 负责创建全新的 {@link Builder} 实例，确保每次构建过程独立。
     * 实现必须保证每次调用 {@link #builder()} 都返回<strong>未使用过</strong>的构建器。
     * </p>
     *
     * <p><b>实现要求：</b></p>
     * <ol>
     *   <li>禁止返回单例或共享实例</li>
     *   <li>构建器初始状态必须完全重置</li>
     * </ol>
     */
    interface BuilderFactory {
        @NotNull
        Builder builder();
    }

    /**
     * 获取特定生长阶段的作物状态
     *
     * @param n 生长阶段索引 (0 ≤ n ≤ maxStage)
     * @return 对应的不可变作物状态
     * @see ArtisanCropState 作物状态详情
     */
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

    /**
     * 作物建造器接口，采用流式API设计
     *
     * @apiNote 所有setter方法返回建造器自身以实现链式调用
     */
    interface Builder extends BaseBuilder {

        @NotNull Builder blockId(@NotNull NamespacedKey blockId);

        @NotNull Builder stages(@NotNull List<ArtisanCropState> stages);

        @NotNull Builder placeSound(@NotNull SoundProperty placeSoundProperty);

        @NotNull Builder breakSound(@NotNull SoundProperty breakSoundProperty);

        @NotNull Builder boneMealMinGrowth(int boneMealMinGrowth);

        @NotNull Builder boneMealMaxGrowth(int boneMealMaxGrowth);

        /**
         * 构建不可变的作物实例
         *
         * @throws IllegalArgumentException 如果必要参数未设置或设置有误
         */
        ArtisanBlock build();
    }
}
