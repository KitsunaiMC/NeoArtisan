package io.github.moyusowo.neoartisan.block.thin;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.util.BlockEventUtil;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockType;
import io.github.moyusowo.neoartisanapi.api.block.base.sound.SoundProperty;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockBase;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockState;
import io.github.moyusowo.neoartisanapi.api.block.gui.GUICreator;
import io.github.moyusowo.neoartisanapi.api.block.thin.ArtisanThinBlock;
import io.github.moyusowo.neoartisanapi.api.block.thin.ArtisanThinBlockData;
import io.github.moyusowo.neoartisanapi.api.block.thin.ArtisanThinBlockState;
import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

import java.util.List;

final class ArtisanThinBlockImpl extends ArtisanBlockBase implements ArtisanThinBlock {

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

    ArtisanThinBlockImpl(NamespacedKey blockId, List<? extends ArtisanBlockState> stages, GUICreator creator, SoundProperty placeSound, SoundProperty breakSound, ArtisanBlockType artisanBlockType) {
        super(blockId, stages, artisanBlockType, creator, placeSound, breakSound);
    }

    @Override
    @NotNull
    public ArtisanThinBlockState getState(int n) {
        return (ArtisanThinBlockState) super.getState(n);
    }

    public static class BuilderImpl implements Builder {

        protected NamespacedKey blockId;
        protected List<ArtisanThinBlockState> stages;
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
        public @NotNull Builder states(@NotNull List<ArtisanThinBlockState> states) {
            this.stages = states;
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
        public @NotNull Builder guiCreator(@NotNull GUICreator creator) {
            this.creator = creator;
            return this;
        }

        @Override
        public ArtisanThinBlock build() {
            if (blockId == null || stages == null) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanThinBlockImpl(blockId, stages, creator, placeSound, breakSound, ArtisanBlockType.THIN_BLOCK);
        }
    }

    public static final class BlockBehavior implements Listener {

        private BlockBehavior() {}

        @InitMethod(priority = InitPriority.LISTENER)
        static void init() {
            NeoArtisan.registerListener(new BlockBehavior());
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onPlace(PlayerInteractEvent event) {
            if (BlockEventUtil.canNotPlaceBasicCheck(event, ArtisanThinBlock.class)) return;
            if (event.getBlockFace() != BlockFace.UP) return;
            ArtisanItem artisanItem = NeoArtisanAPI.getItemRegistry().getArtisanItem(event.getItem());
            BlockEventUtil.onPlaceBasicLogic(
                    event,
                    event.getClickedBlock().getRelative(event.getBlockFace()),
                    event.getClickedBlock(),
                    ArtisanThinBlockData.factory().builder()
                            .blockId(artisanItem.getBlockId())
                            .stage(0)
                            .location(event.getClickedBlock().getRelative(event.getBlockFace()).getLocation())
                            .build()
            );
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onBreak(BlockBreakEvent event) {
            if (BlockEventUtil.isNotTypedArtisanBlock(event.getBlock(), ArtisanThinBlockData.class)) return;
            BlockEventUtil.onBreakBasicLogic(event);
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onBelowBlockBreak(BlockBreakEvent event) {
            if (BlockEventUtil.isNotTypedArtisanBlock(event.getBlock().getRelative(BlockFace.UP), ArtisanThinBlockData.class)) return;
            BlockEventUtil.onBelowBlockBreakBasicLogic(event);
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onBelowBlockPistonBreakOrMove(BlockPistonExtendEvent event) {
            BlockEventUtil.onBelowBlockPistonBreakOrMove(event, ArtisanThinBlockData.class);
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onWaterOrPistonBreak(BlockBreakBlockEvent event) {
            if (BlockEventUtil.isNotTypedArtisanBlock(event.getBlock(), ArtisanThinBlockData.class)) return;
            BlockEventUtil.onWaterOrPistonBreakBasicLogic(event);
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onBlockExplode(BlockExplodeEvent event) {
            BlockEventUtil.onBlockExplode(event, ArtisanThinBlockData.class);
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onEntityExplode(EntityExplodeEvent event) {
            BlockEventUtil.onEntityExplode(event, ArtisanThinBlockData.class);
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onEntityChangeBlock(EntityChangeBlockEvent event) {
            BlockEventUtil.onEntityChangeBlock(event, ArtisanThinBlockData.class);
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onStep(BlockRedstoneEvent event) {
            if (!NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(event.getBlock())) return;
            if (!(NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlockData(event.getBlock()) instanceof ArtisanThinBlockData)) return;
            event.setNewCurrent(0);
        }

    }
}
