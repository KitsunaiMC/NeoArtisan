package io.github.moyusowo.neoartisanapi.api.block.block;

import io.github.moyusowo.neoartisanapi.api.block.block.base.ArtisanBaseBlock;
import io.github.moyusowo.neoartisanapi.api.block.block.base.ArtisanBlocks;
import io.github.moyusowo.neoartisanapi.api.block.util.SoundProperty;
import io.github.moyusowo.neoartisanapi.api.block.gui.GUICreator;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.util.BuilderFactoryUtil;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * 表示一个简单方块的接口。
 *
 * <p>
 * 该简单方块类型只有一个方块状态，适合装饰方块或GUI型方块。
 * </p>
 *
 * @see ArtisanBaseBlock 基础方块接口
 */
public interface ArtisanSimpleBlock extends ArtisanBaseBlock {
    @NotNull
    static Builder builder() {
        return BuilderFactoryUtil.getBuilder(BuilderFactory.class).builder();
    }

    @Override
    @NotNull
    default ArtisanBlocks getType() {
        return ArtisanBlocks.SIMPLE;
    }

    interface BuilderFactory {
        @NotNull
        Builder builder();
    }

    interface Builder {
        @NotNull
        Builder blockId(@NotNull NamespacedKey blockId);

        @NotNull
        Builder state(@NotNull ArtisanBaseBlockState stage);

        @NotNull
        Builder placeSound(@NotNull SoundProperty placeSoundProperty);

        @NotNull
        Builder guiCreator(@NotNull GUICreator guiCreator);

        @NotNull
        Builder blockEntity();

        @NotNull
        ArtisanSimpleBlock build();
    }
}
