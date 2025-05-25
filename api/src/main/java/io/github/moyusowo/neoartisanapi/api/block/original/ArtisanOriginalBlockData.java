package io.github.moyusowo.neoartisanapi.api.block.original;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;

public interface ArtisanOriginalBlockData extends ArtisanBlockData {

    @Override
    ArtisanOriginalBlock getArtisanBlock();

    @Override
    ArtisanOriginalBlockState getArtisanBlockState();

    static Builder builder() {
        return Bukkit.getServicesManager().load(Builder.class);
    }

    interface Builder extends BaseBuilder {

        Builder location(Location location);

        Builder blockId(NamespacedKey blockId);

        Builder stage(int stage);

        ArtisanOriginalBlockData build();
    }
}
