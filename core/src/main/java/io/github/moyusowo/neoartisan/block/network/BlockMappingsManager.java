package io.github.moyusowo.neoartisan.block.network;

import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import org.bukkit.Bukkit;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.moyusowo.neoartisan.util.file.FileUtil.saveDefaultIfNotExists;

public final class BlockMappingsManager {

    private BlockMappingsManager() {}

    private static final Map<Integer, Integer> mappings = new ConcurrentHashMap<>();

    @InitMethod(priority = InitPriority.HIGHEST)
    public static void init() {
        File dataFolder = NeoArtisan.instance().getDataFolder();
        File configFile = new File(dataFolder, "mappings.yml");
        if (!configFile.exists()) {
            NeoArtisan.logger().warning("missing mappings.yml. Regenerated.");
            saveDefaultIfNotExists("mappings.yml");
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        for (String key : config.getKeys(false)) {
            String value = config.getString(key);
            if (value == null) {
                NeoArtisan.logger().severe("Error on reading mappings.yml: " + key + " don't have a value.");
                continue;
            }
            BlockData fromData = Bukkit.createBlockData(key);
            BlockData toData = Bukkit.createBlockData(value);
            WrappedBlockState fromState = WrappedBlockState.getByString(fromData.getAsString());
            WrappedBlockState toState = WrappedBlockState.getByString(toData.getAsString());
            mappings.put(fromState.getGlobalId(), toState.getGlobalId());
        }
    }

    public static Integer getMappedStateId(int original) {
        return mappings.getOrDefault(original, null);
    }

}
