package io.github.moyusowo.neoartisan.block.head;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.util.BlockEventUtil;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockBase;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockState;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockType;
import io.github.moyusowo.neoartisanapi.api.block.base.sound.SoundProperty;
import io.github.moyusowo.neoartisanapi.api.block.gui.GUICreator;
import io.github.moyusowo.neoartisanapi.api.block.head.ArtisanHeadBlock;
import io.github.moyusowo.neoartisanapi.api.block.head.ArtisanHeadBlockData;
import io.github.moyusowo.neoartisanapi.api.block.head.ArtisanHeadBlockState;
import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static io.github.moyusowo.neoartisan.block.util.BlockEventUtil.isNotTypedArtisanBlock;
import static io.github.moyusowo.neoartisan.block.util.BoundingBoxUtil.overlap;

final class ArtisanHeadBlockImpl extends ArtisanBlockBase implements ArtisanHeadBlock {
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

    ArtisanHeadBlockImpl(@NotNull NamespacedKey blockId, @NotNull List<? extends ArtisanBlockState> stages, ArtisanBlockType artisanBlockType, @Nullable GUICreator creator, SoundProperty placeSound, SoundProperty breakSound) {
        super(blockId, stages, artisanBlockType, creator, placeSound, breakSound);
    }

    @Override
    @NotNull
    public ArtisanHeadBlockState getState(int n) {
        return (ArtisanHeadBlockState) super.getState(n);
    }

    private static class BuilderImpl implements Builder {

        protected NamespacedKey blockId;
        protected List<ArtisanHeadBlockState> stages;
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
        public @NotNull Builder states(@NotNull List<ArtisanHeadBlockState> states) {
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
        public ArtisanHeadBlock build() {
            if (blockId == null || stages == null) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanHeadBlockImpl(blockId, stages, ArtisanBlockType.FULL_BLOCK, creator, placeSound, breakSound);
        }
    }

    public static final class ArtisanHeadBlockBehavior implements Listener {
        private ArtisanHeadBlockBehavior() {}

        @InitMethod(priority = InitPriority.LISTENER)
        static void init() {
            NeoArtisan.registerListener(new ArtisanHeadBlockBehavior());
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onPlace(PlayerInteractEvent event) {
            if (BlockEventUtil.canNotPlaceBasicCheck(event, ArtisanHeadBlock.class)) return;
            ArtisanItem artisanItem = NeoArtisanAPI.getItemRegistry().getArtisanItem(event.getItem());
            if (overlap(event.getClickedBlock().getRelative(event.getBlockFace()))) return;
            BlockEventUtil.onPlaceBasicLogic(
                    event,
                    event.getClickedBlock().getRelative(event.getBlockFace()),
                    event.getClickedBlock(),
                    ArtisanHeadBlockData.factory().builder()
                            .blockId(artisanItem.getBlockId())
                            .stage(0)
                            .location(event.getClickedBlock().getRelative(event.getBlockFace()).getLocation())
                            .build()
            );
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onBreak(BlockBreakEvent event) {
            if (isNotTypedArtisanBlock(event.getBlock(), ArtisanHeadBlockData.class)) return;
            BlockEventUtil.onBreakBasicLogic(event);
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onBlockExplode(BlockExplodeEvent event) {
            BlockEventUtil.onBlockExplode(event, ArtisanHeadBlockData.class);
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onEntityExplode(EntityExplodeEvent event) {
            BlockEventUtil.onEntityExplode(event, ArtisanHeadBlockData.class);
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onEntityChangeBlock(EntityChangeBlockEvent event) {
            BlockEventUtil.onEntityChangeBlock(event, ArtisanHeadBlockData.class);
        }

        @EventHandler(priority = EventPriority.LOWEST)
        public void onPistonBreak(BlockBreakBlockEvent event) {
            if (BlockEventUtil.isNotTypedArtisanBlock(event.getBlock(), ArtisanHeadBlockData.class)) return;
            BlockEventUtil.onWaterOrPistonBreakBasicLogic(event);
        }
    }
}
