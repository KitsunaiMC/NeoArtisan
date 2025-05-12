package io.github.moyusowo.neoartisan;

import io.github.moyusowo.neoartisan.attribute.AttributeInit;
import io.github.moyusowo.neoartisan.block.ArtisanBlockInit;
import io.github.moyusowo.neoartisan.block.crop.CropDataSerializer;
import io.github.moyusowo.neoartisan.item.ArtisanItemInit;
import io.github.moyusowo.neoartisan.recipe.ArtisanRecipeInit;
import io.github.moyusowo.neoartisan.util.Debug;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Logger;

public final class NeoArtisan extends JavaPlugin {

    private static NeoArtisan instance;
    private static NamespacedKey artisanItemIdKey;
    private static NamespacedKey artisanItemAttackDamageKey, artisanItemAttackKnockbackKey, artisanItemAttackSpeedKey;

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

    @Override
    public void onEnable() {
        AttributeInit.init();
        ArtisanItemInit.init();
        ArtisanRecipeInit.init();
        ArtisanBlockInit.init();
        Debug.init();
    }

    @Override
    public void onDisable() {
        Bukkit.resetRecipes();
        try {
            CropDataSerializer.save();
            NeoArtisan.logger().info("作物数据保存成功");
        } catch (IOException e) {
            NeoArtisan.logger().severe("作物数据保存失败: " + e);
        }
    }
}
