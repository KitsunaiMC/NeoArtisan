package io.github.moyusowo.neoartisan.block.task;

import io.github.moyusowo.neoartisan.NeoArtisan;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public final class LifecycleTask {
    private final Runnable runnable;
    private final boolean isAsynchronous, runInChunkNotLoaded;
    private final long delay, period;
    private final Location location;
    private BukkitTask bukkitTask;

    public LifecycleTask(@NotNull Runnable runnable, long delay, long period, Location location, boolean isAsynchronous, boolean runInChunkNotLoaded) {
        this.runnable = runnable;
        this.delay = delay;
        this.period = period;
        this.isAsynchronous = isAsynchronous;
        this.runInChunkNotLoaded = runInChunkNotLoaded;
        this.location = location;
    }

    public void run() {
        if (bukkitTask != null) return;
        final Runnable r;
        if (!runInChunkNotLoaded) {
            r = () -> {
                if (location.isChunkLoaded()) {
                    runnable.run();
                }
            };
        } else {
            r = runnable;
        }
        if (isAsynchronous) {
            bukkitTask = Bukkit.getScheduler().runTaskTimerAsynchronously(NeoArtisan.instance(), r, delay, period);
        } else {
            bukkitTask = Bukkit.getScheduler().runTaskTimer(NeoArtisan.instance(), r, delay, period);
        }
    }

    public void cancel() {
        if (bukkitTask == null || bukkitTask.isCancelled()) return;
        bukkitTask.cancel();
    }

}
