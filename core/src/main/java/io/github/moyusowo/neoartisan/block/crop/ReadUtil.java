package io.github.moyusowo.neoartisan.block.crop;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.BlockStateUtil;
import io.github.moyusowo.neoartisanapi.api.block.crop.CropStageProperty;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.apache.commons.lang3.IntegerRange;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static io.github.moyusowo.neoartisan.util.Util.saveDefaultIfNotExists;
import static io.github.moyusowo.neoartisan.util.Util.stringToNamespaceKey;

final class ReadUtil {

    private ReadUtil() {}

    public static File[] readAllFiles() {
        File dataFolder = NeoArtisan.instance().getDataFolder();
        File blockFolder = new File(dataFolder, "block");
        File cropFolder = new File(blockFolder, "crop");
        if (!cropFolder.exists()) {
            cropFolder.mkdirs();
            saveDefaultIfNotExists("block/crop/magic_crop.yml");
        }
        return cropFolder.listFiles();
    }

    public static NamespacedKey getCropId(YamlConfiguration crop) {
        String cropId = crop.getString("cropId");
        if (cropId != null) return new NamespacedKey(NeoArtisan.instance(), cropId);
        else throw new IllegalArgumentException("You must provide a cropId!");
    }

    public static int getActualState(YamlConfiguration crop) {
        String actualStateName = crop.getString("actualState");
        Integer blockState = BlockStateUtil.parseBlockStateId(actualStateName);
        if (blockState != null) return blockState;
        else throw new IllegalArgumentException("You must provide a existed BlockState!");
    }

    public static List<CropStageProperty> getStages(YamlConfiguration crop) {
        List<CropStageProperty> properties = new ArrayList<>();
        ConfigurationSection stagesSection = crop.getConfigurationSection("stages");
        if (stagesSection == null) throw new IllegalArgumentException("You must provide any stages!");
        for (String key : stagesSection.getKeys(false)) {
            ConfigurationSection section = stagesSection.getConfigurationSection(key);
            if (section == null) throw new IllegalArgumentException("You can not provide a empty stage!");
            Integer appearance = BlockStateUtil.parseBlockStateId(section.getString("appearance"));
            if (appearance == null) throw new IllegalArgumentException("You must provide a appearance state!");
            List<ItemGenerator> generators = new ArrayList<>();
            ConfigurationSection drops = section.getConfigurationSection("drops");
            if (drops != null) {
                for (String registryId : drops.getKeys(false)) {
                    ConfigurationSection item = drops.getConfigurationSection(registryId);
                    if (item == null) {
                        int cnt = drops.getInt(registryId);
                        generators.add(ItemGenerator.SimpleGenerator(stringToNamespaceKey(registryId), cnt));
                    } else {
                        int min = item.getInt("min");
                        int max = item.getInt("max");
                        generators.add(ItemGenerator.rangedGenerator(stringToNamespaceKey(registryId), min, max));
                    }
                }
            }
            CropStageProperty cropStageProperty = new CropStageProperty(appearance, generators.toArray(new ItemGenerator[0]));
            properties.add(cropStageProperty);
        }
        if (properties.isEmpty()) throw new IllegalArgumentException("You must provide any stages!");
        return properties;
    }

    public static int getMinFertilizeGrowth(YamlConfiguration crop) {
        ConfigurationSection section = crop.getConfigurationSection("fertilizeGrowth");
        if (section == null) {
            return crop.getInt("fertilizeGrowth");
        } else {
            return section.getInt("min");
        }
    }

    public static int getMaxFertilizeGrowth(YamlConfiguration crop) {
        ConfigurationSection section = crop.getConfigurationSection("fertilizeGrowth");
        if (section == null) {
            return crop.getInt("fertilizeGrowth");
        } else {
            return section.getInt("max");
        }
    }
}
