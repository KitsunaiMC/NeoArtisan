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
     * @return 作物建造器工厂实例（非null）
     * @throws IllegalStateException 如果工厂服务未注册
     * @see Builder 构建器接口
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

    interface Builder extends BaseBuilder {
        @NotNull Builder appearanceState(@NotNull FullBlockAppearance fullBlockAppearance);

        @NotNull Builder generators(@NotNull ItemGenerator[] generators);

        @NotNull ArtisanFullBlockState build();
    }
}
