package io.github.moyusowo.neoartisan.recipe;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.ArrayKey;
import io.github.moyusowo.neoartisanapi.api.item.ItemRegistry;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Map;

final class CraftingTableBehavior implements Listener {

    private CraftingTableBehavior() {}

    static void registerListener() {
        NeoArtisan.registerListener(new CraftingTableBehavior());
    }

    @EventHandler
    private void onPrepareItemCraft(PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();
        ItemStack[] matrix = inventory.getMatrix();
        if (matrix.length == 4) return;
        if (event.getRecipe() != null) {
            for (int i = 0; i < 9; i++) {
                if (matrix[i] == null) continue;
                NamespacedKey registryId = ItemRegistry.getItemRegistryManager().getRegistryId(matrix[i]);
                if (ItemRegistry.getItemRegistryManager().isArtisanItem(registryId) && (!ItemRegistry.getItemRegistryManager().getArtisanItem(registryId).hasOriginalCraft())) {
                    event.getInventory().setResult(null);
                    break;
                } else {
                    return;
                }
            }
        }
        ArrayKey shapedRegistryKey = ArtisanShapedRecipeImpl.toRegistryKey(matrix);
        if (RecipeRegistryImpl.getInstance().shapedRegistry.containsKey(shapedRegistryKey)) {
            ArtisanShapedRecipeImpl r = (ArtisanShapedRecipeImpl) RecipeRegistryImpl.getInstance().shapedRegistry.get(shapedRegistryKey);
            event.getInventory().setResult(ItemRegistry.getItemRegistryManager().getItemStack(r.getResult(), r.getCount()));
        } else {
            ArrayKey shapelessRegistryKey = ArtisanShapelessRecipeImpl.toRegistryKey(matrix);
            if (RecipeRegistryImpl.getInstance().shapelessRegistry.containsKey(shapelessRegistryKey)) {
                ArtisanShapelessRecipeImpl r = (ArtisanShapelessRecipeImpl) RecipeRegistryImpl.getInstance().shapelessRegistry.get(shapelessRegistryKey);
                event.getInventory().setResult(ItemRegistry.getItemRegistryManager().getItemStack(r.getResult(), r.getCount()));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    private void onItemCraft(InventoryClickEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getClickedInventory() instanceof CraftingInventory inventory)) return;
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (event.getSlotType() == InventoryType.SlotType.RESULT) {
            if (inventory.getRecipe() != null || inventory.getResult() == null) return;
            event.setCancelled(true);
            if (event.isShiftClick()) {
                ItemStack[] matrix = inventory.getMatrix();
                int minAmount = Integer.MAX_VALUE;
                for (ItemStack itemStack : matrix) {
                    if (itemStack != null) {
                        minAmount = Math.min(minAmount, itemStack.getAmount());
                    }
                }
                for (int i = 0; i < minAmount; i++) {
                    Map<Integer, ItemStack> leftovers = player.getInventory().addItem(inventory.getResult().clone());
                    if (!leftovers.isEmpty()) {
                        ItemStack reduce = inventory.getResult().clone();
                        reduce.setAmount(reduce.getAmount() - leftovers.get(0).getAmount());
                        player.getInventory().removeItem(reduce);
                        break;
                    } else {
                        for (ItemStack itemStack : matrix) {
                            if (itemStack != null) {
                                itemStack.setAmount(itemStack.getAmount() - 1);
                            }
                        }
                    }
                }
                inventory.setMatrix(matrix);
            } else if (event.getCursor().getType() != Material.AIR && event.getCursor().isSimilar(inventory.getResult()) && event.getCursor().getAmount() + inventory.getResult().getAmount() < event.getCursor().getMaxStackSize()) {
                ItemStack[] matrix = inventory.getMatrix();
                for (ItemStack itemStack : matrix) {
                    if (itemStack != null) {
                        itemStack.setAmount(itemStack.getAmount() - 1);
                    }
                }
                event.getCursor().setAmount(event.getCursor().getAmount() + inventory.getResult().getAmount());
                inventory.setMatrix(matrix);
            } else if (event.getCursor().getType() == Material.AIR) {
                ItemStack[] matrix = inventory.getMatrix();
                for (ItemStack itemStack : matrix) {
                    if (itemStack != null) {
                        itemStack.setAmount(itemStack.getAmount() - 1);
                    }
                }
                player.setItemOnCursor(inventory.getResult().clone());
                inventory.setMatrix(matrix);
            }
            player.updateInventory();
        }
    }

}
