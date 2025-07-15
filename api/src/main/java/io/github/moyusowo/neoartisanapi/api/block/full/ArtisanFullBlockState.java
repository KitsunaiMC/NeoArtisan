package io.github.moyusowo.neoartisanapi.api.block.full;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockState;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
 * 完整自定义方块的特定状态定义。
 * <p>
 * 包含外观和掉落物等状态相关属性。
 * </p>
 *
 * @see ArtisanBlockState 基础方块状态接口
 */
public interface ArtisanFullBlockState extends ArtisanBlockState {
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

        @NotNull Builder appearanceState(@NotNull FullBlockAppearance fullBlockAppearance);

        @NotNull Builder generators(@NotNull ItemGenerator[] generators);

        @NotNull ArtisanFullBlockState build();
    }
}
