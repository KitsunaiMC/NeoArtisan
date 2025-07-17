package io.github.moyusowo.neoartisan;

import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public final class RegisterManager {

    public static final IllegalAccessError REGISTRY_CLOSED = new IllegalAccessError("Registry closed! Please register in the methods with annotation.");
    public static final String eTips = "只能在使用注解的方法内注册！";

    private RegisterManager() {}

    private static Status status = Status.CLOSED;

    @InitMethod(priority = InitPriority.REGISTRY_OPEN)
    static void openRegister() {
        status = Status.OPEN;
    }

    @InitMethod(priority = InitPriority.REGISTRY_CLOSED)
    static void closeRegister() {
        status = Status.CLOSED;
    }

    @InitMethod(priority = InitPriority.REGISTER)
    static void register() {
        for (
                Plugin plugin : Arrays.stream(Bukkit.getPluginManager().getPlugins())
                .filter(
                    p ->
                        p.getPluginMeta().getPluginDependencies().contains("NeoArtisan") ||
                        p.getPluginMeta().getPluginSoftDependencies().contains("NeoArtisan") ||
                        p.getName().equals("NeoArtisan")
                )
                .filter(
                    p -> p.isEnabled() || p.getName().equals("NeoArtisan")
                )
                .toList()
        ) {
            String pkg = plugin.getClass().getPackageName();
            if (NeoArtisan.isDebugMode()) {
                NeoArtisan.logger().info(pkg);
            }
            try {
                Reflections reflections = new Reflections(
                        new ConfigurationBuilder()
                                .forPackage(pkg, plugin.getClass().getClassLoader())
                                .addClassLoaders(plugin.getClass().getClassLoader())
                                .setScanners(Scanners.MethodsAnnotated)
                );
                Set<Method> methods = reflections.getMethodsAnnotatedWith(NeoArtisanAPI.Register.class);
                for (Method method : methods) {
                    try {
                        method.setAccessible(true);
                        method.invoke(null);
                    } catch (Exception e) {
                        NeoArtisan.logger().severe("fail to run register method: "  + method.getDeclaringClass().getName() + "." + method.getName() + ", cause: " + e + ": " + e.getCause());
                    }
                }
            } catch (Throwable e) {
                NeoArtisan.logger().severe("fail to load plugin class: "  + pkg + ", " + e + ": " + e.getCause());
                NeoArtisan.logger().severe("plugin: " + plugin.getName() + " will not enable");
                HandlerList.unregisterAll(plugin);
                Bukkit.getScheduler().cancelTasks(plugin);
                Bukkit.getPluginManager().disablePlugin(plugin);
            }
        }
    }

    public static boolean isOpen() {
        return status == Status.OPEN;
    }

    private enum Status {
        NOT_YET_OPEN,
        OPEN,
        CLOSED;
    }

}
