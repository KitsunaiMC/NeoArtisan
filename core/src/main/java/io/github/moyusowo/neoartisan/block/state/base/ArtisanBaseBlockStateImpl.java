package io.github.moyusowo.neoartisan.block.state.base;

import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public abstract class ArtisanBaseBlockStateImpl implements ArtisanBaseBlockState {
    private final int appearanceState, actualState;
    private final ItemGenerator[] generators;

    protected ArtisanBaseBlockStateImpl(int appearanceState, int actualState, ItemGenerator[] generators) {
        this.appearanceState = appearanceState;
        this.actualState = actualState;
        this.generators = Arrays.copyOf(generators, generators.length);
    }

    @Override
    public int appearanceState() {
        return this.appearanceState;
    }

    @Override
    public int actualState() {
        return this.actualState;
    }

    @Override
    public @NotNull Collection<ItemStack> drops() {
        Collection<ItemStack> drops = new ArrayList<>();
        for (ItemGenerator itemGenerator : generators) {
            drops.add(itemGenerator.generate());
        }
        return drops;
    }

    @Override
    public @Nullable Integer getBrightness() {
        return null;
    }

    @Override
    public @Nullable Integer getHardness() {
        return null;
    }
}
