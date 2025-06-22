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
     * 作物状态建造器接口
     */
    interface Builder extends BaseBuilder {

        /**
         * 设置作物外观配置
         *
         * @param cropAppearance 具体的外观实现（非null）
         */
        @NotNull Builder appearance(@NotNull CropAppearance cropAppearance);

        /**
         * 设置掉落物生成器数组
         *
         * @param generators 生成器数组（可为空但非null）
         */
        @NotNull Builder generators(@NotNull ItemGenerator[] generators);

        /**
         * 构建不可变的作物状态实例
         *
         * @throws IllegalArgumentException 如果必要参数未设置
         */
        @NotNull ArtisanCropState build();
    }
}
