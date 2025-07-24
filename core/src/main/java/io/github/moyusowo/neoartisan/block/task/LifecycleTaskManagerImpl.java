package io.github.moyusowo.neoartisan.block.task;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

final class LifecycleTaskManagerImpl implements LifecycleTaskManagerInternal {
    @InitMethod(priority = InitPriority.REGISTRAR)
    private static void init() {
        Bukkit.getServicesManager().register(
                BuilderFactory.class,
                LifecycleTaskManagerImpl::new,
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private final List<LifecycleTask> lifecycleTasks;
    private final List<PriorityRunnable> terminateTasks, initTasks;
    private final Location location;
    private boolean isInit = false;

    public LifecycleTaskManagerImpl(@NotNull Location location) {
        this.lifecycleTasks = new ArrayList<>();
        this.terminateTasks = new ArrayList<>();
        this.initTasks = new ArrayList<>();
        this.location = location;
    }

    @Override
    public void addLifecycleTask(@NotNull Runnable runnable, long delay, long period, boolean isAsynchronous, boolean runInChunkNotLoaded) {
        if (isInit) throw new IllegalStateException("The LifecycleTaskManager has been init!");
        lifecycleTasks.add(new LifecycleTask(runnable, delay, period, location, isAsynchronous, runInChunkNotLoaded));
    }

    @Override
    public void addTerminateRunnable(@NotNull Runnable runnable, SingleTaskPriority type) {
        if (isInit) throw new IllegalStateException("The LifecycleTaskManager has been init!");
        terminateTasks.add(new PriorityRunnable(runnable, type));
    }

    @Override
    public void addInitRunnable(@NotNull Runnable runnable, SingleTaskPriority type) {
        if (isInit) throw new IllegalStateException("The LifecycleTaskManager has been init!");
        initTasks.add(new PriorityRunnable(runnable, type));
    }

    @Override
    public void runInit(@NotNull Location location) {
        initTasks.sort(Comparator.comparingInt(p -> p.type.priority));
        for (PriorityRunnable priorityRunnable : initTasks) {
            try {
                priorityRunnable.runnable.run();
            } catch (Exception e) {
                NeoArtisan.logger().warning("error when block lifecycle init at location " + location + " of block " + NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlockData(location.getBlock()).blockId().asString() + ", " + e);
            }
        }
        isInit = true;
        for (LifecycleTask lifecycleTask : lifecycleTasks) {
            lifecycleTask.run();
        }
    }

    @Override
    public void runTerminate(@NotNull Location location) {
        for (LifecycleTask lifecycleTask : lifecycleTasks) {
            lifecycleTask.cancel();
        }
        terminateTasks.sort((p1, p2) -> Integer.compare(p2.type.priority, p1.type.priority));
        for (PriorityRunnable priorityRunnable : terminateTasks) {
            try {
                priorityRunnable.runnable.run();
            } catch (Exception e) {
                NeoArtisan.logger().warning("error when block lifecycle terminate at location " + location + " of block " + NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlockData(location.getBlock()).blockId().asString() + ", " + e);
            }
        }
    }

    private record PriorityRunnable(@NotNull Runnable runnable, @NotNull SingleTaskPriority type) {}
}
