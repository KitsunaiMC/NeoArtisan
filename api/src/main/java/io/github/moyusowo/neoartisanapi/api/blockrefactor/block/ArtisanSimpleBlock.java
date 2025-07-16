package io.github.moyusowo.neoartisanapi.api.blockrefactor.block;

import io.github.moyusowo.neoartisanapi.api.block.base.sound.SoundProperty;
import io.github.moyusowo.neoartisanapi.api.block.gui.GUICreator;
import io.github.moyusowo.neoartisanapi.api.blockrefactor.blockstate.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.util.BuilderFactoryUtil;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public interface ArtisanSimpleBlock extends ArtisanBaseBlock {
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
    static Builder builder() {
        return BuilderFactoryUtil.getBuilder(BuilderFactory.class).builder();
    }

    interface BuilderFactory {
        @NotNull
        Builder builder();
    }

    interface Builder {
        @NotNull
        Builder blockId(@NotNull NamespacedKey blockId);

        @NotNull
        Builder stage(@NotNull ArtisanBaseBlockState stage);

        @NotNull
        Builder placeSound(@NotNull SoundProperty placeSoundProperty);

        @NotNull
        Builder guiCreator(@NotNull GUICreator guiCreator);

        @NotNull
        ArtisanSimpleBlock build();
    }
}
