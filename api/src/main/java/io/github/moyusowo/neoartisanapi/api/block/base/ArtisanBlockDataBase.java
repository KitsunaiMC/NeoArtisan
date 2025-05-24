package io.github.moyusowo.neoartisanapi.api.block.base;

import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.gui.ArtisanBlockGUI;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

public abstract class ArtisanBlockDataBase implements ArtisanBlockData {

    private final Location location;
    private final NamespacedKey blockId;
    private final int stage;
    private final PersistentDataContainer persistentDataContainer;
    private final ArtisanBlockGUI artisanBlockGUI;

    protected ArtisanBlockDataBase(NamespacedKey blockId, int stage, Location location) {
        this.blockId = blockId;
        this.stage = stage;
        this.location = location;
        this.persistentDataContainer = NeoArtisanAPI.emptyPersistentDataContainer().emptyPersistentDataContainer();
        this.artisanBlockGUI = this.getArtisanBlock().createGUI(location);
    }

    @Override
    @Nullable
    public ArtisanBlockGUI getGUI() {
        return this.artisanBlockGUI;
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

    @Override
    public Location getLocation() {
        return this.location;
    }

}
