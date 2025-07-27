package io.github.moyusowo.neoartisan.block.task;

import io.github.moyusowo.neoartisanapi.api.block.task.LifecycleTaskManager;
import io.github.moyusowo.neoartisanapi.api.util.ServiceUtil;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public interface LifecycleTaskManagerInternal extends LifecycleTaskManager {
    @NotNull
    static LifecycleTaskManagerInternal create(@NotNull Location location) {
        return ServiceUtil.getService(BuilderFactory.class).create(location);
    }

    interface BuilderFactory {
        @NotNull
        LifecycleTaskManagerInternal create(@NotNull Location location);
    }

    default void addInitRunnable(@NotNull Runnable runnable) {
        addInitRunnable(runnable, SingleTaskPriority.COMMON);
    }

    default void addTerminateRunnable(@NotNull Runnable runnable) {
        addTerminateRunnable(runnable, SingleTaskPriority.COMMON);
    }

    void addInitRunnable(@NotNull Runnable runnable, SingleTaskPriority type);

    void addTerminateRunnable(@NotNull Runnable runnable, SingleTaskPriority type);

    void runTerminate(@NotNull Location location);

    void runInit(@NotNull Location location);


}
