package io.github.moyusowo.neoartisanapi.api.block.base;

import io.github.moyusowo.neoartisanapi.NeoArtisanAPI;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.ApiStatus;

public abstract class ArtisanBlockDataBase implements ArtisanBlockData {

    private final NamespacedKey blockId;
    private final int stage;
    private final PersistentDataContainer persistentDataContainer;

    protected ArtisanBlockDataBase(NamespacedKey blockId, int stage) {
        this.blockId = blockId;
        this.stage = stage;
        this.persistentDataContainer = NeoArtisanAPI.emptyPersistentDataContainer().emptyPersistentDataContainer();
    }

    @Override
    public ArtisanBlock getArtisanBlock() {
        return NeoArtisanAPI.getBlockRegistry().getArtisanBlock(blockId);
    }

    @Override
    public ArtisanBlockState getArtisanBlockState() {
        return NeoArtisanAPI.getBlockRegistry().getArtisanBlock(blockId).getState(stage);
    }

    @Override
    public PersistentDataContainer getPersistentDataContainer() {
        return this.persistentDataContainer;
    }

    @ApiStatus.Internal
    public void setPersistentDataContainer(PersistentDataContainer persistentDataContainer) {
        persistentDataContainer.copyTo(this.persistentDataContainer, true);
    }

    @Override
    public NamespacedKey blockId() {
        return this.blockId;
    }

    @Override
    public int stage() {
        return this.stage;
    }

}
