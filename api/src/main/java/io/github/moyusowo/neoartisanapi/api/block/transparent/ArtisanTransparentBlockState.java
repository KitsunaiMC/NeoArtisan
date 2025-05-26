package io.github.moyusowo.neoartisanapi.api.block.transparent;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockState;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.Bukkit;

/**
 * 透明自定义方块的特定状态定义。
 * <p>
 * 包含外观和掉落物等状态相关属性。
 * </p>
 *
 * @see ArtisanBlockState 基础方块状态接口
 * @since 1.0.0
 */
public interface ArtisanTransparentBlockState extends ArtisanBlockState {

    static Builder builder() {
        return Bukkit.getServicesManager().load(Builder.class);
    }

    interface Builder {

        Builder appearanceState(TransparentAppearance transparentAppearance);

        Builder generators(ItemGenerator[] generators);

        ArtisanTransparentBlockState build();
    }
}
