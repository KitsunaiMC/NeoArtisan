package io.github.moyusowo.neoartisan.block.task;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public final class LifecycleTask {
    private final BukkitRunnable bukkitRunnable;
    private final boolean isAsynchronous;
    private final long delay, period;
    private final Plugin plugin;
    private BukkitTask bukkitTask;

    public LifecycleTask(@NotNull Plugin plugin, @NotNull BukkitRunnable bukkitRunnable, long delay, long period, boolean isAsynchronous) {
        this.plugin = plugin;
        this.bukkitRunnable = bukkitRunnable;
        this.delay = delay;
        this.period = period;
        this.isAsynchronous = isAsynchronous;
        this.bukkitTask = null;
    }

    public LifecycleTask(@NotNull Plugin plugin, @NotNull BukkitRunnable bukkitRunnable, long delay, long period) {
        this(plugin, bukkitRunnable, delay, period, false);
    }

    public void run() {
        if (bukkitTask != null) return;
        if (isAsynchronous) {
            bukkitTask = bukkitRunnable.runTaskTimerAsynchronously(plugin, delay, period);
        } else {
            bukkitTask = bukkitRunnable.runTaskTimer(plugin, delay, period);
        }
    }

    public void cancel() {
        if (bukkitTask == null || bukkitTask.isCancelled()) return;
        bukkitTask.cancel();
    }

}
