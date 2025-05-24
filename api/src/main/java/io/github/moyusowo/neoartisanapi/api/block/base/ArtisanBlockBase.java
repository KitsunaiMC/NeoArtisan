package io.github.moyusowo.neoartisanapi.api.block.base;

import org.bukkit.NamespacedKey;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.*;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class ArtisanBlockBase implements ArtisanBlock {

    private final NamespacedKey blockId;
    private final List<ArtisanBlockState> stages;

    protected ArtisanBlockBase(NamespacedKey blockId, List<? extends ArtisanBlockState> stages) {
        this.blockId = blockId;
        this.stages = new ArrayList<>();
        this.stages.addAll(stages);
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
