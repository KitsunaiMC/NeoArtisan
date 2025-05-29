package io.github.moyusowo.neoartisan.recipe;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.block.CraftFurnace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.*;

import java.security.Timestamp;
import java.util.Timer;
import java.util.UUID;

final class FurnaceBehavior implements Listener {

    private FurnaceBehavior() {}

    @InitMethod(priority = InitPriority.LISTENER)
    static void registerListener() {
        NeoArtisan.registerListener(new FurnaceBehavior());
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
        if (!(event.getClickedInventory() instanceof FurnaceInventory)) return;
        if (event.getSlotType() != InventoryType.SlotType.CRAFTING) return;
        final ItemStack itemStack = event.getCursor();
        if (itemStack.isEmpty()) return;
        if (!hasRecipe(itemStack)) {
            NamespacedKey registryId = NeoArtisanAPI.getItemRegistry().getRegistryId(itemStack);
            System.out.println(registryId.asString());
            if (RecipeRegistryImpl.getInstance().furnaceRegistry.containsKey(registryId)) {
                System.out.println("contains");
                ArtisanFurnaceRecipeImpl artisanFurnaceRecipe = (ArtisanFurnaceRecipeImpl) RecipeRegistryImpl.getInstance().furnaceRegistry.get(registryId);
                FurnaceRecipe r = new FurnaceRecipe(
                        new NamespacedKey(registryId.namespace(), registryId.getKey() + "_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().toLowerCase()),
                        NeoArtisanAPI.getItemRegistry().getItemStack(artisanFurnaceRecipe.getResult()),
                        new RecipeChoice.ExactChoice(itemStack),
                        artisanFurnaceRecipe.getExp(),
                        artisanFurnaceRecipe.getCookTime()
                );
                Bukkit.addRecipe(r);
            }
        }
    }
}
