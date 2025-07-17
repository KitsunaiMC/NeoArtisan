package io.github.moyusowo.neoartisanapi.api.block.task;

import org.bukkit.NamespacedKey;

public final class LifecycleTaskType {
    public static final String namespace = "neoartisan_block_task";
    public static final NamespacedKey GUI = new NamespacedKey(namespace, "gui");
    public static final NamespacedKey RANDOM_TICK = new NamespacedKey(namespace, "random_tick");
}
