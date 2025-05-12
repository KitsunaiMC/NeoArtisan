package io.github.moyusowo.neoartisanapi.api.block.crop;

import org.bukkit.NamespacedKey;

import java.util.Arrays;

@SuppressWarnings("unused")
public record CropStageProperty(int appearanceState, NamespacedKey[] drops) {

    public CropStageProperty(int appearanceState, NamespacedKey[] drops) {
        this.appearanceState = appearanceState;
        this.drops = Arrays.copyOf(drops, drops.length);
    }

    @Override
    public NamespacedKey[] drops() {
        return Arrays.copyOf(drops, drops.length);
    }
}
