package io.github.moyusowo.neoartisan.util.init;

public enum InitPriority {
    REGISTRY(1),
    REGISTRAR(2),
    REGISTER(3),
    BLOCKDATA(4),
    HIGHEST(5),
    HIGH(6),
    DEFAULT(7),
    LOW(8),
    LOWEST(9),
    LISTENER(10),
    COMMANDS(11);

    private final int priority;

    InitPriority(int priority) {
        this.priority = priority;
    }

    public int priority() {
        return this.priority;
    }
}
