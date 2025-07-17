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
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private final List<Runnable> terminateTasks, initTasks;
    private boolean isInit = false;

    public LifecycleTaskManagerImpl(@NotNull Location location) {
        this.lifecycleTasks = new ArrayList<>();
        this.terminateTasks = new ArrayList<>();
        this.initTasks = new ArrayList<>();
    }

    @Override
    public void addLifecycleTask(@NotNull BukkitRunnable bukkitRunnable, long delay, long period, boolean isAsynchronous) {
        if (isInit) throw new IllegalStateException("The LifecycleTaskManager has been init!");
        lifecycleTasks.add(new LifecycleTask(NeoArtisan.instance(), bukkitRunnable, delay, period, isAsynchronous));
    }

    @Override
    public void addTerminateRunnable(@NotNull Runnable runnable) {
        if (isInit) throw new IllegalStateException("The LifecycleTaskManager has been init!");
        terminateTasks.add(runnable);
    }

    @Override
    public void addInitRunnable(@NotNull Runnable runnable) {
        if (isInit) throw new IllegalStateException("The LifecycleTaskManager has been init!");
        initTasks.add(runnable);
    }

    @Override
    public void runInit(@NotNull Location location) {
        for (Runnable runnable : initTasks) {
            try {
                runnable.run();
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
        for (Runnable runnable : terminateTasks) {
            try {
                runnable.run();
            } catch (Exception e) {
                NeoArtisan.logger().warning("error when block lifecycle terminate at location " + location + " of block " + NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlockData(location.getBlock()).blockId().asString() + ", " + e);
            }
        }
    }
}
