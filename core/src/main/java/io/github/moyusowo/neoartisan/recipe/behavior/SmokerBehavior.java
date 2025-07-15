package io.github.moyusowo.neoartisan.recipe.behavior;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.recipe.internal.RecipeRegistryInternal;
import io.github.moyusowo.neoartisan.util.ArrayKey;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanSmokingRecipe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;

import java.util.UUID;

final class SmokerBehavior implements Listener {
    private SmokerBehavior() {}

    @InitMethod(priority = InitPriority.LISTENER)
    static void registerListener() {
        NeoArtisan.registerListener(new SmokerBehavior());
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
            ArrayKey smokerKey = ArrayKeyUtil.toSmokerKey(itemStack);
            RecipeRegistryInternal registryInternal = (RecipeRegistryInternal) NeoArtisanAPI.getRecipeRegistry();
            if (registryInternal.has(smokerKey) && registryInternal.get(smokerKey) instanceof ArtisanSmokingRecipe r) {
                SmokingRecipe recipe = new SmokingRecipe(
                        new NamespacedKey(r.getKey().namespace(), r.getKey().getKey() + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().toLowerCase()),
                        r.getResultGenerator().generate(),
                        new RecipeChoice.ExactChoice(itemStack),
                        r.getExp(),
                        r.getCookTime()
                );
                Bukkit.addRecipe(recipe);
            }
        }
    }
}
