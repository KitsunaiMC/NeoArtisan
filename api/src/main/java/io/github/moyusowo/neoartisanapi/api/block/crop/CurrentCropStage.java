package io.github.moyusowo.neoartisanapi.api.block.crop;

import io.github.moyusowo.neoartisanapi.api.block.ArtisanBlockState;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
public interface CurrentCropStage extends ArtisanBlockState {

    NamespacedKey cropId();

    int stage();

    int getBlockState();

    ItemStack[] getDrops();

    ItemGenerator[] getGenerators();

    boolean hasNextStage();

    CurrentCropStage getNextStage();

    CurrentCropStage getNextFertilizeStage();

    int getMaxStage();

}
