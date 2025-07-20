package io.github.moyusowo.neoartisan.util.init;

import io.github.moyusowo.neoartisan.NeoArtisan;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public final class Initializer {
    private static final List<Method> ENABLE_METHODS = Collections.synchronizedList(new ArrayList<>());
    private static final List<Method> STARTUP_METHODS = Collections.synchronizedList(new ArrayList<>());

    public static void scanPackage(String pkg) {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .forPackage(pkg)
                        .setScanners(Scanners.MethodsAnnotated)
        );
        Set<Method> methods = reflections.getMethodsAnnotatedWith(InitMethod.class);
        for (Method method : methods) {
            if (method.getAnnotation(InitMethod.class).priority() != InitPriority.STARTUP) {
                method.setAccessible(true);
                ENABLE_METHODS.add(method);
            } else {
                method.setAccessible(true);
                STARTUP_METHODS.add(method);
            }
        }
    }

    public static void executeEnable() {
        ENABLE_METHODS.sort(Comparator.comparingInt(m -> m.getAnnotation(InitMethod.class).priority().priority()));
        for (Method method : ENABLE_METHODS) {
            try {
                method.invoke(null);
                if (NeoArtisan.isDebugMode()) {
                    NeoArtisan.logger().info("successfully initialize method: " + method.getDeclaringClass().getName() + "." + method.getName());
                }
            } catch (InvocationTargetException e) {
                NeoArtisan.logger().severe("fail to initialize method: " + method + ", " + e + ", cause: " + e.getCause());
                NeoArtisan.logger().severe("fail to enable plugin. plugin disabling...");
                Bukkit.getPluginManager().disablePlugin(NeoArtisan.instance());
                return;
            } catch (Exception e) {
                NeoArtisan.logger().severe("fail to initialize method: " + method + ", " + e);
                NeoArtisan.logger().severe("fail to enable plugin. plugin disabling...");
                Bukkit.getPluginManager().disablePlugin(NeoArtisan.instance());
                return;
            }
        }
        NeoArtisan.logger().info("successfully initialize and plugin is enabled.");
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void executeStartup() {
        for (Method method : STARTUP_METHODS) {
            try {
                method.invoke(null);
                if (NeoArtisan.isDebugMode()) {
                    NeoArtisan.logger().info("successfully initialize method: " + method.getDeclaringClass().getName() + "." + method.getName());
                }
            } catch (InvocationTargetException e) {
                NeoArtisan.logger().severe("fail to initialize method: " + method + ", " + e + ", cause: " + e.getCause());
                NeoArtisan.logger().severe("fail to startup plugin. plugin disabling...");
                Bukkit.getPluginManager().disablePlugin(NeoArtisan.instance());
                return;
            } catch (Exception e) {
                NeoArtisan.logger().severe("fail to initialize method: " + method + ", " + e);
                NeoArtisan.logger().severe("fail to startup plugin. plugin disabling...");
                for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
                    if (plugin.isEnabled() && plugin.getPluginMeta().getPluginDependencies().contains("NeoArtisan")) {
                        Bukkit.getPluginManager().disablePlugin(plugin);
                    } else if (plugin.isEnabled() && plugin.getPluginMeta().getPluginSoftDependencies().contains("NeoArtisan")) {
                        Bukkit.getPluginManager().disablePlugin(plugin);
                    }
                }
                Bukkit.getPluginManager().disablePlugin(NeoArtisan.instance());
                return;
            }
        }
        NeoArtisan.logger().info("plugin successfully startup.");
    }
}

