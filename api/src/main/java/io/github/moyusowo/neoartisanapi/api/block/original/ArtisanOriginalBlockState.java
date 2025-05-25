package io.github.moyusowo.neoartisanapi.api.block.original;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockState;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.BlockType;

public interface ArtisanOriginalBlockState extends ArtisanBlockState {

    static Builder builder() {
        return Bukkit.getServicesManager().load(Builder.class);
    }

    interface Builder {

        Builder blockType(Material material);

        Builder generators(ItemGenerator[] generators);

        ArtisanOriginalBlockState build();
    }
}
