package io.github.moyusowo.neoartisan.block.data;

import io.github.moyusowo.neoartisanapi.api.block.block.base.ArtisanBaseBlock;
import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBaseBlockState;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public record ArtisanBlockDataView(
        @NotNull ArtisanBaseBlock block,
        @NotNull ArtisanBaseBlockState state,
        @NotNull Location location,
        @NotNull NamespacedKey blockId,
        int stage
) {
    public static @NotNull ArtisanBlockDataView from(@NotNull ArtisanBlockData data) {
        return new ArtisanBlockDataView(data.getArtisanBlock(), data.getArtisanBlockState(), data.getLocation(), data.blockId(), data.stage());
    }
}
