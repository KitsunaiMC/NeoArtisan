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
 */
public interface ArtisanThinBlock extends ArtisanBlock {
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

    @NotNull ArtisanThinBlockState getState(int n);

    interface Builder extends BaseBuilder {

        @NotNull Builder blockId(@NotNull NamespacedKey blockId);

        @NotNull Builder states(@NotNull List<ArtisanThinBlockState> states);

        @NotNull Builder placeSound(@NotNull SoundProperty placeSoundProperty);

        @NotNull Builder breakSound(@NotNull SoundProperty breakSoundProperty);

        @NotNull Builder guiCreator(@NotNull GUICreator creator);

        ArtisanThinBlock build();
    }
}
