package io.github.moyusowo.neoartisanapi.api.block.base;

import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public abstract class ArtisanBlockStateBase implements ArtisanBlockState {

    private final int appearanceState, actualState;
    private final ItemGenerator[] generators;

    protected ArtisanBlockStateBase(int appearanceState, int actualState, ItemGenerator[] generators) {
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
    public ItemStack[] drops() {
        ItemStack[] drops = new ItemStack[generators.length];
        for (int i = 0; i < drops.length; i++) {
            drops[i] = generators[i].generate();
        }
        return drops;
    }

    @Override
    public ItemGenerator[] generators() {
        return Arrays.copyOf(generators, generators.length);
    }

}
