package io.github.moyusowo.neoartisan.util.init;

import io.github.moyusowo.neoartisan.NeoArtisan;
import org.bukkit.Bukkit;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.util.*;

public final class Initializer {
    private static final List<Method> ENABLE_METHODS = Collections.synchronizedList(new ArrayList<>());

    public static void scanPackage(String pkg) {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .forPackage(pkg)
                        .setScanners(Scanners.MethodsAnnotated)
        );
        Set<Method> methods = reflections.getMethodsAnnotatedWith(InitMethod.class);
        for (Method method : methods) {
            method.setAccessible(true);
            ENABLE_METHODS.add(method);
        }
    }

    public static void executeEnable() {
        ENABLE_METHODS.sort(Comparator.comparingInt(m -> m.getAnnotation(InitMethod.class).priority().priority()));
        ENABLE_METHODS.forEach(method -> {
            try {
                method.invoke(null);
                if (NeoArtisan.isDebugMode()) {
                    NeoArtisan.logger().info("successfully initialize method: " + method.getDeclaringClass().getName() + "." + method.getName());
                }
            } catch (Exception e) {
                NeoArtisan.logger().severe("fail to initialize method: " + method + ", " + e);
                NeoArtisan.logger().severe("fail to enable plugin. plugin disabling...");
                Bukkit.getPluginManager().disablePlugin(NeoArtisan.instance());
            }
        });
        NeoArtisan.logger().info("successfully initialize and plugin is enabled.");
    }
}

