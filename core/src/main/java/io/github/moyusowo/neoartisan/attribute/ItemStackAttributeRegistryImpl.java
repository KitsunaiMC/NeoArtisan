package io.github.moyusowo.neoartisan.attribute;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.RegisterManager;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.attribute.ItemStackAttributeRegistry;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class ItemStackAttributeRegistryImpl implements ItemStackAttributeRegistry {

    private static ItemStackAttributeRegistryImpl instance;

    @InitMethod(priority = InitPriority.REGISTRY_LOAD)
    public static void init() {
        new ItemStackAttributeRegistryImpl();
    }

    public static ItemStackAttributeRegistryImpl getInstance() {
        return instance;
    }

    private ItemStackAttributeRegistryImpl() {
        instance = this;
        ItemStackAttributeRegistry = new HashMap<>();
        Bukkit.getServicesManager().register(
                ItemStackAttributeRegistry.class,
                ItemStackAttributeRegistryImpl.getInstance(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private final Map<NamespacedKey, PersistentDataType<?, ?>> ItemStackAttributeRegistry;

    @Override
    public void registerAttribute(@NotNull NamespacedKey attributeKey, @NotNull PersistentDataType<?, ?> pdcType) {
        try {
            if (RegisterManager.isOpen()) {
                ItemStackAttributeRegistry.put(attributeKey, pdcType);
            } else {
                throw RegisterManager.RegisterException.exception();
            }
        } catch (RegisterManager.RegisterException e) {
            NeoArtisan.logger().severe(RegisterManager.eTips);
        }
    }

    @Override
    public boolean hasAttribute(@NotNull NamespacedKey attributeKey) {
        return ItemStackAttributeRegistry.containsKey(attributeKey);
    }

    @Override
    public @NotNull Class<?> getAttributeJavaType(@NotNull NamespacedKey attributeKey) {
        if (!ItemStackAttributeRegistry.containsKey(attributeKey)) throw new IllegalArgumentException("You must check if attribute exists before get!");
        return ItemStackAttributeRegistry.get(attributeKey).getComplexType();
    }

    @Override
    public @NotNull PersistentDataType<?, ?> getAttributePDCType(@NotNull NamespacedKey attributeKey) {
        if (!ItemStackAttributeRegistry.containsKey(attributeKey)) throw new IllegalArgumentException("You must check if attribute exists before get!");
        return ItemStackAttributeRegistry.get(attributeKey);
    }

    @Override
    @SuppressWarnings("unchecked")
    public @Nullable <T> T getItemStackAttributeValue(@NotNull ItemStack itemStack, @NotNull NamespacedKey attributeKey) {
        if (itemStack.getPersistentDataContainer().has(attributeKey)) {
            if (!hasAttribute(attributeKey)) throw new IllegalArgumentException("You didn't register the attribute!");
            return (T) itemStack.getPersistentDataContainer().get(attributeKey, getAttributePDCType(attributeKey));
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void setItemStackAttributeValue(@NotNull ItemStack itemStack, @NotNull NamespacedKey attributeKey, @NotNull T value) {
        if (!hasAttribute(attributeKey)) throw new IllegalArgumentException("You didn't register the attribute!");
        ItemMeta meta = itemStack.getItemMeta();
        if (meta.getPersistentDataContainer().has(attributeKey)) {
            meta.getPersistentDataContainer().remove(attributeKey);
        }
        PersistentDataType<?, T> type = (PersistentDataType<?, T>) getAttributePDCType(attributeKey);
        meta.getPersistentDataContainer().set(attributeKey, type, value);
        itemStack.setItemMeta(meta);
    }
}
