package io.github.moyusowo.neoartisanapi.api.block.thin;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;

public interface ArtisanThinBlockData extends ArtisanBlockData {

    @Override
    ArtisanThinBlock getArtisanBlock();

    @Override
    ArtisanThinBlockState getArtisanBlockState();

    static Builder builder() {
        return Bukkit.getServicesManager().load(Builder.class);
    }

    interface Builder extends BaseBuilder {

        Builder location(Location location);

        Builder blockId(NamespacedKey blockId);

        Builder stage(int stage);

        ArtisanThinBlockData build();
    }
}
