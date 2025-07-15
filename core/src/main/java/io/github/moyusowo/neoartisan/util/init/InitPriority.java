package io.github.moyusowo.neoartisan.util.init;

@SuppressWarnings("unused")
public enum InitPriority {
    REGISTRY_LOAD(1),
    REGISTRAR(2),
    REGISTRY_OPEN(3),
    INTERNAL_REGISTER(4),
    REGISTER(5),
    REGISTRY_CLOSED(6),
    BLOCKDATA(7),
    HIGHEST(8),
    HIGH(9),
    DEFAULT(10),
    LOW(11),
    LOWEST(12),
    LISTENER(13),
    COMMANDS(14);

    private final int priority;

    InitPriority(int priority) {
        this.priority = priority;
    }

    public int priority() {
        return this.priority;
    }
}
