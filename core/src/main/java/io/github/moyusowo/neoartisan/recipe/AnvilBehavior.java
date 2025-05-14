package io.github.moyusowo.neoartisan.recipe;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.item.ItemRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

final class AnvilBehavior implements Listener {

    private AnvilBehavior() {}

    @InitMethod(order = InitPriority.NORMAL)
    static void registerListener() {
        NeoArtisan.registerListener(new AnvilBehavior());
    }

    @EventHandler
    private void onAnvil(PrepareAnvilEvent event) {
        if (event.getResult() == null) return;
        ItemStack firstItem = event.getInventory().getFirstItem();
        ItemStack secondItem = event.getInventory().getSecondItem();
        if (ItemRegistry.getItemRegistryManager().isArtisanItem(firstItem)) {
            event.setResult(null);
            return;
        }
        if (ItemRegistry.getItemRegistryManager().isArtisanItem(secondItem)) {
            event.setResult(null);
        }
    }

}
