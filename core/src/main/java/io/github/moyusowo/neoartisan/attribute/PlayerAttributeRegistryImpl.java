package io.github.moyusowo.neoartisan.attribute;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisanapi.api.attribute.AttributeTypeRegistry;
import io.github.moyusowo.neoartisanapi.api.attribute.PlayerAttributeRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class PlayerAttributeRegistryImpl implements PlayerAttributeRegistry {

    private static PlayerAttributeRegistryImpl instance;

    public static void init() {
        new PlayerAttributeRegistryImpl();
    }

    public static PlayerAttributeRegistryImpl getInstance() {
        return instance;
    }

    private PlayerAttributeRegistryImpl() {
        instance = this;
        playerAttributeRegistry = new HashMap<>();
        registerFromFile();
        NeoArtisan.logger().info("成功从文件注册 " + (playerAttributeRegistry.size()) + " 个玩家自定义属性");
    }

    private final Map<NamespacedKey, String> playerAttributeRegistry;

    private void registerFromFile() {
        File file = ReadUtil.readAttributeFiles();
        File playerFile = new File(file, "player_attribute.yml");
        if (playerFile.isFile() && ReadUtil.isYmlFile(playerFile)) {
            YamlConfiguration global = YamlConfiguration.loadConfiguration(playerFile);
            for (String key : global.getKeys(false)) {
                String value = global.getString(key);
                if (!AttributeTypeRegistryImpl.getInstance().hasAttributeType(value)) throw new IllegalArgumentException("You must provide a legal type name!");
                playerAttributeRegistry.put(new NamespacedKey(NeoArtisan.instance(), key), value);
            }
        }
    }

    @Override
    public void registerPlayerAttribute(@NotNull NamespacedKey attributeKey, @NotNull String typeName) {
        if (!AttributeTypeRegistry.getAttributeTypeRegistryManager().hasAttributeType(typeName)) throw new IllegalArgumentException("You must provide a legal type name!");
        playerAttributeRegistry.put(attributeKey, typeName);
    }

    @Override
    public boolean hasPlayerAttribute(@NotNull NamespacedKey attributeKey) {
        return playerAttributeRegistry.containsKey(attributeKey);
    }

    @Override
    public @NotNull String getPlayerAttributeTypeName(@NotNull NamespacedKey attributeKey) {
        if (!playerAttributeRegistry.containsKey(attributeKey)) throw new IllegalArgumentException("You must check if attribute exists before get!");
        return playerAttributeRegistry.get(attributeKey);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> @Nullable T getPlayerAttribute(@NotNull Player player, @NotNull NamespacedKey attributeKey) {
        if (!PlayerAttributeRegistry.getPlayerAttributeRegistryManager().hasPlayerAttribute(attributeKey)) throw new IllegalArgumentException("You didn't register the attribute!");
        if (player.getPersistentDataContainer().has(attributeKey)) {
            String typeName = PlayerAttributeRegistry.getPlayerAttributeRegistryManager().getPlayerAttributeTypeName(attributeKey);
            return (T) player.getPersistentDataContainer().get(attributeKey, AttributeTypeRegistry.getAttributeTypeRegistryManager().getAttributePDCType(typeName));
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> void setPlayerAttribute(@NotNull Player player, @NotNull NamespacedKey attributeKey, T value) {
        if (!PlayerAttributeRegistry.getPlayerAttributeRegistryManager().hasPlayerAttribute(attributeKey)) throw new IllegalArgumentException("You didn't register the attribute!");
        if (player.getPersistentDataContainer().has(attributeKey)) {
            player.getPersistentDataContainer().remove(attributeKey);
        }
        String typeName = PlayerAttributeRegistry.getPlayerAttributeRegistryManager().getPlayerAttributeTypeName(attributeKey);
        PersistentDataType<?, T> type = (PersistentDataType<?, T>) AttributeTypeRegistry.getAttributeTypeRegistryManager().getAttributePDCType(typeName);
        player.getPersistentDataContainer().set(attributeKey, type, value);
    }
}
