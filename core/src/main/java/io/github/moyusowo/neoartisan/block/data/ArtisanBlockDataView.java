package io.github.moyusowo.neoartisan.block.data;

import io.papermc.paper.persistence.PersistentDataContainerView;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

public record ArtisanBlockDataView(
        @NotNull Location location,
        @NotNull NamespacedKey blockId,
        int stage,
        @NotNull PersistentDataContainerView persistentDataContainerView
) {}
