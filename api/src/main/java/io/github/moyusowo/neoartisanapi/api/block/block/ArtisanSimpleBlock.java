package io.github.moyusowo.neoartisanapi.api.block.block;

import io.github.moyusowo.neoartisanapi.api.block.util.SoundProperty;
import io.github.moyusowo.neoartisanapi.api.block.gui.GUICreator;
import io.github.moyusowo.neoartisanapi.api.block.blockstate.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.util.BuilderFactoryUtil;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

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
        ArtisanSimpleBlock build();
    }
}
