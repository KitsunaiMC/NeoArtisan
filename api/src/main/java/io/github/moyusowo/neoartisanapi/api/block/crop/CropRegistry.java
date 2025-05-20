package io.github.moyusowo.neoartisanapi.api.block.crop;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlock;
import io.github.moyusowo.neoartisanapi.api.block.base.BlockRegistry;
import org.bukkit.NamespacedKey;

import java.util.List;

public interface CropRegistry extends BlockRegistry {

    boolean isArtisanBlock(NamespacedKey cropId);

    ArtisanCrop getArtisanBlock(NamespacedKey cropId);
}
