package io.github.moyusowo.neoartisanapi.api.block.transparent;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;

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

    @Override
    ArtisanTransparentBlock getArtisanBlock();

    @Override
    ArtisanTransparentBlockState getArtisanBlockState();

    static Builder builder() {
        return Bukkit.getServicesManager().load(Builder.class);
    }

    interface Builder extends BaseBuilder {

        Builder location(Location location);

        Builder blockId(NamespacedKey blockId);

        Builder stage(int stage);

        ArtisanTransparentBlockData build();
    }
}
