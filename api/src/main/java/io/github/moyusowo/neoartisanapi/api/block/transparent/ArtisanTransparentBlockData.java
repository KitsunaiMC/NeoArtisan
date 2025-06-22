package io.github.moyusowo.neoartisanapi.api.block.transparent;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * 透明自定义方块在世界中的具体数据实例。
 * <p>
 * 包含位置、状态等运行时信息，通过 {@link Builder} 构建不可变实例。
 * </p>
 *
 * @see ArtisanBlockData 基础方块数据接口
 * @since 1.0.0
 */
public interface ArtisanTransparentBlockData extends ArtisanBlockData {
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

    @Override
    @NotNull
    ArtisanTransparentBlock getArtisanBlock();

    @Override
    @NotNull
    ArtisanTransparentBlockState getArtisanBlockState();

    interface Builder extends BaseBuilder {

        Builder location(Location location);

        Builder blockId(NamespacedKey blockId);

        Builder stage(int stage);

        ArtisanTransparentBlockData build();
    }
}
