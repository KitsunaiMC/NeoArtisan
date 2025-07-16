package io.github.moyusowo.neoartisan.block.blockdata;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.block.ArtisanBaseBlock;
import io.github.moyusowo.neoartisanapi.api.block.blockdata.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.blockstate.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.block.gui.ArtisanBlockGUI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArtisanBlockDataImpl implements ArtisanBlockDataInternal {
    @InitMethod(priority = InitPriority.BLOCKDATA)
    private static void init() {
        Bukkit.getServicesManager().register(
                BuilderFactory.class,
                BuilderImpl::new,
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private final Location location;
    private final NamespacedKey blockId;
    private final int stage;
    private final PersistentDataContainer persistentDataContainer;
    private final ArtisanBlockGUI artisanBlockGUI;

    public ArtisanBlockDataImpl(@NotNull NamespacedKey blockId, int stage, @NotNull Location location) {
        this.blockId = blockId;
        this.stage = stage;
        this.location = location;
        this.persistentDataContainer = NeoArtisanAPI.emptyPersistentDataContainer().emptyPersistentDataContainer();
        this.artisanBlockGUI = this.getArtisanBlock().createGUI(location);
    }

    @Override
    public void setPersistentDataContainer(@NotNull PersistentDataContainer persistentDataContainer) {
        persistentDataContainer.copyTo(this.persistentDataContainer, true);
    }

    @Override
    public @Nullable ArtisanBlockGUI getGUI() {
        return this.artisanBlockGUI;
    }

    @Override
    public @NotNull Location getLocation() {
        return this.location;
    }

    @Override
    public @NotNull NamespacedKey blockId() {
        return this.blockId;
    }

    @Override
    public int stage() {
        return this.stage;
    }

    @Override
    @NotNull
    public ArtisanBaseBlock getArtisanBlock() {
        return NeoArtisanAPI.getBlockRegistry().getArtisanBlock(blockId);
    }

    @Override
    public @NotNull ArtisanBaseBlockState getArtisanBlockState() {
        return NeoArtisanAPI.getBlockRegistry().getArtisanBlock(blockId).getState(stage);
    }

    @Override
    public @NotNull PersistentDataContainer getPersistentDataContainer() {
        return this.persistentDataContainer;
    }

    private static class BuilderImpl implements Builder {
        private NamespacedKey blockId;
        private int stage;
        private Location location;

        public BuilderImpl() {
            blockId = null;
            location = null;
            stage = -1;
        }

        @Override
        @NotNull
        public Builder blockId(@NotNull NamespacedKey blockId) {
            this.blockId = blockId;
            return this;
        }

        @Override
        @NotNull
        public Builder stage(int stage) {
            this.stage = stage;
            return this;
        }

        @Override
        @NotNull
        public Builder location(@NotNull Location location) {
            this.location = location;
            return this;
        }

        @Override
        @NotNull
        public ArtisanBlockData build() {
            if (blockId == null || stage < 0 || location == null) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanBlockDataImpl(blockId, stage, location);
        }
    }
}
