package io.github.moyusowo.neoartisan.util;

import io.github.moyusowo.neoartisan.NeoArtisan;
import org.jetbrains.annotations.NotNull;

import java.io.File;

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
