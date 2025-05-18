package io.github.moyusowo.neoartisan.util.terminate;

import io.github.moyusowo.neoartisan.NeoArtisan;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import java.lang.invoke.TypeDescriptor;
import java.lang.reflect.Method;
import java.util.*;

public final class Terminator {
    private static final List<MethodInf> DISABLE_METHODS = Collections.synchronizedList(new ArrayList<>());

    record MethodInf(String className, String methodName) {
        void execute() throws Exception {
            try {
                Class<?> clazz = NeoArtisan.instance().getClass().getClassLoader().loadClass(className);
                Method method = clazz.getDeclaredMethod(methodName);
                method.setAccessible(true);
                method.invoke(null);
            } catch (ClassNotFoundException e) {
                NeoArtisan.logger().severe("方法调用失败: " + className + "." + methodName + ": " + e);
                Class<?> clazz = Class.forName(className);
                Method method = clazz.getDeclaredMethod(methodName);
                method.invoke(null);
            }
        }
    }

    public static void scanPackage(String pkg) {
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .forPackage(pkg)
                        .setScanners(Scanners.MethodsAnnotated)
        );
        Set<Method> methods = reflections.getMethodsAnnotatedWith(TerminateMethod.class);
        for (Method method : methods) {
            method.setAccessible(true);
            DISABLE_METHODS.add(new MethodInf(method.getDeclaringClass().getName(), method.getName()));
        }
    }

    public static void executeDisable() {

        DISABLE_METHODS.forEach(method -> {
            try {
                method.execute();
            } catch (Exception e) {
                NeoArtisan.logger().severe("方法调用失败: " + method.className + "." + method.methodName + ": " + e);
            }
        });

        NeoArtisan.logger().info("插件关闭成功！");
    }
}
