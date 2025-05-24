package io.github.moyusowo.neoartisanapi.api.block.base;

import io.github.moyusowo.neoartisanapi.api.block.gui.ArtisanBlockGUI;
import io.github.moyusowo.neoartisanapi.api.block.gui.GUICreator;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.*;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class ArtisanBlockBase implements ArtisanBlock {

    private final NamespacedKey blockId;
    private final List<ArtisanBlockState> stages;
    private final GUICreator creator;

    protected ArtisanBlockBase(NamespacedKey blockId, List<? extends ArtisanBlockState> stages, GUICreator creator) {
        this.blockId = blockId;
        this.creator = creator;
        this.stages = new ArrayList<>();
        this.stages.addAll(stages);
    }

    @Override
    @Nullable
    public ArtisanBlockGUI createGUI(Location location) {
        if (this.creator == null) return null;
        return this.creator.create(location);
    }

    @Override
    @NotNull
    public NamespacedKey getBlockId() {
        return this.blockId;
    }

    @Override
    @NotNull
    public ArtisanBlockState getState(int n) {
        if (n > getTotalStates()) return this.stages.getLast();
        else return this.stages.get(n);
    }

    @Override
    public int getTotalStates() {
        return this.stages.size() - 1;
    }

}
