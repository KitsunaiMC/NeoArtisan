package io.github.moyusowo.neoartisanapi.api.block.crop;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * 表示世界中实际存在的自定义作物方块数据，扩展基础 {@link ArtisanBlockData} 功能。
 * <p>
 * 提供作物特有的生长状态控制方法，所有实例应通过 {@link Builder} 构建。
 * </p>
 *
 * @see ArtisanBlockData 基础方块数据接口
 */
public interface ArtisanCropData extends ArtisanBlockData {
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
    ArtisanCrop getArtisanBlock();

    @Override
    @NotNull
    ArtisanCropState getArtisanBlockState();

    /**
     * 检查是否存在下一生长阶段
     *
     * @return 如果当前不是最终生长阶段返回true
     */
    boolean hasNextStage();

    /**
     * 获取自然生长后的下一阶段数据
     *
     * @return 新阶段的数据副本（相同位置，stage+1）
     * @throws IllegalArgumentException 如果已是最终阶段
     */
    @NotNull ArtisanCropData getNextStage();

    /**
     * 获取骨粉催熟后的下一阶段数据
     *
     * @return 根据 {@link ArtisanCrop#generateBoneMealGrowth()}
     *         计算的新阶段数据
     */
    @NotNull ArtisanCropData getNextFertilizeStage();

    interface Builder extends BaseBuilder {

        @NotNull Builder location(@NotNull Location location);

        @NotNull Builder blockId(@NotNull NamespacedKey blockId);

        @NotNull Builder stage(int stage);

        @NotNull ArtisanCropData build();
    }

}
