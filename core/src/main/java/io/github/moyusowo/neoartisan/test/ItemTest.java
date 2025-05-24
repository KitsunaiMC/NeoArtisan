package io.github.moyusowo.neoartisan.test;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.item.*;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.checkerframework.checker.units.qual.N;

import java.util.List;

final class ItemTest {

    static final String namespace = "neoartisan";
    static final NamespacedKey broken_stick = new NamespacedKey(namespace, "broken_stick"),
            magic_bread = new NamespacedKey(namespace, "magic_bread"),
            magic_diamond = new NamespacedKey(namespace, "magic_diamond"),
            magic_helmet = new NamespacedKey(namespace, "magic_helmet"),
            magic_sword = new NamespacedKey(namespace, "magic_sword"),
            cooking_pot = new NamespacedKey(namespace, "cooking_pot"),
            cutting_board = new NamespacedKey(namespace, "cutting_board");

    @InitMethod(order = InitPriority.LOW)
    private static void register() {
        if (NeoArtisan.isDebugMode()) {
            NeoArtisanAPI.getItemRegistry().registerItem(
                    NeoArtisanAPI.getItemRegistry().builder()
                            .registryId(broken_stick)
                            .rawMaterial(Material.STICK)
                            .hasOriginalCraft(true)
                            .displayName("有点损坏的木棍")
                            .lore(
                                    List.of(
                                            "是一根好像快坏掉了的木棍",
                                            "但是似乎还能用"
                                    )
                            )
                            .blockId(new NamespacedKey(NeoArtisan.instance(), "magic_crop"))
            );
            NeoArtisanAPI.getItemRegistry().registerItem(
                    NeoArtisanAPI.getItemRegistry().builder()
                            .registryId(magic_bread)
                            .rawMaterial(Material.BREAD)
                            .displayName("<red>魔法面包~")
                            .lore(
                                    List.of(
                                            "魔法面包好",
                                            "魔法面包妙",
                                            "魔法面包生存少不了"
                                    )
                            )
                            .foodProperty(
                                    new FoodProperty(10, 10f, true)
                            )
            );
            NeoArtisanAPI.getItemRegistry().registerItem(
                    NeoArtisanAPI.getItemRegistry().builder()
                            .registryId(magic_diamond)
                            .rawMaterial(Material.DIAMOND)
                            .displayName("<blue>魔法钻石~")
                            .lore(
                                    List.of(
                                            "被神赐福过的魔法钻石",
                                            "可以合成更多的钻石噢"
                                    )
                            )
            );
            NeoArtisanAPI.getItemRegistry().registerItem(
                    NeoArtisanAPI.getItemRegistry().builder()
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
            );
            NeoArtisanAPI.getItemRegistry().registerItem(
                    NeoArtisanAPI.getItemRegistry().builder()
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
            );
            NeoArtisanAPI.getItemRegistry().registerItem(
                    NeoArtisanAPI.getItemRegistry().builder()
                            .registryId(cooking_pot)
                            .rawMaterial(Material.PAPER)
                            .displayName("烹饪锅")
                            .blockId(cooking_pot)
                            .itemModel(cooking_pot)
            );
            NeoArtisanAPI.getItemRegistry().registerItem(
                    NeoArtisanAPI.getItemRegistry().builder()
                            .registryId(cutting_board)
                            .rawMaterial(Material.PAPER)
                            .displayName("砧板")
                            .blockId(cutting_board)
                            .itemModel(cutting_board)
            );
        }
    }
}
