package io.github.moyusowo.neoartisan.guide.listener;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.guide.category.GuideCategoryHolder;
import io.github.moyusowo.neoartisan.guide.index.GuideIndexHolder;
import io.github.moyusowo.neoartisan.guide.index.GuideIndexManager;
import io.github.moyusowo.neoartisan.guide.item.GuideGUIHolder;
import io.github.moyusowo.neoartisan.guide.item.GuideGUIManager;
import io.github.moyusowo.neoartisan.guide.item.GuideGUIType;
import io.github.moyusowo.neoartisan.registry.internal.GuideRegistryInternal;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.registry.Registries;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public final class GuideListener implements Listener {
    private GuideListener() {}

    @InitMethod(priority = InitPriority.LISTENER)
    static void init() {
        NeoArtisan.registerListener(new GuideListener());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        if (event.useItemInHand() == Event.Result.DENY) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() == null) return;
        Optional<String> bookId = GuideRegistryInternal.getInstance().getBookPDC(event.getItem());
        bookId.ifPresent(s -> GuideRegistryInternal.getInstance().openGuideBook(s, event.getPlayer()));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getInventory().getHolder() instanceof GuideGUIHolder holder) {
            event.setCancelled(true);
            if (event.getRawSlot() == holder.nextPageSlot) {
                GuideGUIManager.openItemGuide(holder.itemId, player, holder.type, holder.page + 1);
            } else if (event.getRawSlot() == holder.lastPageSlot) {
                GuideGUIManager.openItemGuide(holder.itemId, player, holder.type, holder.page - 1);
            } else if (event.getRawSlot() == holder.returnPageSlot) {
                GuideIndexManager.openIndexGuideByItem(player, holder.itemId, 0);
            } else if (event.getClickedInventory() == holder.getInventory()) {
                final ItemStack itemStack = holder.getInventory().getItem(event.getRawSlot());
                if (itemStack != null && !itemStack.isEmpty()) {
                    if (event.getClick().isRightClick()) {
                        GuideGUIManager.openItemGuide(Registries.ITEM.getRegistryId(holder.getInventory().getItem(event.getRawSlot())), player, GuideGUIType.FROM, 0);
                    } else if (event.getClick().isLeftClick()) {
                        GuideGUIManager.openItemGuide(Registries.ITEM.getRegistryId(holder.getInventory().getItem(event.getRawSlot())), player, GuideGUIType.TO, 0);
                    }
                }
            }
        } else if (event.getInventory().getHolder() instanceof GuideIndexHolder holder) {
            event.setCancelled(true);
            if (event.getRawSlot() == GuideIndexHolder.nextPageSlot) {
                GuideIndexManager.openIndexGuideByCategory(player, holder.category, holder.page + 1);
            } else if (event.getRawSlot() == GuideIndexHolder.lastPageSlot) {
                GuideIndexManager.openIndexGuideByCategory(player, holder.category, holder.page - 1);
            } else if (event.getRawSlot() == GuideIndexHolder.returnSlot) {
                GuideRegistryInternal.getInstance().returnToGuideBook(player);
            } else if (event.getClickedInventory() == holder.getInventory()) {
                final ItemStack itemStack = holder.getInventory().getItem(event.getRawSlot());
                if (itemStack != null && !itemStack.isEmpty()) {
                    if (event.getClick().isRightClick()) {
                        GuideGUIManager.openItemGuide(Registries.ITEM.getRegistryId(holder.getInventory().getItem(event.getRawSlot())), player, GuideGUIType.FROM, 0);
                    } else if (event.getClick().isLeftClick()) {
                        GuideGUIManager.openItemGuide(Registries.ITEM.getRegistryId(holder.getInventory().getItem(event.getRawSlot())), player, GuideGUIType.TO, 0);
                    }
                }
            }
        } else if (event.getInventory().getHolder() instanceof GuideCategoryHolder holder) {
            event.setCancelled(true);
            if (event.getRawSlot() == GuideCategoryHolder.nextPageSlot) {
                GuideRegistryInternal.getInstance().returnToGuideBook(player);
            } else if (event.getRawSlot() == GuideCategoryHolder.lastPageSlot) {
                GuideRegistryInternal.getInstance().returnToGuideBook(player);
            } else if (event.getRawSlot() == GuideCategoryHolder.returnSlot) {
                player.closeInventory();
            } else if (event.getClickedInventory() == holder.getInventory()) {
                final ItemStack itemStack = holder.getInventory().getItem(event.getRawSlot());
                if (itemStack != null && !itemStack.isEmpty()) {
                    GuideIndexManager.openIndexGuideByCategoryItemStack(player, itemStack, 0);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDrag(InventoryDragEvent event) {
        if (event.getInventory().getHolder() instanceof GuideGUIHolder || event.getInventory().getHolder() instanceof GuideCategoryHolder || event.getInventory().getHolder() instanceof GuideIndexHolder) {
            event.setCancelled(true);
        }
    }
}
