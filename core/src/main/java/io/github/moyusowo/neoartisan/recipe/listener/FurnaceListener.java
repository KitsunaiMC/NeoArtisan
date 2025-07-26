package io.github.moyusowo.neoartisan.recipe.listener;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanFurnaceRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.RecipeType;
import io.github.moyusowo.neoartisanapi.api.registry.Registries;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;

import java.util.Collection;
import java.util.UUID;

final class FurnaceListener implements Listener {

    private FurnaceListener() {}

    @InitMethod(priority = InitPriority.LISTENER)
    static void registerListener() {
        NeoArtisan.registerListener(new FurnaceListener());
    }

    private static boolean hasRecipe(ItemStack itemStack) {
        for (Recipe recipe : Bukkit.getRecipesFor(itemStack)) {
            if (recipe instanceof FurnaceRecipe furnaceRecipe) {
                if (furnaceRecipe.getInputChoice() instanceof RecipeChoice.ExactChoice exactChoice) {
                    if (exactChoice.test(itemStack)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.isCancelled()) return;
        if (!(event.getClickedInventory() instanceof FurnaceInventory furnaceInventory)) return;
        if (furnaceInventory.getHolder() == null) return;
        if (furnaceInventory.getHolder().getBlock().getType() != Material.FURNACE) return;
        if (event.getSlotType() != InventoryType.SlotType.CRAFTING) return;
        final ItemStack itemStack = event.getCursor();
        if (itemStack.isEmpty()) return;
        if (!hasRecipe(itemStack)) {
            final Collection<ArtisanRecipe> furnaceRecipes = Registries.RECIPE.getRecipesByType(RecipeType.FURNACE);
            for (ArtisanRecipe artisanRecipe : furnaceRecipes) {
                if (artisanRecipe instanceof ArtisanFurnaceRecipe furnaceRecipe) {
                    if (furnaceRecipe.matches(new ItemStack[] { itemStack })) {
                        FurnaceRecipe recipe = new FurnaceRecipe(
                                new NamespacedKey(furnaceRecipe.getKey().namespace(), furnaceRecipe.getKey().getKey() + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().toLowerCase()),
                                furnaceRecipe.getResultGenerator().generate(),
                                new RecipeChoice.ExactChoice(itemStack),
                                furnaceRecipe.getExp(),
                                furnaceRecipe.getCookTime()
                        );
                        Bukkit.addRecipe(recipe);
                        return;
                    }
                }
            }
        }
    }
}
