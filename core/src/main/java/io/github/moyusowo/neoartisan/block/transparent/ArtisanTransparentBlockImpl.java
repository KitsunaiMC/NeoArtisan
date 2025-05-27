package io.github.moyusowo.neoartisan.block.transparent;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.util.BlockEventUtil;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.SoundProperty;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockBase;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockState;
import io.github.moyusowo.neoartisanapi.api.block.gui.GUICreator;
import io.github.moyusowo.neoartisanapi.api.block.transparent.ArtisanTransparentBlock;
import io.github.moyusowo.neoartisanapi.api.block.transparent.ArtisanTransparentBlockData;
import io.github.moyusowo.neoartisanapi.api.block.transparent.ArtisanTransparentBlockState;
import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static io.github.moyusowo.neoartisan.block.util.BoundingBoxUtil.overlap;

class ArtisanTransparentBlockImpl extends ArtisanBlockBase implements ArtisanTransparentBlock {

    @InitMethod(priority = InitPriority.REGISTRAR)
    private static void init() {
        Bukkit.getServicesManager().register(
                Builder.class,
                new BuilderImpl(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private final boolean canBurn;

    protected ArtisanTransparentBlockImpl(NamespacedKey blockId, List<? extends ArtisanBlockState> stages, GUICreator creator, boolean canBurn, SoundProperty placeSound, SoundProperty breakSound) {
        super(blockId, stages, creator, placeSound, breakSound);
        this.canBurn = canBurn;
    }

    @Override
    public boolean canBurn() {
        return this.canBurn;
    }

    @Override
    @NotNull
    public ArtisanTransparentBlockState getState(int n) {
        return (ArtisanTransparentBlockState) super.getState(n);
    }

    public static class BuilderImpl implements Builder {

        protected NamespacedKey blockId;
        protected List<ArtisanTransparentBlockState> stages;
        protected boolean canBurn;
        protected GUICreator creator;
        protected SoundProperty placeSound;
        protected SoundProperty breakSound;

        public BuilderImpl() {
            placeSound = null;
            breakSound = null;
            blockId = null;
            stages = null;
            creator = null;
            canBurn = false;
        }

        @Override
        public @NotNull Builder blockId(@NotNull NamespacedKey blockId) {
            this.blockId = blockId;
            return this;
        }

        @Override
        public @NotNull Builder states(@NotNull List<ArtisanTransparentBlockState> states) {
            this.stages = states;
            return this;
        }

        @Override
        public @NotNull Builder canBurn(boolean canBurn) {
            this.canBurn = canBurn;
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
        public ArtisanTransparentBlock build() {
            if (blockId == null || stages == null) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanTransparentBlockImpl(blockId, stages, creator, canBurn, placeSound, breakSound);
        }
    }

    public static final class ArtisanTransparentBlockBehavior implements Listener {

        private ArtisanTransparentBlockBehavior() {}

        @InitMethod(priority = InitPriority.LISTENER)
        static void init() {
            NeoArtisan.registerListener(new ArtisanTransparentBlockBehavior());
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onPlace(PlayerInteractEvent event) throws Exception {
            if (BlockEventUtil.canNotPlaceBasicCheck(event, ArtisanTransparentBlock.class)) return;
            ArtisanItem artisanItem = NeoArtisanAPI.getItemRegistry().getArtisanItem(event.getItem());
            if (overlap(event.getPlayer(), event.getClickedBlock().getRelative(event.getBlockFace()))) return;
            BlockEventUtil.onPlaceBasicLogic(
                    event,
                    event.getClickedBlock().getRelative(event.getBlockFace()),
                    event.getClickedBlock(),
                    ArtisanTransparentBlockData.builder()
                            .blockId(artisanItem.getBlockId())
                            .stage(0)
                            .location(event.getClickedBlock().getRelative(event.getBlockFace()).getLocation())
                            .build()
            );
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onBreak(BlockBreakEvent event) {
            if (BlockEventUtil.isNotTypedArtisanBlock(event.getBlock(), ArtisanTransparentBlockData.class)) return;
            BlockEventUtil.onBreakBasicLogic(event);
        }

        @EventHandler(priority = EventPriority.LOWEST)
        private static void onPistonBreak(BlockBreakBlockEvent event) {
            if (BlockEventUtil.isNotTypedArtisanBlock(event.getBlock(), ArtisanTransparentBlockData.class)) return;
            BlockEventUtil.onWaterOrPistonBreakBasicLogic(event);
        }

        @EventHandler
        private static void onBlockExplode(BlockExplodeEvent event) {
            BlockEventUtil.onBlockExplode(event, ArtisanTransparentBlockData.class);
        }

        @EventHandler
        private static void onEntityExplode(EntityExplodeEvent event) {
            BlockEventUtil.onEntityExplode(event, ArtisanTransparentBlockData.class);
        }

        @EventHandler
        private static void onEntityChangeBlock(EntityChangeBlockEvent event) {
            BlockEventUtil.onEntityChangeBlock(event, ArtisanTransparentBlockData.class);
        }

        @EventHandler
        private static void onBurn(BlockBurnEvent event) {
            if (event.isCancelled()) return;
            if (!NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(event.getBlock())) return;
            if (!(NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlock(event.getBlock()) instanceof ArtisanTransparentBlockData artisanTransparentBlockData)) return;
            if (artisanTransparentBlockData.getArtisanBlock().canBurn()) return;
            event.setCancelled(true);
        }
    }
}
