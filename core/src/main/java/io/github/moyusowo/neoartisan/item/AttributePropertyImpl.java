package io.github.moyusowo.neoartisan.item;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.item.AttributeProperty;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;

final class AttributePropertyImpl implements AttributeProperty {
    @SuppressWarnings("all")
    private final Map<NamespacedKey, ObjectWithType> attributes;

    private record ObjectWithType<P, C>(@NotNull C object, @NotNull PersistentDataType<P, C> type) {}

    AttributePropertyImpl() {
        this.attributes = new HashMap<>();
    }

    @InitMethod(priority = InitPriority.REGISTRAR)
    static void init() {
        Bukkit.getServicesManager().register(
                AttributeProperty.class,
                new AttributePropertyImpl(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    @Override
    public <P, C> AttributeProperty addAttribute(@NotNull NamespacedKey key, @NotNull PersistentDataType<P, C> type, @NotNull C value) {
        if (attributes.containsKey(key)) throw new IllegalArgumentException("key already exists!");
        attributes.put(key, new ObjectWithType<>(value, type));
        return this;
    }

    @Override
    public boolean hasAttribute(@NotNull NamespacedKey key) {
        return attributes.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> @NotNull T getAttribute(@NotNull NamespacedKey key, @NotNull Class<T> type) {
        return (T) attributes.get(key).object;
    }

    @Override
    public boolean isEmpty() {
        return this.attributes.isEmpty();
    }

    @Override
    public @Unmodifiable @NotNull Collection<NamespacedKey> getAttributeKeys() {
        return Collections.unmodifiableCollection(attributes.keySet());
    }

    @Override
    @SuppressWarnings("all")
    public void setPersistenceDataContainer(@NotNull PersistentDataContainer persistenceDataContainer) {
        for (NamespacedKey key : getAttributeKeys()) {
            ObjectWithType objectWithType = attributes.get(key);
            persistenceDataContainer.set(key, objectWithType.type, objectWithType.object);
        }
    }
}
