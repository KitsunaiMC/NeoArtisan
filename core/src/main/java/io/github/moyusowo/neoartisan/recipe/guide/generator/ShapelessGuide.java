package io.github.moyusowo.neoartisan.recipe.guide.generator;

import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.ArtisanShapelessRecipe;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.Choice;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.ItemChoice;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.MultiChoice;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.TagChoice;
import io.github.moyusowo.neoartisanapi.api.recipe.guide.GuideGUIGenerator;
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
public final class ShapelessGuide implements GuideGUIGenerator {
    private static final ItemStack ICON;
    private static final Button LAST_PAGE, NEXT_PAGE, RETURN;

    static {
        final ItemStack last = ItemStack.of(Material.ARROW), next = ItemStack.of(Material.ARROW), ret = ItemStack.of(Material.BARRIER);
        last.setData(DataComponentTypes.ITEM_NAME, Component.text("上一页"));
        next.setData(DataComponentTypes.ITEM_NAME, Component.text("下一页"));
        ret.setData(DataComponentTypes.ITEM_NAME, Component.text("返回").color(TextColor.color(255, 0, 0)));
        LAST_PAGE = new Button(last, 36);
        NEXT_PAGE = new Button(next, 44);
        RETURN = new Button(ret, 0);
    }

    static {
        ICON = ItemStack.of(Material.CRAFTING_TABLE);
        ICON.setData(DataComponentTypes.ITEM_NAME, MiniMessage.miniMessage().deserialize("工作台：无序合成"));
    }

    @Override
    public @NotNull Button getLastPageButton() {
        return LAST_PAGE;
    }

    @Override
    public @NotNull Button getNextPageButton() {
        return NEXT_PAGE;
    }

    @Override
    public @NotNull Button getReturnButton() {
        return RETURN;
    }

    @Override
    public @NotNull Inventory recipeToInventory(@NotNull ArtisanRecipe recipe, @NotNull InventoryHolder holder, @NotNull Component title) {
        final ArtisanShapelessRecipe shapelessRecipe = (ArtisanShapelessRecipe) recipe;
        final Inventory inventory = Bukkit.createInventory(holder, 45, title);
        final int[] slots = new int[] {
                12, 13, 14,
                21, 22, 23,
                30, 31, 32
        };
        inventory.setItem(19, ICON);
        final List<Choice> inputs = shapelessRecipe.getInputs();
        for (int i = 0; i < inputs.size(); i++) {
            final List<NamespacedKey> input = new ArrayList<>();
            final List<Choice> handle = new ArrayList<>();
            if (inputs.get(i) == Choice.EMPTY) continue;
            handle.add(inputs.get(i));
            while (!handle.isEmpty()) {
                if (handle.getFirst() instanceof ItemChoice itemChoice) {
                    input.add(itemChoice.itemId());
                } else if (handle.getFirst() instanceof TagChoice tagChoice) {
                    input.addAll(Registries.ITEM.getIdByTag(tagChoice.tag()));
                } else if (handle.getFirst() instanceof MultiChoice multiChoice) {
                    handle.addAll(multiChoice.getChoices());
                } else {
                    throw new IllegalArgumentException("can not handle this kinds of choice!");
                }
                handle.removeFirst();
            }
            final ItemStack itemStack = Registries.ITEM.getItemStack(input.getFirst());
            final ItemLore.Builder itemLore = ItemLore.lore().addLine(MiniMessage.miniMessage().deserialize("<white>可以接受的物品<dark_gray>：</dark_gray></white>").decoration(TextDecoration.ITALIC, false));
            if (input.size() > 1) itemStack.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
            while (!input.isEmpty()) {
                itemLore.addLine(MiniMessage.miniMessage().deserialize(" <gray>- </gray>").append(Registries.ITEM.getItemStack(input.getFirst()).displayName().decoration(TextDecoration.ITALIC, false)).decoration(TextDecoration.ITALIC, false));
                input.removeFirst();
            }
            itemStack.setData(DataComponentTypes.LORE, itemLore);
            inventory.setItem(slots[i], itemStack);
        }
        inventory.setItem(25, shapelessRecipe.getResultGenerator().generate());
        return inventory;
    }
}
