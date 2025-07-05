package io.github.moyusowo.neoartisan.item.listener;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import io.papermc.paper.event.player.PlayerTradeEvent;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.MerchantInventory;

final class ItemBehaviorListener implements Listener {
    @InitMethod(priority = InitPriority.LISTENER)
    static void init() { NeoArtisan.registerListener(new ItemBehaviorListener()); }

    private ItemBehaviorListener() {}

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlace(BlockPlaceEvent event) {
        if (event.isCancelled()) return;
        if (!NeoArtisanAPI.getItemRegistry().isArtisanItem(event.getItemInHand())) return;
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTrade(PlayerTradeEvent event) {
        if (event.isCancelled()) return;
        MerchantInventory inventory = (MerchantInventory) event.getPlayer().getOpenInventory().getTopInventory();
        var ingredients = event.getTrade().getIngredients();
        final NamespacedKey id0 = NeoArtisanAPI.getItemRegistry().getRegistryId(inventory.getItem(0));
        final NamespacedKey id1 = NeoArtisanAPI.getItemRegistry().getRegistryId(inventory.getItem(1));
        final NamespacedKey originalId0 = NeoArtisanAPI.getItemRegistry().getRegistryId(ingredients.getFirst()), originalId1;
        if (ingredients.size() == 1) {
            originalId1 = ArtisanItem.EMPTY;
        } else {
            originalId1 = NeoArtisanAPI.getItemRegistry().getRegistryId(ingredients.get(1));
        }
        if (id0.equals(originalId0) && id1.equals(originalId1)) return;
        if (id1.equals(originalId0) && id0.equals(originalId1)) return;
        event.setCancelled(true);
        event.setIncreaseTradeUses(false);
        event.setRewardExp(false);
    }

}
