package io.github.moyusowo.neoartisan.block.network;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.block.data.CraftBlockData;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.moyusowo.neoartisan.block.util.BlockStateUtil.parseBlockStateId;
import static io.github.moyusowo.neoartisan.util.Util.saveDefaultIfNotExists;

public final class BlockMappingsManager {

    private BlockMappingsManager() {}

    private static final Map<Integer, Integer> mappings = new ConcurrentHashMap<>();

    private static final Set<BlockState> usedStates = new HashSet<>();

    @InitMethod(priority = InitPriority.HIGHEST)
    public static void init() {
        File dataFolder = NeoArtisan.instance().getDataFolder();
        File itemFolder = new File(dataFolder, "block");
        if (!itemFolder.exists()) {
            itemFolder.mkdirs();
        }
        File configFile = new File(itemFolder, "mappings.yml");
        if (!configFile.exists()) {
            NeoArtisan.logger().warning("missing mappings.yml. Regenerated.");
            saveDefaultIfNotExists("block/mappings.yml");
        }
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
        for (String key : config.getKeys(false)) {
            String value = config.getString(key);
            BlockState fromState = ((CraftBlockData) Bukkit.createBlockData(key)).getState();
            BlockState toState = ((CraftBlockData) Bukkit.createBlockData(value)).getState();
            int fromStateId = Block.getId(fromState);
            int toStateId = Block.getId(toState);
            mappings.put(fromStateId, toStateId);
            usedStates.add(Block.stateById(toStateId));
        }
    }

    public static Integer getMappedStateId(int original) {
        return mappings.getOrDefault(original, null);
    }

    public static BlockState getMappedState(BlockState original) {
        Integer stateId = mappings.getOrDefault(Block.getId(original), null);
        if (stateId != null) return Block.stateById(stateId);
        else return null;
    }

    public static Set<BlockState> getUsedStates() {
        return Set.copyOf(usedStates);
    }

}
