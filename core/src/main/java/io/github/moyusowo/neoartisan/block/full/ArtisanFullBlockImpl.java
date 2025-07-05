package io.github.moyusowo.neoartisan.block.full;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.util.BlockEventUtil;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockBase;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockState;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockType;
import io.github.moyusowo.neoartisanapi.api.block.base.sound.SoundProperty;
import io.github.moyusowo.neoartisanapi.api.block.full.ArtisanFullBlock;
import io.github.moyusowo.neoartisanapi.api.block.full.ArtisanFullBlockData;
import io.github.moyusowo.neoartisanapi.api.block.full.ArtisanFullBlockState;
import io.github.moyusowo.neoartisanapi.api.block.gui.GUICreator;
import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static io.github.moyusowo.neoartisan.block.util.BlockEventUtil.isNotTypedArtisanBlock;
import static io.github.moyusowo.neoartisan.block.util.BoundingBoxUtil.overlap;

class ArtisanFullBlockImpl extends ArtisanBlockBase implements ArtisanFullBlock {
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

    protected ArtisanFullBlockImpl(@NotNull NamespacedKey blockId, @NotNull List<? extends ArtisanBlockState> stages, ArtisanBlockType artisanBlockType, @Nullable GUICreator creator, SoundProperty placeSound, SoundProperty breakSound) {
        super(blockId, stages, artisanBlockType, creator, placeSound, breakSound);
    }

    @Override
    @NotNull
    public ArtisanFullBlockState getState(int n) {
        return (ArtisanFullBlockState) super.getState(n);
    }

    private static class BuilderImpl implements Builder {

        protected NamespacedKey blockId;
        protected List<ArtisanFullBlockState> stages;
        protected GUICreator creator;
        protected SoundProperty placeSound;
        protected SoundProperty breakSound;

        public BuilderImpl() {
            placeSound = null;
            breakSound = null;
            blockId = null;
            stages = null;
            creator = null;
        }

        @Override
        public @NotNull Builder blockId(@NotNull NamespacedKey blockId) {
            this.blockId = blockId;
            return this;
        }

        @Override
        public @NotNull Builder states(@NotNull List<ArtisanFullBlockState> states) {
            this.stages = states;
            return this;
        }

        @Override
        public @NotNull Builder guiCreator(@NotNull GUICreator creator) {
            this.creator = creator;
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
        public ArtisanFullBlock build() {
            if (blockId == null || stages == null) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanFullBlockImpl(blockId, stages, ArtisanBlockType.FULL_BLOCK, creator, placeSound, breakSound);
        }
    }

    public static final class ArtisanFullBlockBehavior implements Listener {

        private ArtisanFullBlockBehavior() {}

        @InitMethod(priority = InitPriority.LISTENER)
        static void init() {
            NeoArtisan.registerListener(new ArtisanFullBlockBehavior());
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onPlace(PlayerInteractEvent event) {
            if (BlockEventUtil.canNotPlaceBasicCheck(event, ArtisanFullBlock.class)) return;
            ArtisanItem artisanItem = NeoArtisanAPI.getItemRegistry().getArtisanItem(event.getItem());
            if (overlap(event.getClickedBlock().getRelative(event.getBlockFace()))) return;
            BlockEventUtil.onPlaceBasicLogic(
                    event,
                    event.getClickedBlock().getRelative(event.getBlockFace()),
                    event.getClickedBlock(),
                    ArtisanFullBlockData.factory().builder()
                            .blockId(artisanItem.getBlockId())
                            .stage(0)
                            .location(event.getClickedBlock().getRelative(event.getBlockFace()).getLocation())
                            .build()
            );
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onBreak(BlockBreakEvent event) {
            if (isNotTypedArtisanBlock(event.getBlock(), ArtisanFullBlockData.class)) return;
            BlockEventUtil.onBreakBasicLogic(event);
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onBlockExplode(BlockExplodeEvent event) {
            BlockEventUtil.onBlockExplode(event, ArtisanFullBlockData.class);
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onEntityExplode(EntityExplodeEvent event) {
            BlockEventUtil.onEntityExplode(event, ArtisanFullBlockData.class);
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onEntityChangeBlock(EntityChangeBlockEvent event) {
            BlockEventUtil.onEntityChangeBlock(event, ArtisanFullBlockData.class);
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onPistonPushBlock(BlockPistonExtendEvent event) {
            if (event.isCancelled()) return;
            for (Block block : event.getBlocks()) {
                if (isNotTypedArtisanBlock(block, ArtisanFullBlockData.class)) continue;
                event.setCancelled(true);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Player player : block.getLocation().getNearbyPlayers(64)) {
                            player.sendBlockChange(
                                    block.getLocation(),
                                    CraftBlockData.createData(
                                            net.minecraft.world.level.block.Block.stateById(
                                                    NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlockData(block).getArtisanBlockState().appearanceState()
                                            )
                                    )
                            );
                        }
                    }
                }.runTaskLater(NeoArtisan.instance(), 2L);
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onPistonPullBlock(BlockPistonRetractEvent event) {
            if (event.isCancelled()) return;
            if (event.getBlock().getType() != Material.MOVING_PISTON) return;
            for (Block block : event.getBlocks()) {
                if (isNotTypedArtisanBlock(block, ArtisanFullBlockData.class)) continue;
                event.setCancelled(true);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        for (Player player : block.getLocation().getNearbyPlayers(16)) {
                            player.sendBlockChange(
                                    block.getLocation(),
                                    CraftBlockData.createData(
                                            net.minecraft.world.level.block.Block.stateById(
                                                    NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlockData(block).getArtisanBlockState().appearanceState()
                                            )
                                    )
                            );
                        }
                    }
                }.runTaskLater(NeoArtisan.instance(), 2L);
            }
        }
    }
}
