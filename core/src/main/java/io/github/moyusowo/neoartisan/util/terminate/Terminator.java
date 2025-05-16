package io.github.moyusowo.neoartisan.util.terminate;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Method;
import java.util.*;

public final class Terminator {
    private static final List<Method> DISABLE_METHODS = Collections.synchronizedList(new ArrayList<>());

    public static void scanPackage(String pkg) {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .forPackage(pkg)
                        .setScanners(Scanners.MethodsAnnotated)
        );
        Set<Method> methods = reflections.getMethodsAnnotatedWith(TerminateMethod.class);
        for (Method method : methods) {
            method.setAccessible(true);
            DISABLE_METHODS.add(method);
        }
    }

    public static void executeDisable() {

        DISABLE_METHODS.forEach(method -> {
            try {
                method.invoke(null);
                if (NeoArtisan.isDebugMode()) {
                    NeoArtisan.logger().info("成功调用关闭前方法: " + method.getDeclaringClass().getName() + "." + method.getName());
                }
            } catch (Exception e) {
                throw new RuntimeException("方法调用失败: " + method, e);
            }
        });

        NeoArtisan.logger().info("插件关闭成功！");
    }
}
