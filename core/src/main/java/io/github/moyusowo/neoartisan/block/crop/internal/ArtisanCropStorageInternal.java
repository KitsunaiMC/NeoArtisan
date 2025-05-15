package io.github.moyusowo.neoartisan.block.crop.internal;
import io.github.moyusowo.neoartisanapi.api.block.crop.CurrentCropStage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

@ApiStatus.Internal
public interface ArtisanCropStorageInternal {

    static ArtisanCropStorageInternal getArtisanCropStorageManager() {
        return Bukkit.getServicesManager().load(ArtisanCropStorageInternal.class);
    }

    Map<BlockPos, CurrentCropStage> getChunkArtisanCrops(Level level, int chunkX, int chunkZ);

    @NotNull
    CurrentCropStage getArtisanCropStage(Level level, BlockPos blockPos);

    boolean isArtisanCrop(Level level, BlockPos blockPos);


}
