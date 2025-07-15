package io.github.moyusowo.neoartisan;

import io.github.moyusowo.neoartisan.util.Util;
import io.github.moyusowo.neoartisan.util.init.Initializer;
import io.github.moyusowo.neoartisan.util.terminate.Terminator;
import io.github.moyusowo.neoartisanapi.api.persistence.EmptyPersistentDataContainer;
import io.github.moyusowo.neoartisanapi.api.persistence.type.ItemStackDataType;
import net.momirealms.antigrieflib.AntiGriefLib;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.logging.Logger;

public final class NeoArtisan extends JavaPlugin implements EmptyPersistentDataContainer {

    private static final String pkg = "io.github.moyusowo.neoartisan";
    private final boolean isDebugMode;

    private static NeoArtisan instance;
    private static NamespacedKey artisanItemIdKey;
    private static NamespacedKey artisanItemAttackDamageKey, artisanItemAttackKnockbackKey, artisanItemAttackSpeedKey;
    private static PersistentDataAdapterContext persistentDataAdapterContext;
    private static AntiGriefLib antiGriefLib;

    public NeoArtisan() {
        super();
        instance = this;
        artisanItemIdKey = new NamespacedKey(this, "registry_id");
        artisanItemAttackDamageKey = new NamespacedKey("minecraft", "base_attack_damage");
        artisanItemAttackKnockbackKey = new NamespacedKey("minecraft", "base_attack_knockback");
        artisanItemAttackSpeedKey = new NamespacedKey("minecraft", "base_attack_speed");
        Util.saveDefaultIfNotExists("config.yml");
        File configFile = new File(getDataFolder(), "config.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        isDebugMode = config.getBoolean("debug");
    }

    public static AntiGriefLib getAntiGriefLib() { return antiGriefLib; }

    public static NamespacedKey getArtisanItemIdKey() {
        return artisanItemIdKey;
    }

    public static NamespacedKey getArtisanItemAttackDamageKey() {
        return artisanItemAttackDamageKey;
    }

    public static NamespacedKey getArtisanItemAttackKnockbackKey() {
        return artisanItemAttackKnockbackKey;
    }

    public static NamespacedKey getArtisanItemAttackSpeedKey() {
        return artisanItemAttackSpeedKey;
    }

    public static NeoArtisan instance() {
        return instance;
    }

    public static Logger logger() {
        return instance.getLogger();
    }

    public static void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, instance);
    }

    public static Server server() {
        return instance.getServer();
    }

    public static boolean isDebugMode() {
        return instance().isDebugMode;
    }

    public PersistentDataContainer emptyPersistentDataContainer() { return persistentDataAdapterContext.newPersistentDataContainer(); }

    @Override
    public void onEnable() {
        antiGriefLib = AntiGriefLib.builder(this).silentLogs(true).ignoreOP(true).build();
        ItemStackDataType.ITEM_STACK.getComplexType();
        persistentDataAdapterContext = ItemStack.of(Material.STICK).getItemMeta().getPersistentDataContainer().getAdapterContext();
        Bukkit.getServicesManager().register(
                EmptyPersistentDataContainer.class,
                this,
                this,
                ServicePriority.Normal
        );
        Initializer.scanPackage(pkg);
        Initializer.executeEnable();
        Terminator.scanPackage(pkg);
    }

    @Override
    public void onDisable() {
        Terminator.executeDisable();
    }
}
