package io.github.moyusowo.neoartisan.recipe.listener;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanShapedRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanShapelessRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.RecipeType;
import io.github.moyusowo.neoartisanapi.api.registry.Registries;
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

import java.util.Collection;
import java.util.Map;

final class CraftingTableListener implements Listener {

    private CraftingTableListener() {}

    @InitMethod(priority = InitPriority.LISTENER)
    static void registerListener() {
        NeoArtisan.registerListener(new CraftingTableListener());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPrepareItemCraft(PrepareItemCraftEvent event) {
        CraftingInventory inventory = event.getInventory();
        ItemStack[] matrix = inventory.getMatrix();
        if (matrix.length == 4) {
            for (int i = 0; i < 4; i++) {
                if (matrix[i] == null) continue;
                NamespacedKey registryId = Registries.ITEM.getRegistryId(matrix[i]);
                if (Registries.ITEM.isArtisanItem(registryId) && (!Registries.ITEM.getArtisanItem(registryId).hasOriginalCraft())) {
                    event.getInventory().setResult(null);
                    break;
                }
            }
            return;
        }
        if (event.getRecipe() != null) {
            boolean hasArtisan = false;
            for (int i = 0; i < 9; i++) {
                if (matrix[i] == null) continue;
                NamespacedKey registryId = Registries.ITEM.getRegistryId(matrix[i]);
                if (Registries.ITEM.isArtisanItem(registryId) && (!Registries.ITEM.getArtisanItem(registryId).hasOriginalCraft())) {
                    event.getInventory().setResult(null);
                    hasArtisan = true;
                    break;
                }
            }
            if (!hasArtisan) return;
        }
        final Collection<ArtisanRecipe> shapedRecipes = Registries.RECIPE.getRecipesByType(RecipeType.SHAPED);
        for (ArtisanRecipe artisanRecipe : shapedRecipes) {
            if (artisanRecipe instanceof ArtisanShapedRecipe shapedRecipe) {
                if (shapedRecipe.matches(matrix)) {
                    event.getInventory().setResult(shapedRecipe.getResultGenerator().generate());
                    return;
                }
            }
        }
        final Collection<ArtisanRecipe> shapelessRecipes = Registries.RECIPE.getRecipesByType(RecipeType.SHAPELESS);
        for (ArtisanRecipe artisanRecipe : shapelessRecipes) {
            if (artisanRecipe instanceof ArtisanShapelessRecipe shapelessRecipe) {
                if (shapelessRecipe.matches(matrix)) {
                    event.getInventory().setResult(shapelessRecipe.getResultGenerator().generate());
                    return;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemCraft(InventoryClickEvent event) {
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
