package io.github.moyusowo.neoartisanapi.api.block.base;

import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.inventory.ItemStack;

public interface ArtisanBlockState {

    int appearanceState();

    int actualState();

    ItemStack[] drops();

    ItemGenerator[] generators();

}
