package io.github.moyusowo.neoartisan;

import io.github.moyusowo.neoartisan.util.init.Initializer;
import io.github.moyusowo.neoartisan.util.terminate.Terminator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Logger;

public final class NeoArtisan extends JavaPlugin {

    private static final String pkg = "io.github.moyusowo.neoartisan";
    private static final boolean isDebugMode = true;

    private static NeoArtisan instance;
    private static NamespacedKey artisanItemIdKey;
    private static NamespacedKey artisanItemAttackDamageKey, artisanItemAttackKnockbackKey, artisanItemAttackSpeedKey;
    private static PersistentDataAdapterContext persistentDataAdapterContext;

    public NeoArtisan() {
        super();
        instance = this;
        artisanItemIdKey = new NamespacedKey(this, "registry_id");
        artisanItemAttackDamageKey = new NamespacedKey("minecraft", "base_attack_damage");
        artisanItemAttackKnockbackKey = new NamespacedKey("minecraft", "base_attack_knockback");
        artisanItemAttackSpeedKey = new NamespacedKey("minecraft", "base_attack_speed");
    }

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
        return isDebugMode;
    }

    public static PersistentDataContainer emptyPersistentDataContainer() { return persistentDataAdapterContext.newPersistentDataContainer(); }

    @Override
    public void onEnable() {
        persistentDataAdapterContext = ItemStack.of(Material.STICK).getItemMeta().getPersistentDataContainer().getAdapterContext();
        Initializer.scanPackage(pkg);
        Initializer.executeEnable();
        Terminator.scanPackage(pkg);
    }

    @Override
    public void onDisable() {
        Terminator.executeDisable();
    }
}
