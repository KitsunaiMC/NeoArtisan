package io.github.moyusowo.neoartisan.block.crop;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.network.BlockMappingsManager;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisanapi.api.block.crop.CropRegistry;
import io.github.moyusowo.neoartisanapi.api.block.crop.CropStageProperty;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.ServicePriority;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.moyusowo.neoartisan.block.BlockStateUtil.stateById;

class CropRegistryImpl implements CropRegistry {

    private static CropRegistryImpl instance;

    public static CropRegistryImpl getInstance() {
        return instance;
    }

    @InitMethod
    public static void init() {
        new CropRegistryImpl();
    }

    private final ConcurrentHashMap<NamespacedKey, ArtisanCropImpl> registry;

    private final Set<BlockState> usedStates;

    private CropRegistryImpl() {
        instance = this;
        registry = new ConcurrentHashMap<>();
        usedStates = BlockMappingsManager.getUsedStates();
        Bukkit.getServicesManager().register(
                CropRegistry.class,
                CropRegistryImpl.getInstance(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    public void registerCrop(NamespacedKey cropId, int actualState, List<CropStageProperty> stages, int boneMealMinGrowth, int boneMealMaxGrowth) {
        for (CropStageProperty property : stages) {
            if (usedStates.contains(stateById(property.appearanceState()))) {
                throw new IllegalArgumentException("The BlockState: " + stateById(property.appearanceState()) + " is used!");
            }
        }
        registry.put(cropId, new ArtisanCropImpl(cropId, actualState, stages, boneMealMinGrowth, boneMealMaxGrowth));
    }

    public void registerCrop(NamespacedKey cropId, int actualState, List<CropStageProperty> stages, int boneMealGrowth) {
        registerCrop(cropId, actualState, stages, boneMealGrowth, boneMealGrowth);
    }

    public boolean isArtisanCrop(NamespacedKey cropId) {
        return registry.containsKey(cropId);
    }

    public ArtisanCropImpl getArtisanCrop(NamespacedKey cropId) {
        return registry.get(cropId);
    }
}
