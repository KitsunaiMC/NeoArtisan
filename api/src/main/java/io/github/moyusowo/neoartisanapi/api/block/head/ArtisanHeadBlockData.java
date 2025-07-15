package io.github.moyusowo.neoartisanapi.api.block.head;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * 自定义头颅方块在世界中的具体数据实例。
 * <p>
 * 包含位置、状态等运行时信息，通过 {@link ArtisanHeadBlockData.Builder} 构建不可变实例。
 * </p>
 *
 * @see ArtisanBlockData 基础方块数据接口
 */
public interface ArtisanHeadBlockData extends ArtisanBlockData {
    /**
     * 获取工厂服务实例
     * <p>
     * 此工厂用于创建全新的 {@link Builder} 实例，确保每次构建过程独立且线程安全。
     * </p>
     *
     * @return 作建造器工厂实例（非null）
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
    ArtisanHeadBlock getArtisanBlock();

    @Override
    @NotNull
    ArtisanHeadBlockState getArtisanBlockState();

    interface Builder extends BaseBuilder {

        Builder location(Location location);

        Builder blockId(NamespacedKey blockId);

        Builder stage(int stage);

        ArtisanHeadBlockData build();
    }
}
