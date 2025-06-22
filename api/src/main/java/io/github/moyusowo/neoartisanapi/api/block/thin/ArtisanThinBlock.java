package io.github.moyusowo.neoartisanapi.api.block.thin;

import io.github.moyusowo.neoartisanapi.api.block.base.sound.SoundProperty;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlock;
import io.github.moyusowo.neoartisanapi.api.block.gui.GUICreator;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 表示薄型外观自定义方块的接口。
 * <p>
 * 扩展 {@link ArtisanBlock} 并添加薄型方块特有行为，所有实例应通过 {@link Builder} 构建。
 * </p>
 *
 * @see ArtisanBlock 基础方块接口
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public interface ArtisanThinBlock extends ArtisanBlock {

    /**
     * 获取薄型方块建造器实例
     * @return 通过服务管理器加载的建造器
     * @throws IllegalStateException 如果服务未注册
     */
    static Builder builder() {
        return Bukkit.getServicesManager().load(Builder.class);
    }

    /**
     * 获取特定状态下的薄型方块状态
     * @param n 状态索引
     * @return 对应的不可变状态实例
     */
    @NotNull ArtisanThinBlockState getState(int n);

    /**
     * 薄型方块建造器接口
     */
    interface Builder extends BaseBuilder {

        @NotNull Builder blockId(@NotNull NamespacedKey blockId);

        @NotNull Builder states(@NotNull List<ArtisanThinBlockState> states);

        @NotNull Builder placeSound(@NotNull SoundProperty placeSoundProperty);

        @NotNull Builder breakSound(@NotNull SoundProperty breakSoundProperty);

        @NotNull Builder guiCreator(@NotNull GUICreator creator);

        ArtisanThinBlock build();
    }
}
