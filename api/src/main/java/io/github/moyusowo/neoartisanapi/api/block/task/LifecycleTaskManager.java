package io.github.moyusowo.neoartisanapi.api.block.task;

import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public interface LifecycleTaskManager {
    /**
     * 添加方块生命周期任务
     * <p>
     * 用于注册需要在方块放置后运行的周期性任务（如GUI刷新等）。
     * </p>
     *
     * <p>
     * 任务在方块被破坏后会自动取消。
     * </p>
     *
     * @param runnable 要添加的任务（非null）
     * @param delay 任务开始时的延迟（Tick为单位）
     * @param period 任务执行的周期（Tick为单位）
     * @param isAsynchronous 是否异步执行
     * @param runInChunkNotLoaded 在区块未加载的情况下仍然执行任务（谨慎使用！）
     * @throws IllegalStateException 如果初始化已完成
     */
    void addLifecycleTask(@NotNull Runnable runnable, long delay, long period, boolean isAsynchronous, boolean runInChunkNotLoaded);

    /**
     * 添加方块被破坏后的任务
     *
     * <p>
     * 用于注册需要在方块被破坏后运行的一次性任务（如保存、释放资源等）。
     * </p>
     *
     * @param runnable 要添加的任务（非null）
     */
    void addTerminateRunnable(@NotNull Runnable runnable);

    /**
     * 添加方块初始化时的任务
     *
     * <p>
     * 用于注册需要在方块初始化时运行的一次性任务（如初始化GUI等）。
     * </p>
     *
     * @param runnable 要添加的任务（非null）
     */
    void addInitRunnable(@NotNull Runnable runnable);
}
