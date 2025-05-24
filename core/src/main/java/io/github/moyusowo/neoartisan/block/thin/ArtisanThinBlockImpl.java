package io.github.moyusowo.neoartisan.block.thin;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.storage.internal.ArtisanBlockStorageInternal;
import io.github.moyusowo.neoartisan.block.util.InteractionUtil;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockBase;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockState;
import io.github.moyusowo.neoartisanapi.api.block.event.ArtisanBlockPlaceEvent;
import io.github.moyusowo.neoartisanapi.api.block.gui.GUICreator;
import io.github.moyusowo.neoartisanapi.api.block.thin.ArtisanThinBlock;
import io.github.moyusowo.neoartisanapi.api.block.thin.ArtisanThinBlockData;
import io.github.moyusowo.neoartisanapi.api.block.thin.ArtisanThinBlockState;
import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static io.github.moyusowo.neoartisan.block.util.BlockStateUtil.stateById;

public class ArtisanThinBlockImpl extends ArtisanBlockBase implements ArtisanThinBlock {

    @InitMethod(order = InitPriority.HIGH)
    private static void init() {
        Bukkit.getServicesManager().register(
                Builder.class,
                new BuilderImpl(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    protected ArtisanThinBlockImpl(NamespacedKey blockId, List<? extends ArtisanBlockState> stages, GUICreator creator) {
        super(blockId, stages, creator);
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

        public BuilderImpl() {
            blockId = null;
            stages = null;
            creator = null;
        }

        @Override
        public Builder blockId(NamespacedKey blockId) {
            this.blockId = blockId;
            return this;
        }

        @Override
        public Builder states(List<ArtisanThinBlockState> states) {
            this.stages = states;
            return this;
        }

        @Override
        public Builder guiCreator(GUICreator creator) {
            this.creator = creator;
            return this;
        }

        @Override
        public ArtisanThinBlock build() {
            if (blockId == null || stages == null) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanThinBlockImpl(blockId, stages, creator);
        }
    }

    public static final class BlockBehavior implements Listener {

        private BlockBehavior() {}

        @InitMethod
        static void init() {
            NeoArtisan.registerListener(new BlockBehavior());
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onPlace(PlayerInteractEvent event) throws Exception {
            if (event.useInteractedBlock() == Event.Result.DENY) return;
            if (event.useItemInHand() == Event.Result.DENY) return;
            if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
            if (event.getBlockFace() != BlockFace.UP) return;
            if (!NeoArtisanAPI.getItemRegistry().isArtisanItem(event.getItem())) return;
            ArtisanItem artisanItem = NeoArtisanAPI.getItemRegistry().getArtisanItem(event.getItem());
            if (artisanItem.getBlockId() == null) return;
            if (!NeoArtisanAPI.getBlockRegistry().isArtisanBlock(artisanItem.getBlockId())) return;
            if (!(NeoArtisanAPI.getBlockRegistry().getArtisanBlock(artisanItem.getBlockId()) instanceof ArtisanThinBlock)) return;
            if ((!event.getPlayer().isSneaking()) && InteractionUtil.isInteractable(event.getClickedBlock())) return;
            event.setCancelled(true);
            ArtisanBlockPlaceEvent artisanBlockPlaceEvent = new ArtisanBlockPlaceEvent(
                    event.getClickedBlock().getRelative(event.getBlockFace()),
                    event.getClickedBlock().getRelative(event.getBlockFace()).getState(),
                    event.getClickedBlock(),
                    event.getItem(),
                    event.getPlayer(),
                    true,
                    EquipmentSlot.HAND,
                    NeoArtisanAPI.getBlockRegistry().getArtisanBlock(artisanItem.getBlockId())
            );
            Bukkit.getPluginManager().callEvent(artisanBlockPlaceEvent);
            if (artisanBlockPlaceEvent.isCancelled()) return;
            place(
                    event.getClickedBlock().getRelative(event.getBlockFace()),
                    ArtisanThinBlockData.builder()
                            .blockId(artisanItem.getBlockId())
                            .stage(0)
                            .location(event.getClickedBlock().getRelative(event.getBlockFace()).getLocation())
                            .build()
            );
            if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                event.getItem().setAmount(event.getItem().getAmount() - 1);
            }
        }

        @EventHandler
        private static void onBreak(BlockBreakEvent event) {
            if (event.isCancelled()) return;
            if (!NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(event.getBlock())) return;
            if (!(NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlock(event.getBlock()) instanceof ArtisanThinBlockData artisanThinBlockData)) return;
            if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
                ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock());
                return;
            }
            event.setCancelled(true);
            event.getPlayer().giveExp(event.getExpToDrop());
            event.getBlock().setType(Material.AIR);
            for (ItemStack drop : artisanThinBlockData.getArtisanBlockState().drops()) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
            }
            ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock());
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onBelowBlockBreak(BlockBreakEvent event) {
            if (event.isCancelled()) return;
            if (!NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(event.getBlock().getRelative(BlockFace.UP))) return;
            if (!(NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlock(event.getBlock().getRelative(BlockFace.UP)) instanceof ArtisanThinBlockData artisanThinBlockData)) return;
            event.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
            for (ItemStack drop : artisanThinBlockData.getArtisanBlockState().drops()) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getRelative(BlockFace.UP).getLocation(), drop);
            }
            ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock().getRelative(BlockFace.UP));
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onBelowBlockPistonBreakOrMove(BlockPistonExtendEvent event) {
            if (event.isCancelled()) return;
            for (Block block : event.getBlocks()) {
                if (!NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(block.getRelative(BlockFace.UP))) continue;
                if (!(NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlock(block.getRelative(BlockFace.UP)) instanceof ArtisanThinBlockData artisanThinBlockData)) continue;
                block.getRelative(BlockFace.UP).setType(Material.AIR);
                for (ItemStack drop : artisanThinBlockData.getArtisanBlockState().drops()) {
                    block.getWorld().dropItemNaturally(block.getRelative(BlockFace.UP).getLocation(), drop);
                }
                ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(block.getRelative(BlockFace.UP));
            }
        }

        @EventHandler(priority = EventPriority.LOWEST)
        private static void onWaterOrPistonBreak(BlockBreakBlockEvent event) {
            if (!NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(event.getBlock())) return;
            if (!(NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlock(event.getBlock()) instanceof ArtisanThinBlockData artisanThinBlockData)) return;
            while (!event.getDrops().isEmpty()) event.getDrops().removeFirst();
            for (ItemStack drop : artisanThinBlockData.getArtisanBlockState().drops()) {
                event.getDrops().add(drop);
            }
            ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock());
        }

        @EventHandler(priority = EventPriority.LOWEST)
        private static void onStep(BlockRedstoneEvent event) {
            if (!NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(event.getBlock())) return;
            if (!(NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlock(event.getBlock()) instanceof ArtisanThinBlockData)) return;
            event.setNewCurrent(0);
        }

        private static void place(Block bukkitBlock, ArtisanThinBlockData artisanThinBlockData) throws Exception {
            CraftWorld craftWorld = (CraftWorld) bukkitBlock.getWorld();
            Level nmsWorld = craftWorld.getHandle();
            BlockPos pos = new BlockPos(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ());
            ArtisanBlockStorageInternal.getInternal().placeArtisanBlock(nmsWorld, pos, artisanThinBlockData);
            nmsWorld.setBlock(pos, stateById(artisanThinBlockData.getArtisanBlockState().actualState()), 3);
        }

        private static void replace(Block bukkitBlock, ArtisanThinBlockData artisanThinBlockData) {
            CraftWorld craftWorld = (CraftWorld) bukkitBlock.getWorld();
            Level nmsWorld = craftWorld.getHandle();
            BlockPos pos = new BlockPos(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ());
            ArtisanBlockStorageInternal.getInternal().replaceArtisanBlock(nmsWorld, pos, artisanThinBlockData);
            nmsWorld.setBlock(pos, stateById(artisanThinBlockData.getArtisanBlockState().actualState()), 3);
        }

    }
}
