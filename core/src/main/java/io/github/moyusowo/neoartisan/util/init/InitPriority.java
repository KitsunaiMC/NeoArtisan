package io.github.moyusowo.neoartisan.util.init;

@SuppressWarnings("unused")
public enum InitPriority {
    REGISTRY_LOAD(1),
    REGISTRAR(2),
    REGISTRY_OPEN(3),
    INTERNAL_REGISTER(4),
    REGISTER(5),
    BLOCKDATA(7),
    STORAGE_INIT(8),
    HIGHEST(10),
    HIGH(11),
    DEFAULT(12),
    LOW(13),
    LOWEST(14),
    LISTENER(15),
    COMMANDS(16),
    STARTUP(100);

    private final int priority;

    InitPriority(int priority) {
        this.priority = priority;
    }

    public int priority() {
        return this.priority;
    }
}
