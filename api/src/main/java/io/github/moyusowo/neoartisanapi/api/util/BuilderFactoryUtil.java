package io.github.moyusowo.neoartisanapi.api.util;

import org.bukkit.Bukkit;

public final class BuilderFactoryUtil {
    private BuilderFactoryUtil() {}

    public static <T> T getBuilder(Class<T> type) {
        T builder = Bukkit.getServicesManager().load(type);
        if (builder == null) {
            throw new IllegalStateException("Builder service not registered for: " + type.getName());
        }
        return builder;
    }
}
