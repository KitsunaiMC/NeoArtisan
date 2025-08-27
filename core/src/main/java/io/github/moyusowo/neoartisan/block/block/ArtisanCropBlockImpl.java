package io.github.moyusowo.neoartisan.block.block;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.block.base.ArtisanBaseBlockImpl;
import io.github.moyusowo.neoartisan.block.block.listener.Util;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.block.ArtisanCropBlock;
import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.block.storage.Storages;
import io.github.moyusowo.neoartisanapi.api.block.util.SoundProperty;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Farmland;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

final class ArtisanCropBlockImpl extends ArtisanBaseBlockImpl implements ArtisanCropBlock {
    @InitMethod(priority = InitPriority.REGISTRAR)
    private static void init() {
        Bukkit.getServicesManager().register(
                BuilderFactory.class,
                BuilderImpl::new,
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private final int boneMealMinGrowth, boneMealMaxGrowth;

    private ArtisanCropBlockImpl(@NotNull NamespacedKey blockId, @NotNull List<ArtisanBaseBlockState> stages, SoundProperty placeSound, int boneMealMinGrowth, int boneMealMaxGrowth) {
        super(blockId, stages, null, placeSound);
        this.boneMealMinGrowth = boneMealMinGrowth;
        this.boneMealMaxGrowth = boneMealMaxGrowth;
    }

    @Override
    public int getBoneMealMinGrowth() {
        return this.boneMealMinGrowth;
    }

    @Override
    public int getBoneMealMaxGrowth() {
        return this.boneMealMaxGrowth;
    }

    @Override
    public int generateBoneMealGrowth() {
        return ThreadLocalRandom.current().nextInt(this.boneMealMinGrowth, this.boneMealMaxGrowth + 1);
    }

    @Override
    public void onRandomTick(ArtisanBlockData cropData) {
        if (Util.hasNextStage(cropData) && cropData.getLocation().getBlock().getLightLevel() >= 9) {
            double g = 1;
            final Block farmland = cropData.getLocation().getBlock().getRelative(BlockFace.DOWN);
            if (farmland.getType() != Material.FARMLAND || (!(farmland.getBlockData() instanceof Farmland farmlandData))) return;
            if (farmlandData.getMoisture() == 0) g += 1;
            else g += 3;
            final BlockFace[] faces = { BlockFace.EAST, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.NORTH_EAST, BlockFace.NORTH_WEST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST };
            for (BlockFace face : faces) {
                final Block block = farmland.getRelative(face);
                if (block.getType() == Material.FARMLAND) {
                    final Farmland data = (Farmland) block.getBlockData();
                    if (data.getMoisture() == 0) g += 0.25;
                    else g += 0.75;
                }
            }
            boolean hasInOblique = false;
            for (int i = 4; i < 8; i++) {
                if (Storages.BLOCK.isArtisanBlock(cropData.getLocation().getBlock().getRelative(faces[i])) && Storages.BLOCK.getArtisanBlockData(cropData.getLocation().getBlock().getRelative(faces[i])).blockId().equals(cropData.blockId())) {
                    hasInOblique = true;
                    break;
                }
            }
            if (hasInOblique) g /= 2;
            boolean hasInEast = Storages.BLOCK.isArtisanBlock(cropData.getLocation().getBlock().getRelative(BlockFace.EAST)) && Storages.BLOCK.getArtisanBlockData(cropData.getLocation().getBlock().getRelative(BlockFace.EAST)).blockId().equals(cropData.blockId()),
                    hasInWest = Storages.BLOCK.isArtisanBlock(cropData.getLocation().getBlock().getRelative(BlockFace.WEST)) && Storages.BLOCK.getArtisanBlockData(cropData.getLocation().getBlock().getRelative(BlockFace.WEST)).blockId().equals(cropData.blockId()),
                    hasInNorth = Storages.BLOCK.isArtisanBlock(cropData.getLocation().getBlock().getRelative(BlockFace.NORTH)) && Storages.BLOCK.getArtisanBlockData(cropData.getLocation().getBlock().getRelative(BlockFace.NORTH)).blockId().equals(cropData.blockId()),
                    hasInSouth = Storages.BLOCK.isArtisanBlock(cropData.getLocation().getBlock().getRelative(BlockFace.SOUTH)) && Storages.BLOCK.getArtisanBlockData(cropData.getLocation().getBlock().getRelative(BlockFace.SOUTH)).blockId().equals(cropData.blockId());
            if (((hasInEast && hasInNorth) || (hasInEast && hasInSouth) || (hasInWest && hasInNorth) || (hasInWest && hasInSouth)) && !hasInOblique) g /= 2;
            final double rate = 1.0 / (1.0 + 25.0 / g);
            if (ThreadLocalRandom.current().nextDouble() < rate) {
                Util.replace(cropData.getLocation().getBlock(), Util.getNextStage(cropData));
            }
        }
    }

    public static final class BuilderImpl implements Builder {
        private NamespacedKey blockId;
        private List<ArtisanBaseBlockState> stages;
        private int boneMealMinGrowth;
        private int boneMealMaxGrowth;
        private SoundProperty placeSound;

        public BuilderImpl() {
            blockId = null;
            stages = null;
            placeSound = null;
            boneMealMaxGrowth = -1;
            boneMealMinGrowth = -1;
        }

        @Override
        public @NotNull Builder blockId(@NotNull NamespacedKey blockId) {
            this.blockId = blockId;
            return this;
        }

        @Override
        public @NotNull Builder states(@NotNull List<ArtisanBaseBlockState> states) {
            this.stages = states;
            return this;
        }

        @Override
        public @NotNull Builder placeSound(@NotNull SoundProperty placeSoundProperty) {
            this.placeSound = placeSoundProperty;
            return this;
        }

        @Override
        public @NotNull Builder boneMealMinGrowth(int boneMealMinGrowth) {
            this.boneMealMinGrowth = boneMealMinGrowth;
            return this;
        }

        @Override
        public @NotNull Builder boneMealMaxGrowth(int boneMealMaxGrowth) {
            this.boneMealMaxGrowth = boneMealMaxGrowth;
            return this;
        }

        @Override
        public @NotNull ArtisanCropBlock build() {
            if (blockId == null || stages == null || boneMealMinGrowth == -1 || boneMealMaxGrowth == -1) throw new IllegalArgumentException("You must fill all the param!");
            if (boneMealMinGrowth < 0 || boneMealMinGrowth > boneMealMaxGrowth) throw new IllegalArgumentException("min can't larger than max!");
            return new ArtisanCropBlockImpl(blockId, stages, placeSound, boneMealMinGrowth, boneMealMaxGrowth);
        }
    }
}
