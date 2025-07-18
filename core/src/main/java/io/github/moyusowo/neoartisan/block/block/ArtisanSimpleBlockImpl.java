package io.github.moyusowo.neoartisan.block.block;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.block.base.ArtisanBaseBlockImpl;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.block.ArtisanSimpleBlock;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.block.gui.GUICreator;
import io.github.moyusowo.neoartisanapi.api.block.util.SoundProperty;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

final class ArtisanSimpleBlockImpl extends ArtisanBaseBlockImpl implements ArtisanSimpleBlock {
    @InitMethod(priority = InitPriority.REGISTRAR)
    private static void init() {
        Bukkit.getServicesManager().register(
                BuilderFactory.class,
                BuilderImpl::new,
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private final ArtisanBaseBlockState state;
    private final boolean hasBlockEntity;

    private ArtisanSimpleBlockImpl(@NotNull NamespacedKey blockId, @NotNull ArtisanBaseBlockState state, @Nullable GUICreator creator, SoundProperty placeSound, boolean hasBlockEntity) {
        super(blockId, List.of(), creator, placeSound);
        this.state = state;
        this.hasBlockEntity = hasBlockEntity;
    }

    @Override
    public @NotNull ArtisanBaseBlockState getState(int n) {
        return state;
    }

    @Override
    public boolean hasBlockEntity() {
        return hasBlockEntity;
    }

    public static final class BuilderImpl implements Builder {
        private NamespacedKey blockId;
        private ArtisanBaseBlockState state;
        private SoundProperty placeSound;
        private GUICreator guiCreator;
        private boolean hasBlockEntity;

        public BuilderImpl() {
            blockId = null;
            state = null;
            placeSound = null;
            guiCreator = null;
            hasBlockEntity = false;
        }

        @Override
        public @NotNull Builder blockId(@NotNull NamespacedKey blockId) {
            this.blockId = blockId;
            return this;
        }

        @Override
        public @NotNull Builder state(@NotNull ArtisanBaseBlockState stage) {
            this.state = stage;
            return this;
        }

        @Override
        public @NotNull Builder placeSound(@NotNull SoundProperty placeSoundProperty) {
            this.placeSound = placeSoundProperty;
            return this;
        }

        @Override
        public @NotNull Builder guiCreator(@NotNull GUICreator guiCreator) {
            this.guiCreator = guiCreator;
            return this;
        }

        @Override
        public @NotNull Builder blockEntity() {
            this.hasBlockEntity = true;
            return this;
        }

        @Override
        public @NotNull ArtisanSimpleBlock build() {
            if (blockId == null || state == null) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanSimpleBlockImpl(blockId, state, guiCreator, placeSound, hasBlockEntity);
        }
    }
}
