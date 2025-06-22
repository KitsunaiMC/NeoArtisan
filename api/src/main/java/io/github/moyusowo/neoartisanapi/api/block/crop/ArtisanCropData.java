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
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public interface ArtisanCropData extends ArtisanBlockData {
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
     * 获取关联的自定义方块定义
     *
     * @return 不可变的方块类型实例
     */
    @Override
    @NotNull
    ArtisanCrop getArtisanBlock();

    /**
     * 获取当前方块状态实例
     *
     * @return 通过 {@link ArtisanCrop#getState(int)} 获取的不可变状态
     * @see ArtisanCrop#getState(int)
     */
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
    ArtisanCropData getNextStage();

    /**
     * 获取骨粉催熟后的下一阶段数据
     *
     * @return 根据 {@link ArtisanCrop#generateBoneMealGrowth()}
     *         计算的新阶段数据
     */
    ArtisanCropData getNextFertilizeStage();

    /**
     * 作物数据建造器接口
     */
    interface Builder extends BaseBuilder {

        /**
         * 设置方块位置（必须调用）
         * @param location 作物所在位置（非null）
         */
        Builder location(Location location);

        /**
         * 设置作物类型ID（必须调用）
         * @param blockId 与 {@link ArtisanCrop#getBlockId()} 一致的ID
         */
        Builder blockId(NamespacedKey blockId);

        /**
         * 设置当前生长阶段（必须调用）
         * @param stage 0到最大阶段之间的整数
         */
        Builder stage(int stage);

        /**
         * 构建不可变的作物数据实例
         * @throws IllegalArgumentException 如果必要参数未设置
         */
        ArtisanCropData build();
    }

}
