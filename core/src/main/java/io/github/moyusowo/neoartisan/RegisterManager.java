package io.github.moyusowo.neoartisan;

import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public final class RegisterManager {

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
        if (NeoArtisan.isDebugMode()) {
            NeoArtisan.logger().info(Arrays.toString(Bukkit.getPluginManager().getPlugins()));
        }
        for (
                Plugin plugin : Arrays.stream(Bukkit.getPluginManager().getPlugins())
                .filter(
                    p ->
                        p.getPluginMeta().getPluginDependencies().contains("NeoArtisan") ||
                        p.getPluginMeta().getPluginSoftDependencies().contains("NeoArtisan") ||
                        p.getName().equals("NeoArtisan")
                )
                .toList()
        ) {
            String pkg = plugin.getClass().getPackageName();
            if (NeoArtisan.isDebugMode()) {
                NeoArtisan.logger().info(pkg);
            }
            Reflections reflections = new Reflections(
                    new ConfigurationBuilder()
                            .forPackage(pkg, plugin.getClass().getClassLoader())
                            .addClassLoaders(plugin.getClass().getClassLoader())
                            .setScanners(Scanners.MethodsAnnotated)
            );
            Set<Method> methods = reflections.getMethodsAnnotatedWith(NeoArtisanAPI.Register.class);
            for (Method method : methods) {
                method.setAccessible(true);
                try {
                    method.invoke(null);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    NeoArtisan.logger().info("注册方法 " + method.getDeclaringClass().getPackageName() + "." + method.getDeclaringClass().getName() + " 执行失败：" + e.getLocalizedMessage());
                }
                NeoArtisan.logger().info("成功执行注册方法：" + method.getDeclaringClass().getPackageName() + "." + method.getDeclaringClass().getName());
            }
        }
    }

    public static boolean isOpen() {
        return status == Status.OPEN;
    }

    private enum Status {
        OPEN,
        CLOSED;
    }

    public static final class RegisterException extends Exception {
        private RegisterException() {
            super("Registry is closed! Please register with annotation!");
        }

        public static RegisterException exception() {
            return new RegisterException();
        }
    }

}
