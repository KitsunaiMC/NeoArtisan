package io.github.moyusowo.neoartisan.recipe.guide.category;

import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public final class GuideCategoryHolder implements InventoryHolder {
    private final Inventory inventory;
    public final int page;
    public static final int nextPageSlot = 53, lastPageSlot = 45, returnSlot = 0;
    private static final ItemStack LAST_PAGE, NEXT_PAGE, RETURN;

    static {
        LAST_PAGE = ItemStack.of(Material.ARROW);
        NEXT_PAGE = ItemStack.of(Material.ARROW);
        RETURN = ItemStack.of(Material.BARRIER);
        LAST_PAGE.setData(DataComponentTypes.ITEM_NAME, Component.text("上一页"));
        NEXT_PAGE.setData(DataComponentTypes.ITEM_NAME, Component.text("下一页"));
        RETURN.setData(DataComponentTypes.ITEM_NAME, Component.text("返回").color(TextColor.color(255, 0, 0)));
    }

    public GuideCategoryHolder(int page, final List<ItemStack> items) {
        this.page = page;
        this.inventory = Bukkit.createInventory(this, 54, Component.text("物品大全"));
        int slot = 9;
        for (ItemStack itemStack : items) {
            if (slot >= 45) {
                throw new IllegalArgumentException("too large list!");
            }
            inventory.setItem(slot, itemStack);
            slot++;
        }
        inventory.setItem(lastPageSlot, LAST_PAGE);
        inventory.setItem(returnSlot, RETURN);
        inventory.setItem(nextPageSlot, NEXT_PAGE);
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
