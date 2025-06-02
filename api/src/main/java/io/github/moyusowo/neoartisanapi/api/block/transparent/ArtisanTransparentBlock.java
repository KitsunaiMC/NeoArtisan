package io.github.moyusowo.neoartisanapi.api.block.transparent;

import io.github.moyusowo.neoartisanapi.api.block.base.sound.SoundProperty;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlock;
import io.github.moyusowo.neoartisanapi.api.block.gui.GUICreator;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 表示半透明自定义方块的基础接口。
 * <p>
 * 所有实例应通过 {@link Builder} 构建。
 * </p>
 *
 * @see ArtisanBlock 基础方块接口
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public interface ArtisanTransparentBlock extends ArtisanBlock {

    /**
     * 获取透明方块建造器实例
     * @return 通过服务管理器加载的建造器
     * @throws IllegalStateException 如果服务未注册
     */
    static Builder builder() {
        return Bukkit.getServicesManager().load(Builder.class);
    }

    /**
     * 检查该透明方块是否可被火焰点燃
     * @return 如果方块具有可燃性返回true
     */
    boolean canBurn();

    @NotNull ArtisanTransparentBlockState getState(int n);

    /**
     * 透明方块建造器接口
     */
    interface Builder {

        @NotNull Builder blockId(@NotNull NamespacedKey blockId);

        @NotNull Builder states(@NotNull List<ArtisanTransparentBlockState> states);

        @NotNull Builder placeSound(@NotNull SoundProperty placeSoundProperty);

        @NotNull Builder breakSound(@NotNull SoundProperty breakSoundProperty);

        @NotNull Builder canBurn(boolean canBurn);

        @NotNull Builder guiCreator(@NotNull GUICreator creator);

        ArtisanTransparentBlock build();
    }
}
