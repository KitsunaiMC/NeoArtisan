package io.github.moyusowo.neoartisan.attribute;

import io.github.moyusowo.neoartisan.NeoArtisan;
import org.jetbrains.annotations.NotNull;

import java.io.File;

import static io.github.moyusowo.neoartisan.util.Util.saveDefaultIfNotExists;

final class ReadUtil {

    private ReadUtil() {}

    public static File readAttributeFiles() {
        File dataFolder = NeoArtisan.instance().getDataFolder();
        File recipeFolder = new File(dataFolder, "attribute");
        if (!recipeFolder.exists()) {
            recipeFolder.mkdirs();
            saveDefaultIfNotExists("attribute/global_attribute.yml");
            saveDefaultIfNotExists("attribute/itemstack_attribute.yml");
            saveDefaultIfNotExists("attribute/player_attribute.yml");
        }
        return recipeFolder;
    }

    public static boolean isYmlFile(@NotNull File file) {
        if (!file.isFile()) return false;
        String name = file.getName().toLowerCase();
        return name.endsWith(".yml") || name.endsWith(".yaml");
    }

}
