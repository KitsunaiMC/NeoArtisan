package io.github.moyusowo.neoartisan.block.task;

public enum SingleTaskPriority {
    BLOCK_ENTITY(0),
    GUI(1),
    COMMON(25564);

    public final int priority;

    SingleTaskPriority(int priority) {
        this.priority = priority;
    }
}
