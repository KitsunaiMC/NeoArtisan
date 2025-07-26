package io.github.moyusowo.neoartisan.recipe.guide.listener;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.recipe.guide.index.GuideIndexHolder;
import io.github.moyusowo.neoartisan.recipe.guide.index.GuideIndexManager;
import io.github.moyusowo.neoartisan.recipe.guide.item.GuideGUIHolder;
import io.github.moyusowo.neoartisan.recipe.guide.item.GuideGUIType;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.registry.Registries;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public final class GuideListener implements Listener {
    private GuideListener() {}

    @InitMethod(priority = InitPriority.LISTENER)
    static void init() {
        NeoArtisan.registerListener(new GuideListener());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
        if (event.useInteractedBlock() == Event.Result.DENY || event.useItemInHand() == Event.Result.DENY) return;
        if (!event.getAction().isRightClick()) return;
        if (event.getItem() == null || event.getMaterial() != Material.BONE_BLOCK) return;
        event.setCancelled(true);
        GuideIndexManager.openIndex(event.getPlayer(), 0);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getInventory().getHolder() instanceof GuideGUIHolder holder) {
            event.setCancelled(true);
            if (event.getRawSlot() == holder.nextPageSlot) {
                GuideIndexManager.openItemGuide(holder.itemId, player, holder.type, holder.page + 1);
            } else if (event.getRawSlot() == holder.lastPageSlot) {
                GuideIndexManager.openItemGuide(holder.itemId, player, holder.type, holder.page - 1);
            } else if (event.getRawSlot() == holder.returnPageSlot) {
                GuideIndexManager.openIndex(player, 0);
            } else if (event.getClickedInventory() == holder.getInventory() && holder.getInventory().getItem(event.getRawSlot()) != null && !holder.getInventory().getItem(event.getRawSlot()).isEmpty()) {
                if (event.getClick().isRightClick()) {
                    GuideIndexManager.openItemGuide(Registries.ITEM.getRegistryId(holder.getInventory().getItem(event.getRawSlot())), player, GuideGUIType.FROM, 0);
                } else if (event.getClick().isLeftClick()) {
                    GuideIndexManager.openItemGuide(Registries.ITEM.getRegistryId(holder.getInventory().getItem(event.getRawSlot())), player, GuideGUIType.TO, 0);
                }
            }
        }
        if (event.getInventory().getHolder() instanceof GuideIndexHolder holder) {
            event.setCancelled(true);
            if (event.getRawSlot() == GuideIndexHolder.nextPageSlot) {
                GuideIndexManager.openIndex(player, holder.page + 1);
            } else if (event.getRawSlot() == GuideIndexHolder.lastPageSlot) {
                GuideIndexManager.openIndex(player, holder.page - 1);
            } else if (event.getRawSlot() == GuideIndexHolder.returnSlot) {
                player.closeInventory();
            } else if (event.getClickedInventory() == holder.getInventory() && holder.getInventory().getItem(event.getRawSlot()) != null && !holder.getInventory().getItem(event.getRawSlot()).isEmpty()) {
                if (event.getClick().isRightClick()) {
                    GuideIndexManager.openItemGuide(Registries.ITEM.getRegistryId(holder.getInventory().getItem(event.getRawSlot())), player, GuideGUIType.FROM, 0);
                } else if (event.getClick().isLeftClick()) {
                    GuideIndexManager.openItemGuide(Registries.ITEM.getRegistryId(holder.getInventory().getItem(event.getRawSlot())), player, GuideGUIType.TO, 0);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof GuideGUIHolder) {
            event.setCancelled(true);
        }
    }
}
