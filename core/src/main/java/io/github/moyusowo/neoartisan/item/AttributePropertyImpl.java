package io.github.moyusowo.neoartisan.item;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisanapi.api.attribute.AttributeRegistry;
import io.github.moyusowo.neoartisanapi.api.attribute.AttributeTypeRegistry;
import io.github.moyusowo.neoartisanapi.api.item.AttributeProperty;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

class AttributePropertyImpl implements AttributeProperty {

    private final Map<NamespacedKey, Object> globalAttributeValues, itemstackAttributeValues;

    public AttributePropertyImpl() {
        this.globalAttributeValues = new HashMap<>();
        this.itemstackAttributeValues = new HashMap<>();
    }

    @InitMethod
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
        if (!AttributeRegistry.getAttributeRegistryManager().hasGlobalAttribute(attributeKey)) throw new IllegalArgumentException("You didn't register this attribute!");
        String typeName = AttributeRegistry.getAttributeRegistryManager().getGlobalAttributeTypeName(attributeKey);
        Class<?> typeJavaClass = AttributeTypeRegistry.getAttributeTypeRegistryManager().getAttributeJavaType(typeName);
        if (!typeJavaClass.isInstance(value)) throw new IllegalArgumentException("the value doesn't match the attribute!");
        this.globalAttributeValues.put(attributeKey, value);
        return this;
    }

    @Override
    public AttributeProperty addItemstackAttribute(@NotNull NamespacedKey attributeKey, @NotNull Object value) {
        if (attributeKey == null) throw new IllegalArgumentException("You can't provide a null key!");
        if (!AttributeRegistry.getAttributeRegistryManager().hasItemStackAttribute(attributeKey)) throw new IllegalArgumentException("You didn't register this attribute!");
        String typeName = AttributeRegistry.getAttributeRegistryManager().getItemStackAttributeTypeName(attributeKey);
        Class<?> typeJavaClass = AttributeTypeRegistry.getAttributeTypeRegistryManager().getAttributeJavaType(typeName);
        if (!typeJavaClass.isInstance(value)) throw new IllegalArgumentException("the value doesn't match the attribute!");
        this.itemstackAttributeValues.put(attributeKey, value);
        return this;
    }

    @Override
    public boolean hasGlobalAttribute(@NotNull NamespacedKey attributeKey) {
        return this.globalAttributeValues.containsKey(attributeKey);
    }

    @Override
    public boolean hasItemstackAttribute(@NotNull NamespacedKey attributeKey) {
        return this.itemstackAttributeValues.containsKey(attributeKey);
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <T> T getGlobalAttributeValue(@NotNull NamespacedKey attributeKey) {
        String typeName = AttributeRegistry.getAttributeRegistryManager().getGlobalAttributeTypeName(attributeKey);
        Class<T> type = (Class<T>) AttributeTypeRegistry.getAttributeTypeRegistryManager().getAttributeJavaType(typeName);
        return type.cast(this.globalAttributeValues.get(attributeKey));
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <T> T getItemstackAttributeValue(@NotNull NamespacedKey attributeKey) {
        String typeName = AttributeRegistry.getAttributeRegistryManager().getItemStackAttributeTypeName(attributeKey);
        Class<T> type = (Class<T>) AttributeTypeRegistry.getAttributeTypeRegistryManager().getAttributeJavaType(typeName);
        return type.cast(this.itemstackAttributeValues.get(attributeKey));
    }

    @Override
    public boolean isEmpty() {
        return this.globalAttributeValues.isEmpty() && this.itemstackAttributeValues.isEmpty();
    }

    @Override
    public NamespacedKey[] getItemstackAttributeKeys() {
        return this.itemstackAttributeValues.keySet().toArray(new NamespacedKey[0]);
    }
}
