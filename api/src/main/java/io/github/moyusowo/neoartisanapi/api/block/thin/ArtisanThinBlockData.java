package io.github.moyusowo.neoartisanapi.api.block.thin;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;

/**
 * 薄型自定义方块在世界中的具体数据实例。
 * <p>
 * 包含位置、状态等运行时信息，通过 {@link Builder} 构建不可变实例。
 * </p>
 *
 * @see ArtisanBlockData 基础方块数据接口
 * @since 1.0.0
 */
public interface ArtisanThinBlockData extends ArtisanBlockData {

    @Override
    ArtisanThinBlock getArtisanBlock();

    @Override
    ArtisanThinBlockState getArtisanBlockState();

    /**
     * 获取薄型方块数据建造器
     * @return 通过服务管理器加载的建造器
     * @throws IllegalStateException 如果服务未注册
     */
    static Builder builder() {
        return Bukkit.getServicesManager().load(Builder.class);
    }

    interface Builder extends BaseBuilder {

        Builder location(Location location);

        Builder blockId(NamespacedKey blockId);

        Builder stage(int stage);

        ArtisanThinBlockData build();
    }
}
