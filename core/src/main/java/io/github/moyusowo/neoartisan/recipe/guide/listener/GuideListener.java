package io.github.moyusowo.neoartisan.recipe.guide.listener;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.recipe.guide.category.GuideCategoryHolder;
import io.github.moyusowo.neoartisan.recipe.guide.category.GuideCategoryManager;
import io.github.moyusowo.neoartisan.recipe.guide.index.GuideIndexHolder;
import io.github.moyusowo.neoartisan.recipe.guide.item.GuideGUIHolder;
import io.github.moyusowo.neoartisan.recipe.guide.item.GuideGUIType;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.registry.Registries;
import org.bukkit.NamespacedKey;
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

public final class GuideListener implements Listener {
    private GuideListener() {}

    public static final NamespacedKey RECIPE_BOOK = new NamespacedKey("neoartisan", "recipe_book");

    @InitMethod(priority = InitPriority.LISTENER)
    static void init() {
        NeoArtisan.registerListener(new GuideListener());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerInteractEvent event) {
        if (event.useItemInHand() == Event.Result.DENY) return;
        if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() == null || !Registries.ITEM.getRegistryId(event.getItem()).equals(RECIPE_BOOK)) return;
        GuideCategoryManager.openCategoryGuide(event.getPlayer(), 0);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getInventory().getHolder() instanceof GuideGUIHolder holder) {
            event.setCancelled(true);
            if (event.getRawSlot() == holder.nextPageSlot) {
                GuideCategoryManager.openItemGuide(holder.itemId, player, holder.type, holder.page + 1);
            } else if (event.getRawSlot() == holder.lastPageSlot) {
                GuideCategoryManager.openItemGuide(holder.itemId, player, holder.type, holder.page - 1);
            } else if (event.getRawSlot() == holder.returnPageSlot) {
                GuideCategoryManager.openIndexGuide(player, holder.itemId, 0, true);
            } else if (event.getClickedInventory() == holder.getInventory()) {
                final ItemStack itemStack = holder.getInventory().getItem(event.getRawSlot());
                if (itemStack != null && !itemStack.isEmpty()) {
                    if (event.getClick().isRightClick()) {
                        GuideCategoryManager.openItemGuide(Registries.ITEM.getRegistryId(holder.getInventory().getItem(event.getRawSlot())), player, GuideGUIType.FROM, 0);
                    } else if (event.getClick().isLeftClick()) {
                        GuideCategoryManager.openItemGuide(Registries.ITEM.getRegistryId(holder.getInventory().getItem(event.getRawSlot())), player, GuideGUIType.TO, 0);
                    }
                }
            }
        } else if (event.getInventory().getHolder() instanceof GuideIndexHolder holder) {
            event.setCancelled(true);
            if (event.getRawSlot() == GuideIndexHolder.nextPageSlot) {
                GuideCategoryManager.openIndexGuide(player, holder.category, holder.page + 1, false);
            } else if (event.getRawSlot() == GuideIndexHolder.lastPageSlot) {
                GuideCategoryManager.openIndexGuide(player, holder.category, holder.page - 1, false);
            } else if (event.getRawSlot() == GuideIndexHolder.returnSlot) {
                GuideCategoryManager.openCategoryGuide(player, 0);
            } else if (event.getClickedInventory() == holder.getInventory()) {
                final ItemStack itemStack = holder.getInventory().getItem(event.getRawSlot());
                if (itemStack != null && !itemStack.isEmpty()) {
                    if (event.getClick().isRightClick()) {
                        GuideCategoryManager.openItemGuide(Registries.ITEM.getRegistryId(holder.getInventory().getItem(event.getRawSlot())), player, GuideGUIType.FROM, 0);
                    } else if (event.getClick().isLeftClick()) {
                        GuideCategoryManager.openItemGuide(Registries.ITEM.getRegistryId(holder.getInventory().getItem(event.getRawSlot())), player, GuideGUIType.TO, 0);
                    }
                }
            }
        } else if (event.getInventory().getHolder() instanceof GuideCategoryHolder holder) {
            event.setCancelled(true);
            if (event.getRawSlot() == GuideCategoryHolder.nextPageSlot) {
                GuideCategoryManager.openCategoryGuide(player, holder.page + 1);
            } else if (event.getRawSlot() == GuideCategoryHolder.lastPageSlot) {
                GuideCategoryManager.openCategoryGuide(player, holder.page - 1);
            } else if (event.getRawSlot() == GuideCategoryHolder.returnSlot) {
                player.closeInventory();
            } else if (event.getClickedInventory() == holder.getInventory()) {
                final ItemStack itemStack = holder.getInventory().getItem(event.getRawSlot());
                if (itemStack != null && !itemStack.isEmpty()) {
                    GuideCategoryManager.openIndexGuide(player, event.getRawSlot() - 9, 0);
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
