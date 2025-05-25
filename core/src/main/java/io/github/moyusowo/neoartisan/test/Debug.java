package io.github.moyusowo.neoartisan.test;

import io.github.moyusowo.neoartisan.NeoArtisan;

import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.item.ItemRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class Debug implements Listener {

    private Debug() {}

    @InitMethod(priority = InitPriority.LISTENER)
    public static void init() {
        if (NeoArtisan.isDebugMode()) {
            NeoArtisan.registerListener(new Debug());
        }
    }

    @EventHandler
    private static void onPlayerAttack(EntityDamageByEntityEvent event) {

    }

}
