package io.github.moyusowo.neoartisan.block.task;

import io.github.moyusowo.neoartisanapi.api.block.task.LifecycleTaskManager;
import io.github.moyusowo.neoartisanapi.api.util.BuilderFactoryUtil;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface LifecycleTaskManagerInternal extends LifecycleTaskManager {
    @NotNull
    static LifecycleTaskManager create(@NotNull Location location) {
        return BuilderFactoryUtil.getBuilder(BuilderFactory.class).create(location);
    }

    interface BuilderFactory {
        @NotNull
        LifecycleTaskManager create(@NotNull Location location);
    }

    void runTerminate(@NotNull Location location);

    void runInit(@NotNull Location location);
}
