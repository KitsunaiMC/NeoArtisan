package io.github.moyusowo.neoartisan.recipe.guide.index;

import io.github.moyusowo.neoartisanapi.api.registry.Registries;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class GuideIndexHolder implements InventoryHolder {
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

    public GuideIndexHolder(int page, final List<NamespacedKey> items) {
        this.page = page;
        this.inventory = Bukkit.createInventory(this, 54, Component.text("物品大全"));
        int slot = 9;
        for (NamespacedKey item : items) {
            if (slot >= 45) {
                throw new IllegalArgumentException("too large list!");
            }
            final ItemStack itemStack = Registries.ITEM.getItemStack(item);
            ItemLore itemLore = itemStack.getDataOrDefault(DataComponentTypes.LORE, ItemLore.lore().build());
            List<Component> components = new ArrayList<>(itemLore.lines());
            components.add(Component.empty());
            components.add(MiniMessage.miniMessage().deserialize("<white><aqua>右键</aqua>查看作为<green>原料</green>的配方</white>").decoration(TextDecoration.ITALIC, false));
            components.add(MiniMessage.miniMessage().deserialize("<white><aqua>左键</aqua>查看作为<green>产物</green>的配方</white>").decoration(TextDecoration.ITALIC, false));
            components.add(Component.empty());
            components.add(Component.text("ID: ").append(Component.text(item.asString())).decoration(TextDecoration.ITALIC, false).color(TextColor.fromHexString("#555555")));
            itemLore = ItemLore.lore(components);
            itemStack.setData(DataComponentTypes.LORE, itemLore);
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
