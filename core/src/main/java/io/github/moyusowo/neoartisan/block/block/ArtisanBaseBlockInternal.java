package io.github.moyusowo.neoartisan.block.block;

import io.github.moyusowo.neoartisanapi.api.block.block.ArtisanBaseBlock;
import io.github.moyusowo.neoartisanapi.api.block.gui.ArtisanBlockGUI;
import io.github.moyusowo.neoartisanapi.api.block.task.LifecycleTaskManager;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ArtisanBaseBlockInternal extends ArtisanBaseBlock {
    @Nullable
    ArtisanBlockGUI createGUI(@NotNull Location location);

    @NotNull
    LifecycleTaskManager createLifecycleTaskManager(@NotNull Location location);
}
