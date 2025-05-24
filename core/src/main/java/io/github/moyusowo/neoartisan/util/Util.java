package io.github.moyusowo.neoartisan.util;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Collection;

@SuppressWarnings("unused")
public final class Util {

    private Util() {}

    public static void saveDefaultIfNotExists(String resourcePath) {
        String targetPath = resourcePath.replace('/', File.separatorChar);
        File targetFile = new File(NeoArtisan.instance().getDataFolder(), targetPath);
        if (targetFile.exists()) {
            return;
        }
        NeoArtisan.instance().saveResource(resourcePath, false);
    }

    public static boolean isYmlFile(@NotNull File file) {
        if (!file.isFile()) return false;
        String name = file.getName().toLowerCase();
        return name.endsWith(".yml") || name.endsWith(".yaml");
    }

}
