package io.github.moyusowo.neoartisan.item;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.item.AttributeProperty;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

final class AttributePropertyImpl implements AttributeProperty {

    private final Map<NamespacedKey, Object> globalAttributeValues, itemstackAttributeValues;

    AttributePropertyImpl() {
        this.globalAttributeValues = new HashMap<>();
        this.itemstackAttributeValues = new HashMap<>();
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
    public AttributeProperty addGlobalAttribute(@NotNull NamespacedKey attributeKey, @NotNull Object value) {
        if (!NeoArtisanAPI.getGlobalAttributeRegistry().hasAttribute(attributeKey)) throw new IllegalArgumentException("You didn't register this attribute!");
        Class<?> typeJavaClass = NeoArtisanAPI.getGlobalAttributeRegistry().getAttributeJavaType(attributeKey);
        if (!typeJavaClass.isInstance(value)) throw new IllegalArgumentException("the value doesn't match the attribute!");
        this.globalAttributeValues.put(attributeKey, value);
        return this;
    }

    @Override
    public AttributeProperty addItemStackAttribute(@NotNull NamespacedKey attributeKey, @NotNull Object value) {
        if (attributeKey == null) throw new IllegalArgumentException("You can't provide a null key!");
        if (!NeoArtisanAPI.getItemStackAttributeRegistry().hasAttribute(attributeKey)) throw new IllegalArgumentException("You didn't register this attribute!");
        Class<?> typeJavaClass = NeoArtisanAPI.getItemStackAttributeRegistry().getAttributeJavaType(attributeKey);
        if (!typeJavaClass.isInstance(value)) throw new IllegalArgumentException("the value doesn't match the attribute!");
        this.itemstackAttributeValues.put(attributeKey, value);
        return this;
    }

    @Override
    public boolean hasGlobalAttribute(@NotNull NamespacedKey attributeKey) {
        return this.globalAttributeValues.containsKey(attributeKey);
    }

    @Override
    public boolean hasItemStackAttribute(@NotNull NamespacedKey attributeKey) {
        return this.itemstackAttributeValues.containsKey(attributeKey);
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <T> T getGlobalAttributeValue(@NotNull NamespacedKey attributeKey) {
        Class<T> type = (Class<T>) NeoArtisanAPI.getGlobalAttributeRegistry().getAttributeJavaType(attributeKey);
        return type.cast(this.globalAttributeValues.get(attributeKey));
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <T> T getItemStackAttributeValue(@NotNull NamespacedKey attributeKey) {
        Class<T> type = (Class<T>) NeoArtisanAPI.getItemStackAttributeRegistry().getAttributeJavaType(attributeKey);
        return type.cast(this.itemstackAttributeValues.get(attributeKey));
    }

    @Override
    public boolean isEmpty() {
        return this.globalAttributeValues.isEmpty() && this.itemstackAttributeValues.isEmpty();
    }

    @Override
    public NamespacedKey[] getItemStackAttributeKeys() {
        return this.itemstackAttributeValues.keySet().toArray(new NamespacedKey[0]);
    }
}
