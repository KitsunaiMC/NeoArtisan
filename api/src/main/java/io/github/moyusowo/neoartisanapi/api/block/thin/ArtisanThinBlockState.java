package io.github.moyusowo.neoartisanapi.api.block.thin;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockState;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.Bukkit;

/**
 * 薄型自定义方块的特定状态定义。
 * <p>
 * 包含外观和掉落物等状态相关属性。
 * </p>
 *
 * @see ArtisanBlockState 基础方块状态接口
 * @since 1.0.0
 */
public interface ArtisanThinBlockState extends ArtisanBlockState {

    static Builder builder() {
        return Bukkit.getServicesManager().load(Builder.class);
    }

    interface Builder {

        Builder appearanceState(ThinBlockAppearance thinBlockAppearance);

        Builder generators(ItemGenerator[] generators);

        ArtisanThinBlockState build();
    }
}
