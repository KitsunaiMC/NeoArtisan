package io.github.moyusowo.neoartisanapi.api.util;

import org.bukkit.Bukkit;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public final class ServiceUtil {
    private ServiceUtil() {}

    public static <T> T getService(Class<T> type) {
        T builder = Bukkit.getServicesManager().load(type);
        if (builder == null) {
            throw new IllegalStateException("Builder service not registered for: " + type.getName());
        }
        return builder;
    }

    public static <T> T createProxy(Class<T> serviceInterface) {
        return serviceInterface.cast(
                Proxy.newProxyInstance(
                        serviceInterface.getClassLoader(),
                        new Class<?>[]{ serviceInterface },
                        new LazyServiceHandler<>(serviceInterface)
                )
        );
    }

    private static final class LazyServiceHandler<T> implements InvocationHandler {
        private final Class<T> type;
        private volatile T real;

        LazyServiceHandler(Class<T> type) {
            this.type = type;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            T instance = real;
            if (instance == null) {
                synchronized (this) {
                    instance = real;
                    if (instance == null) {
                        instance = Bukkit.getServicesManager().load(type);
                        if (instance == null) {
                            throw new IllegalStateException(
                                    type.getSimpleName() + " service not found!");
                        }
                        real = instance;
                    }
                }
            }
            return method.invoke(instance, args);
        }
    }

}
