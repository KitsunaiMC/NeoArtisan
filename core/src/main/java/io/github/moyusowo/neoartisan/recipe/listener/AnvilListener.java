package io.github.moyusowo.neoartisan.recipe.listener;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

final class AnvilListener implements Listener {

    private AnvilListener() {}

    @InitMethod(priority = InitPriority.LISTENER)
    static void registerListener() {
        NeoArtisan.registerListener(new AnvilListener());
    }

    @EventHandler
    public void onAnvil(PrepareAnvilEvent event) {
        if (event.getResult() == null) return;
        ItemStack firstItem = event.getInventory().getFirstItem();
        ItemStack secondItem = event.getInventory().getSecondItem();
        if (NeoArtisanAPI.getItemRegistry().isArtisanItem(firstItem)) {
            event.setResult(null);
            return;
        }
        if (NeoArtisanAPI.getItemRegistry().isArtisanItem(secondItem)) {
            event.setResult(null);
        }
    }

}
