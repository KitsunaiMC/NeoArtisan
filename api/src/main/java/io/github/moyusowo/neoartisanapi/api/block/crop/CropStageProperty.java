package io.github.moyusowo.neoartisanapi.api.block.crop;

import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@SuppressWarnings("unused")
public record CropStageProperty(int appearanceState, ItemGenerator[] generators) {

    public CropStageProperty(int appearanceState, ItemGenerator[] generators) {
        this.appearanceState = appearanceState;
        this.generators = Arrays.copyOf(generators, generators.length);
    }

    public ItemStack[] drops() {
        ItemStack[] drops = new ItemStack[generators.length];
        for (int i = 0; i < drops.length; i++) {
            drops[i] = generators[i].generate();
        }
        return drops;
    }

    public ItemGenerator[] generators() {
        return Arrays.copyOf(generators, generators.length);
    }
}
