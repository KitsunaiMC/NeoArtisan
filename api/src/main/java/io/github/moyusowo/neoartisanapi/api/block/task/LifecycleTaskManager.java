package io.github.moyusowo.neoartisanapi.api.block.task;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Manages lifecycle tasks for custom blocks.
 * <p>
 * Provides methods to register periodic and one-time tasks that run during different phases
 * of a block's lifecycle, such as initialization, ongoing operations, and termination.
 * </p>
 */
@ApiStatus.NonExtendable
public interface LifecycleTaskManager {
    /**
     * Adds a block lifecycle task
     * <p>
     * Used to register periodic tasks that need to run after the block is placed (such as GUI refresh).
     * </p>
     *
     * <p>
     * Tasks are automatically cancelled when the block is destroyed.
     * </p>
     *
     * @param runnable the task to add (non-null)
     * @param delay the delay before the task starts (in ticks)
     * @param period the period between task executions (in ticks)
     * @param isAsynchronous whether to execute asynchronously
     * @param runInChunkNotLoaded whether to run the task even when the chunk is not loaded (use with caution!)
     * @throws IllegalStateException if initialization is already complete
     */
    void addLifecycleTask(@NotNull Runnable runnable, long delay, long period, boolean isAsynchronous, boolean runInChunkNotLoaded);

    /**
     * Adds a task to run after the block is destroyed
     *
     * <p>
     * Used to register one-time tasks that need to run after the block is destroyed
     * (such as saving data, releasing resources).
     * </p>
     *
     * @param runnable the task to add (non-null)
     */
    void addTerminateRunnable(@NotNull Runnable runnable);

    /**
     * Adds a task to run during block initialization
     *
     * <p>
     * Used to register one-time tasks that need to run during block initialization
     * (such as initializing GUI).
     * </p>
     *
     * @param runnable the task to add (non-null)
     */
    void addInitRunnable(@NotNull Runnable runnable);
}
