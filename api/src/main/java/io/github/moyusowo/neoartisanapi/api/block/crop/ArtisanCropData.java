package io.github.moyusowo.neoartisanapi.api.block.crop;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;

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
     * 获取关联的自定义方块定义
     *
     * @return 不可变的方块类型实例
     */
    @Override
    ArtisanCrop getArtisanBlock();

    /**
     * 获取当前方块状态实例
     *
     * @return 通过 {@link ArtisanCrop#getState(int)} 获取的不可变状态
     * @see ArtisanCrop#getState(int)
     */
    @Override
    ArtisanCropState getArtisanBlockState();

    /**
     * 获取作物数据建造器实例
     *
     * @return 通过服务管理器加载的建造器
     * @throws IllegalStateException 如果建造器服务未注册
     */
    static Builder builder() {
        return Bukkit.getServicesManager().load(Builder.class);
    }

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
