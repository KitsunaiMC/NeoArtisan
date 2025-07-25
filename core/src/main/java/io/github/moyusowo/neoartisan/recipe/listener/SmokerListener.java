package io.github.moyusowo.neoartisan.recipe.listener;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanSmokingRecipe;
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

final class SmokerListener implements Listener {
    private SmokerListener() {}

    @InitMethod(priority = InitPriority.LISTENER)
    static void registerListener() {
        NeoArtisan.registerListener(new SmokerListener());
    }

    private static boolean hasRecipe(ItemStack itemStack) {
        for (Recipe recipe : Bukkit.getRecipesFor(itemStack)) {
            if (recipe instanceof SmokingRecipe smokingRecipe) {
                if (smokingRecipe.getInputChoice() instanceof RecipeChoice.ExactChoice exactChoice) {
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
        if (furnaceInventory.getHolder().getBlock().getType() != Material.SMOKER) return;
        if (event.getSlotType() != InventoryType.SlotType.CRAFTING) return;
        final ItemStack itemStack = event.getCursor();
        if (itemStack.isEmpty()) return;
        if (!hasRecipe(itemStack)) {
            final Collection<ArtisanRecipe> smokingRecipes = Registries.RECIPE.getRecipes(RecipeType.SMOKING);
            for (ArtisanRecipe artisanRecipe : smokingRecipes) {
                if (artisanRecipe instanceof ArtisanSmokingRecipe smokingRecipe) {
                    if (smokingRecipe.matches(new ItemStack[] { itemStack })) {
                        FurnaceRecipe recipe = new FurnaceRecipe(
                                new NamespacedKey(smokingRecipe.getKey().namespace(), smokingRecipe.getKey().getKey() + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().toLowerCase()),
                                smokingRecipe.getResultGenerator().generate(),
                                new RecipeChoice.ExactChoice(itemStack),
                                smokingRecipe.getExp(),
                                smokingRecipe.getCookTime()
                        );
                        Bukkit.addRecipe(recipe);
                        return;
                    }
                }
            }
        }
    }
}
