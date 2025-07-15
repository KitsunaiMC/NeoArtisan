package io.github.moyusowo.neoartisan.recipe.behavior;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.recipe.internal.RecipeRegistryInternal;
import io.github.moyusowo.neoartisan.util.ArrayKey;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanCampfireRecipe;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CampfireRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;

import java.util.UUID;

final class CampfireBehavior implements Listener {
    private CampfireBehavior() {}

    @InitMethod(priority = InitPriority.LISTENER)
    static void registerListener() {
        NeoArtisan.registerListener(new CampfireBehavior());
    }

    private static boolean hasRecipe(ItemStack itemStack) {
        for (Recipe recipe : Bukkit.getRecipesFor(itemStack)) {
            if (recipe instanceof CampfireRecipe campfireRecipe) {
                if (campfireRecipe.getInputChoice() instanceof RecipeChoice.ExactChoice exactChoice) {
                    if (exactChoice.test(itemStack)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @EventHandler(ignoreCancelled = true)
    public void onClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        assert event.getClickedBlock() != null;
        if (event.getClickedBlock().getType() != Material.CAMPFIRE) return;
        final ItemStack itemStack = event.getItem();
        if (itemStack == null || itemStack.isEmpty()) return;
        if (!hasRecipe(itemStack)) {
            ArrayKey campfireKey = ArrayKeyUtil.toCampfireKey(itemStack);
            RecipeRegistryInternal registryInternal = (RecipeRegistryInternal) NeoArtisanAPI.getRecipeRegistry();
            if (registryInternal.has(campfireKey) && registryInternal.get(campfireKey) instanceof ArtisanCampfireRecipe r) {
                CampfireRecipe recipe = new CampfireRecipe(
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
