package io.github.moyusowo.neoartisan.attribute;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.attribute.AttributeRegistry;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

final class AttributeRegistryImpl implements AttributeRegistry {

    @InitMethod(order = InitPriority.HIGH)
    public static void init() {
        new AttributeRegistryImpl();
    }

    private AttributeRegistryImpl() {
        instance = this;
        globalAttributeRegistry = new HashMap<>();
        itemstackAttributeRegistry = new HashMap<>();
        Bukkit.getServicesManager().register(
                AttributeRegistry.class,
                AttributeRegistryImpl.getInstance(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private final Map<NamespacedKey, String> globalAttributeRegistry, itemstackAttributeRegistry;

    private static AttributeRegistryImpl instance;

    public static AttributeRegistryImpl getInstance() {
        return instance;
    }

    @Override
    public void registerGlobalAttribute(@NotNull NamespacedKey attributeKey, @NotNull String typeName) {
        if (!AttributeTypeRegistryImpl.getInstance().hasAttributeType(typeName)) throw new IllegalArgumentException("You must provide a legal type name!");
        globalAttributeRegistry.put(attributeKey, typeName);
    }

    @Override
    public void registerItemstackAttribute(@NotNull NamespacedKey attributeKey, @NotNull String typeName) {
        if (!AttributeTypeRegistryImpl.getInstance().hasAttributeType(typeName)) throw new IllegalArgumentException("You must provide a legal type name!");
        itemstackAttributeRegistry.put(attributeKey, typeName);
    }

    @Override
    public boolean hasGlobalAttribute(@NotNull NamespacedKey attributeKey) {
        return globalAttributeRegistry.containsKey(attributeKey);
    }

    @Override
    public boolean hasItemstackAttribute(@NotNull NamespacedKey attributeKey) {
        return itemstackAttributeRegistry.containsKey(attributeKey);
    }

    @Override
    public @NotNull String getGlobalAttributeTypeName(@NotNull NamespacedKey attributeKey) {
        if (!globalAttributeRegistry.containsKey(attributeKey)) throw new IllegalArgumentException("You must check if attribute exists before get!");
        return globalAttributeRegistry.get(attributeKey);
    }

    @Override
    public @NotNull String getItemstackAttributeTypeName(@NotNull NamespacedKey attributeKey) {
        if (!itemstackAttributeRegistry.containsKey(attributeKey)) throw new IllegalArgumentException("You must check if attribute exists before get!");
        return itemstackAttributeRegistry.get(attributeKey);
    }
}
