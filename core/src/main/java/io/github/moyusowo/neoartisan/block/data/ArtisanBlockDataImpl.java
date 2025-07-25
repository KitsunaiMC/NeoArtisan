package io.github.moyusowo.neoartisan.block.data;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.block.base.ArtisanBaseBlockInternal;
import io.github.moyusowo.neoartisan.block.data.entity.BlockEntityManager;
import io.github.moyusowo.neoartisan.block.task.LifecycleTaskManagerInternal;
import io.github.moyusowo.neoartisan.block.task.SingleTaskPriority;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.block.base.ArtisanBaseBlock;
import io.github.moyusowo.neoartisanapi.api.block.block.ArtisanCropBlock;
import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.block.gui.ArtisanBlockGUI;
import io.github.moyusowo.neoartisanapi.api.block.task.LifecycleTaskManager;
import io.github.moyusowo.neoartisanapi.api.registry.Registries;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

class ArtisanBlockDataImpl implements ArtisanBlockDataInternal {
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
    private final LifecycleTaskManagerInternal lifecycleTaskManager;
    private final ArtisanBlockGUI artisanBlockGUI;

    private ArtisanBlockDataImpl(@NotNull NamespacedKey blockId, int stage, @NotNull Location location) {
        this.blockId = blockId;
        this.stage = stage;
        this.location = location;
        this.artisanBlockGUI = this.getArtisanBlockInternal().createGUI(location);
        this.lifecycleTaskManager = this.getArtisanBlockInternal().createLifecycleTaskManager(location);
        if (getArtisanBlock().hasBlockEntity()) {
            this.lifecycleTaskManager.addTerminateRunnable(() -> BlockEntityManager.remove(location), SingleTaskPriority.BLOCK_ENTITY);
        }
        if (this.artisanBlockGUI != null) {
            lifecycleTaskManager.addInitRunnable(artisanBlockGUI::onInit, SingleTaskPriority.GUI);
        }
        if (getArtisanBlockInternal() instanceof ArtisanCropBlock artisanCropBlock) {
            this.lifecycleTaskManager.addLifecycleTask(
                    () -> {
                        int random = ThreadLocalRandom.current().nextInt(0, 5000);
                        if (random > 3) return;
                        artisanCropBlock.onRandomTick(ArtisanBlockDataImpl.this);
                    },
                    0L,
                    1L,
                    false,
                    false
            );
        }
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
        return Registries.BLOCK.getArtisanBlock(blockId);
    }

    @Override
    @NotNull
    public ArtisanBaseBlockState getArtisanBlockState() {
        return Registries.BLOCK.getArtisanBlock(blockId).getState(stage);
    }

    @Override
    @NotNull
    public PersistentDataContainer getPersistentDataContainer() {
        if (getArtisanBlock().hasBlockEntity()) {
            return BlockEntityManager.getPDC(location);
        } else {
            throw new IllegalStateException("You can NOT get PDC from a block which doesn't have block entity!");
        }
    }

    @Override
    public @NotNull LifecycleTaskManager getLifecycleTaskManager() {
        return this.lifecycleTaskManager;
    }

    private ArtisanBaseBlockInternal getArtisanBlockInternal() {
        return (ArtisanBaseBlockInternal) getArtisanBlock();
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
