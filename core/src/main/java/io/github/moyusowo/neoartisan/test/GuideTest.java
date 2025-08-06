package io.github.moyusowo.neoartisan.test;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisanapi.api.recipe.guide.ItemCategories;
import io.github.moyusowo.neoartisanapi.api.registry.Registries;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Set;

public final class GuideTest {
    private GuideTest() {}

    @SuppressWarnings("UnstableApiUsage")
    public static void register() {
        if (NeoArtisan.isDebugMode()) {
            Registries.GUIDE.registerGuideBook(
                    NeoArtisan.instance(),
                    () -> {
                        final ItemStack itemStack = new ItemStack(Material.BOOK);
                        itemStack.setData(DataComponentTypes.ITEM_NAME, MiniMessage.miniMessage().deserialize("<yellow>配方书</yellow>"));
                        itemStack.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
                        return itemStack;
                    },
                    Set.of(
                            ItemCategories.COMBAT,
                            ItemCategories.FOOD,
                            ItemCategories.MISC,
                            ItemCategories.TOOLS,
                            ItemCategories.DECORATIONS,
                            ItemCategories.ORIGINAL
                    )
            );
        }
    }
}
