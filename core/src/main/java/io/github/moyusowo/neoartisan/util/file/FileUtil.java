package io.github.moyusowo.neoartisan.util.file;

import io.github.moyusowo.neoartisan.NeoArtisan;

import java.io.File;

@SuppressWarnings("unused")
public final class FileUtil {

    private FileUtil() {}

    public static void saveDefaultIfNotExists(String resourcePath) {
        String targetPath = resourcePath.replace('/', File.separatorChar);
        File targetFile = new File(NeoArtisan.instance().getDataFolder(), targetPath);
        if (targetFile.exists()) {
            return;
        }
        NeoArtisan.instance().saveResource(resourcePath, false);
    }

}
