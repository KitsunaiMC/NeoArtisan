package io.github.moyusowo.neoartisan.recipe;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisanapi.api.item.ItemRegistry;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareSmithingEvent;

final class SmithingBehavior implements Listener {

    private SmithingBehavior() {}

    @InitMethod
    static void registerListener() {
        NeoArtisan.registerListener(new SmithingBehavior());
    }

    @EventHandler
    private void onSmithing(PrepareSmithingEvent event) {
        if (event.getInventory().getInputEquipment() == null) return;
        if (ItemRegistry.getItemRegistryManager().isArtisanItem(event.getInventory().getInputEquipment())) event.setResult(null);
    }

}
