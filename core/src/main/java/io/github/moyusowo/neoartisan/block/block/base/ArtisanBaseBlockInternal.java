package io.github.moyusowo.neoartisan.block.block.base;

import io.github.moyusowo.neoartisan.block.task.LifecycleTaskManagerInternal;
import io.github.moyusowo.neoartisanapi.api.block.block.base.ArtisanBaseBlock;
import io.github.moyusowo.neoartisanapi.api.block.gui.ArtisanBlockGUI;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ArtisanBaseBlockInternal extends ArtisanBaseBlock {
    @Nullable
    ArtisanBlockGUI createGUI(@NotNull Location location);

    @NotNull
    LifecycleTaskManagerInternal createLifecycleTaskManager(@NotNull Location location);
}
