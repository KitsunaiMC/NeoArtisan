package io.github.moyusowo.neoartisan.block.block;

import io.github.moyusowo.neoartisanapi.api.block.block.ArtisanBaseBlock;
import io.github.moyusowo.neoartisanapi.api.block.blockstate.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.block.gui.ArtisanBlockGUI;
import io.github.moyusowo.neoartisanapi.api.block.gui.GUICreator;
import io.github.moyusowo.neoartisanapi.api.block.util.SoundProperty;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class ArtisanBaseBlockImpl implements ArtisanBaseBlock {
    private final NamespacedKey blockId;
    private final List<ArtisanBaseBlockState> stages;
    private final GUICreator creator;
    private final SoundProperty placeSound;

    protected ArtisanBaseBlockImpl(@NotNull NamespacedKey blockId, @NotNull List<ArtisanBaseBlockState> stages, @Nullable GUICreator creator, SoundProperty placeSound) {
        this.blockId = blockId;
        this.creator = creator;
        this.placeSound = placeSound;
        this.stages = new ArrayList<>(stages);
    }

    @Override
    public @Nullable ArtisanBlockGUI createGUI(Location location) {
        if (this.creator == null) return null;
        return this.creator.create(location);
    }

    @Override
    public @NotNull NamespacedKey getBlockId() {
        return this.blockId;
    }

    @Override
    public @NotNull ArtisanBaseBlockState getState(int n) {
        if (n > getTotalStates()) return this.stages.getLast();
        if (n < 0) return this.stages.getFirst();
        else return this.stages.get(n);
    }

    @Override
    public int getTotalStates() {
        return this.stages.size();
    }

    @Override
    public @Nullable SoundProperty getPlaceSoundProperty() {
        return this.placeSound;
    }
}
