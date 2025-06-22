package io.github.moyusowo.neoartisanapi.api.block.crop;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockState;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
 * 表示自定义作物的特定生长状态，扩展基础 {@link ArtisanBlockState} 功能。
 * <p>
 * 提供作物特有的外观配置能力，所有实例应通过 {@link Builder} 构建。
 * </p>
 *
 * @see ArtisanBlockState 基础方块状态接口
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public interface ArtisanCropState extends ArtisanBlockState {

    /**
     * 获取作物状态建造器实例
     *
     * @return 通过服务管理器加载的建造器
     * @throws IllegalStateException 如果建造器服务未注册
     */
    static Builder builder() {
        return Bukkit.getServicesManager().load(ArtisanCropState.Builder.class);
    }

    /**
     * 作物状态建造器接口
     */
    interface Builder extends BaseBuilder {

        /**
         * 设置作物外观配置
         *
         * @param cropAppearance 具体的外观实现（非null）
         */
        Builder appearance(@NotNull CropAppearance cropAppearance);

        /**
         * 设置掉落物生成器数组
         *
         * @param generators 生成器数组（可为空但非null）
         */
        Builder generators(@NotNull ItemGenerator[] generators);

        /**
         * 构建不可变的作物状态实例
         *
         * @throws IllegalArgumentException 如果必要参数未设置
         */
        ArtisanCropState build();
    }
}
