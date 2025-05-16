package io.github.moyusowo.neoartisan.test;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.NeoArtisanAPI;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataType;

import static io.github.moyusowo.neoartisan.test.ItemTest.namespace;

final class AttributeTest {

    static final NamespacedKey global = new NamespacedKey(namespace, "global"),
                    itemstack = new NamespacedKey(namespace, "itemstack"),
                    player = new NamespacedKey(namespace, "player");

    @InitMethod(order = InitPriority.LOW)
    private static void register() {
        if (NeoArtisan.isDebugMode()) {
            NeoArtisanAPI.getGlobalAttributeRegistry().registerAttribute(
                    global,
                    PersistentDataType.DOUBLE
            );
            NeoArtisanAPI.getItemStackAttributeRegistry().registerAttribute(
                    itemstack,
                    PersistentDataType.INTEGER
            );
            NeoArtisanAPI.getPlayerAttributeRegistry().registerAttribute(
                    player,
                    PersistentDataType.FLOAT
            );
        }
    }
}
