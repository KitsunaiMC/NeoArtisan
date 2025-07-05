package io.github.moyusowo.neoartisan.block.crop;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.storage.internal.ArtisanBlockStorageInternal;
import io.github.moyusowo.neoartisan.block.util.BlockEventUtil;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockType;
import io.github.moyusowo.neoartisanapi.api.block.base.sound.SoundProperty;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockBase;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlock;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCrop;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCropData;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCropState;
import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

final class ArtisanCropImpl extends ArtisanBlockBase implements ArtisanCrop {

    @InitMethod(priority = InitPriority.REGISTRAR)
    private static void init() {
        Bukkit.getServicesManager().register(
                BuilderFactory.class,
                new BuilderFactory() {
                    @Override
                    public @NotNull Builder builder() {
                        return new BuilderImpl();
                    }
                },
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private final int boneMealMinGrowth, boneMealMaxGrowth;

    ArtisanCropImpl(NamespacedKey cropId, List<ArtisanCropState> stages, int boneMealMinGrowth, int boneMealMaxGrowth, SoundProperty placeSound, SoundProperty breakSound, ArtisanBlockType artisanBlockType) {
        super(cropId, stages, artisanBlockType, null, placeSound, breakSound);
        this.boneMealMinGrowth = boneMealMinGrowth;
        this.boneMealMaxGrowth = boneMealMaxGrowth;
    }

    @Override
    public @NotNull ArtisanCropState getState(int n) {
        return (ArtisanCropState) super.getState(n);
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

    public static class BuilderImpl implements Builder {

        protected NamespacedKey blockId;
        protected List<ArtisanCropState> stages;
        protected int boneMealMinGrowth;
        protected int boneMealMaxGrowth;
        protected SoundProperty placeSound;
        protected SoundProperty breakSound;

        public BuilderImpl() {
            blockId = null;
            stages = null;
            placeSound = null;
            breakSound = null;
            boneMealMaxGrowth = -1;
            boneMealMinGrowth = -1;
        }

        @Override
        public @NotNull Builder blockId(@NotNull NamespacedKey blockId) {
            this.blockId = blockId;
            return this;
        }

        @Override
        public @NotNull Builder stages(@NotNull List<ArtisanCropState> stages) {
            this.stages = stages;
            return this;
        }

        @Override
        public @NotNull Builder placeSound(@NotNull SoundProperty placeSoundProperty) {
            this.placeSound = placeSoundProperty;
            return this;
        }

        @Override
        public @NotNull Builder breakSound(@NotNull SoundProperty breakSoundProperty) {
            this.breakSound = breakSoundProperty;
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
        public ArtisanBlock build() {
            if (blockId == null || stages == null || boneMealMinGrowth == -1 || boneMealMaxGrowth == -1) throw new IllegalArgumentException("You must fill all the param!");
            if (boneMealMinGrowth < 0 || boneMealMinGrowth > boneMealMaxGrowth) throw new IllegalArgumentException("min can't larger than max!");
            return new ArtisanCropImpl(blockId, stages, boneMealMinGrowth, boneMealMaxGrowth, placeSound, breakSound, ArtisanBlockType.CROP_BLOCK);
        }
    }

    public static final class ArtisanCropBehavior implements Listener {

        private static final HashMap<Block, ArtisanCropData> grownCrop = new HashMap<>();

        private ArtisanCropBehavior() {}

        @InitMethod(priority = InitPriority.LISTENER)
        static void init() {
            NeoArtisan.registerListener(new ArtisanCropBehavior());
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onPlace(PlayerInteractEvent event) throws Exception {
            if (BlockEventUtil.canNotPlaceBasicCheck(event, ArtisanCrop.class)) return;
            if (event.getClickedBlock().getType() != Material.FARMLAND) return;
            if (event.getBlockFace() != BlockFace.UP) return;
            if (event.getClickedBlock().getRelative(BlockFace.UP).getType() != Material.AIR) return;
            ArtisanItem artisanItem = NeoArtisanAPI.getItemRegistry().getArtisanItem(event.getItem());
            BlockEventUtil.onPlaceBasicLogic(
                    event,
                    event.getClickedBlock().getRelative(BlockFace.UP),
                    event.getClickedBlock(),
                    new ArtisanCropDataImpl(artisanItem.getBlockId(), 0, event.getClickedBlock().getRelative(BlockFace.UP).getLocation())
            );
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onBreak(BlockBreakEvent event) {
            if (BlockEventUtil.isNotTypedArtisanBlock(event.getBlock(), ArtisanCropData.class)) return;
            BlockEventUtil.onBreakBasicLogic(event);
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onBlockBreakUnderCrop(BlockBreakEvent event) {
            if (BlockEventUtil.isNotTypedArtisanBlock(event.getBlock().getRelative(BlockFace.UP), ArtisanCropData.class)) return;
            BlockEventUtil.onBelowBlockBreakBasicLogic(event);
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onPistonBreakOrMoveUnderCrop(BlockPistonExtendEvent event) {
            BlockEventUtil.onBelowBlockPistonBreakOrMove(event, ArtisanCropData.class);
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onBlockExplode(BlockExplodeEvent event) {
            BlockEventUtil.onBlockExplode(event, ArtisanCropData.class);
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onEntityExplode(EntityExplodeEvent event) {
            BlockEventUtil.onEntityExplode(event, ArtisanCropData.class);
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onEntityChangeBlock(EntityChangeBlockEvent event) {
            BlockEventUtil.onEntityChangeBlock(event, ArtisanCropData.class);
        }

        @EventHandler(priority = EventPriority.LOWEST)
        private static void onWaterOrPistonBreak(BlockBreakBlockEvent event) {
            if (BlockEventUtil.isNotTypedArtisanBlock(event.getBlock(), ArtisanCropData.class)) return;
            BlockEventUtil.onWaterOrPistonBreakBasicLogic(event);
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onEntityChangeFarmland(EntityChangeBlockEvent event) {
            if (event.isCancelled()) return;
            if (!NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(event.getBlock().getRelative(BlockFace.UP))) return;
            if (!(NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlockData(event.getBlock().getRelative(BlockFace.UP)) instanceof ArtisanCropData artisanCropData)) return;
            if (event.getBlock().getType() != Material.FARMLAND) return;
            event.setCancelled(true);
            event.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
            event.getBlock().setType(Material.DIRT);
            for (ItemStack drop: artisanCropData.getArtisanBlockState().drops()) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getRelative(BlockFace.UP).getLocation().add(0.5, 0.5, 0.5), drop);
            }
            ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock().getRelative(BlockFace.UP));
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onGrow(BlockGrowEvent event) {
            if (event.isCancelled()) return;
            if (!NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(event.getBlock())) return;
            if (!(NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlockData(event.getBlock()) instanceof ArtisanCropData artisanCropData)) return;
            event.setCancelled(true);
            if (artisanCropData.hasNextStage()) {
                grownCrop.put(event.getBlock(), artisanCropData);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        grownCrop.remove(event.getBlock());
                    }
                }.runTaskLater(NeoArtisan.instance(), 0L);
                BlockEventUtil.replace(event.getBlock(), artisanCropData.getNextStage());
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onFertilize(BlockFertilizeEvent event) {
            if (event.isCancelled()) return;
            for (BlockState blockState : event.getBlocks()) {
                if (NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(blockState.getBlock())) {
                    Object object = NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlockData(blockState.getBlock());
                    if (object instanceof ArtisanCropData) {
                        ArtisanCropData artisanCropData = grownCrop.get(blockState.getBlock());
                        BlockEventUtil.replace(blockState.getBlock(), artisanCropData);
                        grownCrop.remove(blockState.getBlock());
                        event.setCancelled(true);
                        if (artisanCropData.hasNextStage()) {
                            BlockEventUtil.replace(blockState.getBlock(), artisanCropData.getNextFertilizeStage());
                            BlockEventUtil.playBoneMealEffects(blockState.getLocation());
                        }
                    }
                }
            }
        }
    }

}
