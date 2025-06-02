package io.github.moyusowo.neoartisan.util.init;

@SuppressWarnings("unused")
public enum InitPriority {
    REGISTRY_LOAD(1),
    REGISTRAR(2),
    REGISTRY_OPEN(3),
    REGISTER(4),
    REGISTRY_CLOSED(5),
    BLOCKDATA(6),
    HIGHEST(7),
    HIGH(8),
    DEFAULT(9),
    LOW(10),
    LOWEST(11),
    LISTENER(12),
    COMMANDS(13);

    private final int priority;

    InitPriority(int priority) {
        this.priority = priority;
    }

    public int priority() {
        return this.priority;
    }
}
