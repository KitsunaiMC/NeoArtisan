package io.github.moyusowo.neoartisan.test;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.item.*;
import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.FoodProperties;
import io.papermc.paper.datacomponent.item.ItemLore;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
final class ItemTest {

    static final String namespace = "neoartisan";
    static final NamespacedKey broken_stick = new NamespacedKey(namespace, "broken_stick"),
            magic_bread = new NamespacedKey(namespace, "magic_bread"),
            magic_diamond = new NamespacedKey(namespace, "magic_diamond"),
            magic_helmet = new NamespacedKey(namespace, "magic_helmet"),
            magic_sword = new NamespacedKey(namespace, "magic_sword"),
            cooking_pot = new NamespacedKey(namespace, "cooking_pot"),
            cutting_board = new NamespacedKey(namespace, "cutting_board");

    @NeoArtisanAPI.Register
    private static void register() {
        if (NeoArtisan.isDebugMode()) {
            NeoArtisanAPI.getItemRegistry().registerItem(
                    ArtisanItem.complexBuilder()
                            .registryId(broken_stick)
                            .itemStack(() -> {
                                ItemStack itemStack = ItemStack.of(Material.STICK);
                                itemStack.setData(DataComponentTypes.ITEM_NAME, Component.text("有点损坏的木棍"));
                                ItemLore itemLore = ItemLore.lore(
                                        List.of(
                                                Component.text("是一根好像快坏掉了的木棍"),
                                                Component.text("但是似乎还能用")
                                        )
                                );
                                itemStack.setData(DataComponentTypes.LORE, itemLore);
                                return itemStack;
                            })
                            .hasOriginalCraft()
                            .blockId(new NamespacedKey(NeoArtisan.instance(), "magic_crop"))
                            .build()
            );
            NeoArtisanAPI.getItemRegistry().registerItem(
                    ArtisanItem.complexBuilder()
                            .registryId(magic_bread)
                            .itemStack(() -> {
                                ItemStack itemStack = ItemStack.of(Material.BREAD);
                                itemStack.setData(DataComponentTypes.ITEM_NAME, Component.text("<red>魔法面包~"));
                                ItemLore itemLore = ItemLore.lore(
                                        List.of(
                                                Component.text("魔法面包好"),
                                                Component.text("魔法面包妙"),
                                                Component.text("魔法面包生存少不了")
                                        )
                                );
                                itemStack.setData(DataComponentTypes.LORE, itemLore);
                                FoodProperties foodProperties = FoodProperties.food().saturation(10).nutrition(10).canAlwaysEat(true).build();
                                itemStack.setData(DataComponentTypes.FOOD, foodProperties);
                                return itemStack;
                            })
                            .build()
            );
            NeoArtisanAPI.getItemRegistry().registerItem(
                    ArtisanItem.builder()
                            .registryId(magic_helmet)
                            .rawMaterial(Material.IRON_HELMET)
                            .displayName("<aqua>魔法头盔~")
                            .lore(
                                    List.of(
                                            "魔法头盔一顶",
                                            "可以帮助你挡住下落的蜘（ji）蛛（ju）"
                                    )
                            )
                            .armorProperty(
                                    new ArmorProperty(
                                            5,
                                            1,
                                            null
                                    )
                            )
                            .maxDurability(2500)
                            .build()
            );
            NeoArtisanAPI.getItemRegistry().registerItem(
                    ArtisanItem.builder()
                            .registryId(magic_sword)
                            .rawMaterial(Material.IRON_SWORD)
                            .displayName("<yellow>魔法剑~")
                            .lore(
                                    List.of(
                                            "魔法剑一把",
                                            "可以帮助你更快地杀怪"
                                    )
                            )
                            .weaponProperty(
                                    new WeaponProperty(
                                            1.0f,
                                            1.5f,
                                            11.0f
                                    )
                            )
                            .maxDurability(5000)
                            .build()
            );
            NeoArtisanAPI.getItemRegistry().registerItem(
                    ArtisanItem.builder()
                            .registryId(cooking_pot)
                            .rawMaterial(Material.PAPER)
                            .displayName("烹饪锅")
                            .blockId(cooking_pot)
                            .itemModel(cooking_pot)
                            .build()
            );
            NeoArtisanAPI.getItemRegistry().registerItem(
                    ArtisanItem.builder()
                            .registryId(cutting_board)
                            .rawMaterial(Material.PAPER)
                            .displayName("砧板")
                            .blockId(cutting_board)
                            .itemModel(cutting_board)
                            .build()
            );
        }
    }
}
