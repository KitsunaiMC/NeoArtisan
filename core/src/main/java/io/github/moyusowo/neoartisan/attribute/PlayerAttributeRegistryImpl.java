package io.github.moyusowo.neoartisan.attribute;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.RegisterManager;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.attribute.PlayerAttributeRegistry;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PlayerAttributeRegistryImpl implements PlayerAttributeRegistry {

    private static PlayerAttributeRegistryImpl instance;

    @InitMethod(priority = InitPriority.REGISTRY_LOAD)
    public static void init() {
        new PlayerAttributeRegistryImpl();
    }

    public static PlayerAttributeRegistryImpl getInstance() {
        return instance;
    }

    private PlayerAttributeRegistryImpl() {
        instance = this;
        playerAttributeRegistry = new HashMap<>();
        Bukkit.getServicesManager().register(
                PlayerAttributeRegistry.class,
                PlayerAttributeRegistryImpl.getInstance(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private final Map<NamespacedKey, PersistentDataType<?, ?>> playerAttributeRegistry;


    @Override
    public void registerAttribute(@NotNull NamespacedKey attributeKey, @NotNull PersistentDataType<?, ?> pdcType) {
        if (RegisterManager.isOpen()) {
            playerAttributeRegistry.put(attributeKey, pdcType);
            NeoArtisan.logger().info("successfully register player attribute: " + attributeKey.asString());
        } else {
            throw RegisterManager.REGISTRY_CLOSED;
        }
    }

    @Override
    public boolean hasAttribute(@NotNull NamespacedKey attributeKey) {
        return playerAttributeRegistry.containsKey(attributeKey);
    }

    @Override
    public @NotNull Class<?> getAttributeJavaType(@NotNull NamespacedKey attributeKey) {
        if (!playerAttributeRegistry.containsKey(attributeKey)) throw new IllegalArgumentException("You must check if attribute exists before get!");
        return playerAttributeRegistry.get(attributeKey).getComplexType();
    }

    @Override
    public @NotNull PersistentDataType<?, ?> getAttributePDCType(@NotNull NamespacedKey attributeKey) {
        if (!playerAttributeRegistry.containsKey(attributeKey)) throw new IllegalArgumentException("You must check if attribute exists before get!");
        return playerAttributeRegistry.get(attributeKey);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> @Nullable T getPlayerAttribute(@NotNull Player player, @NotNull NamespacedKey attributeKey) {
        if (!hasAttribute(attributeKey)) throw new IllegalArgumentException("You didn't register the attribute!");
        if (player.getPersistentDataContainer().has(attributeKey)) {
            return (T) player.getPersistentDataContainer().get(attributeKey, getAttributePDCType(attributeKey));
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void setPlayerAttribute(@NotNull Player player, @NotNull NamespacedKey attributeKey, T value) {
        if (!hasAttribute(attributeKey)) throw new IllegalArgumentException("You didn't register the attribute!");
        if (player.getPersistentDataContainer().has(attributeKey)) {
            player.getPersistentDataContainer().remove(attributeKey);
        }
        PersistentDataType<?, T> type = (PersistentDataType<?, T>) getAttributePDCType(attributeKey);
        player.getPersistentDataContainer().set(attributeKey, type, value);
    }
}
