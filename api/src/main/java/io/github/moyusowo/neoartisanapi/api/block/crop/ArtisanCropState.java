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
 */
public interface ArtisanCropState extends ArtisanBlockState {
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

    interface Builder extends BaseBuilder {

        @NotNull Builder appearance(@NotNull CropAppearance cropAppearance);

        @NotNull Builder generators(@NotNull ItemGenerator[] generators);

        @NotNull ArtisanCropState build();
    }
}
