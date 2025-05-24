package io.github.moyusowo.neoartisan.block.transparent;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.storage.internal.ArtisanBlockStorageInternal;
import io.github.moyusowo.neoartisan.block.util.InteractionUtil;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockBase;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockState;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCropData;
import io.github.moyusowo.neoartisanapi.api.block.event.ArtisanBlockPlaceEvent;
import io.github.moyusowo.neoartisanapi.api.block.transparent.ArtisanTransparentBlock;
import io.github.moyusowo.neoartisanapi.api.block.transparent.ArtisanTransparentBlockData;
import io.github.moyusowo.neoartisanapi.api.block.transparent.ArtisanTransparentBlockState;
import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static io.github.moyusowo.neoartisan.block.util.BlockStateUtil.stateById;

class ArtisanTransparentBlockImpl extends ArtisanBlockBase implements ArtisanTransparentBlock {

    @InitMethod(order = InitPriority.HIGH)
    private static void init() {
        Bukkit.getServicesManager().register(
                Builder.class,
                new BuilderImpl(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private final boolean canBurn;

    protected ArtisanTransparentBlockImpl(NamespacedKey blockId, List<? extends ArtisanBlockState> stages, boolean canBurn) {
        super(blockId, stages);
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

        public BuilderImpl() {
            blockId = null;
            stages = null;
            canBurn = false;
        }

        @Override
        public Builder blockId(NamespacedKey blockId) {
            this.blockId = blockId;
            return this;
        }

        @Override
        public Builder states(List<ArtisanTransparentBlockState> states) {
            this.stages = states;
            return this;
        }

        @Override
        public Builder canBurn(boolean canBurn) {
            this.canBurn = canBurn;
            return this;
        }

        @Override
        public ArtisanTransparentBlock build() {
            if (blockId == null || stages == null) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanTransparentBlockImpl(blockId, stages, canBurn);
        }
    }

    public static final class ArtisanTransparentBlockBehavior implements Listener {

        private ArtisanTransparentBlockBehavior() {}

        @InitMethod
        static void init() {
            NeoArtisan.registerListener(new ArtisanTransparentBlockBehavior());
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onPlace(PlayerInteractEvent event) throws Exception {
            if (event.useInteractedBlock() == Event.Result.DENY) return;
            if (event.useItemInHand() == Event.Result.DENY) return;
            if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
            if (!NeoArtisanAPI.getItemRegistry().isArtisanItem(event.getItem())) return;
            ArtisanItem artisanItem = NeoArtisanAPI.getItemRegistry().getArtisanItem(event.getItem());
            if (artisanItem.getBlockId() == null) return;
            if (!NeoArtisanAPI.getBlockRegistry().isArtisanBlock(artisanItem.getBlockId())) return;
            if (!(NeoArtisanAPI.getBlockRegistry().getArtisanBlock(artisanItem.getBlockId()) instanceof ArtisanTransparentBlock)) return;
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
                    ArtisanTransparentBlockData.builder()
                            .blockId(artisanItem.getBlockId())
                            .stage(0)
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
            if (!(NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlock(event.getBlock()) instanceof ArtisanTransparentBlockData artisanTransparentBlockData)) return;
            if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
                ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock());
                return;
            }
            event.setCancelled(true);
            event.getPlayer().giveExp(event.getExpToDrop());
            event.getBlock().setType(Material.AIR);
            for (ItemStack drop : artisanTransparentBlockData.getArtisanBlockState().drops()) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
            }
            ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock());
        }

        @EventHandler
        private static void onBurn(BlockBurnEvent event) {
            if (event.isCancelled()) return;
            if (!NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(event.getBlock())) return;
            if (!(NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlock(event.getBlock()) instanceof ArtisanTransparentBlockData artisanTransparentBlockData)) return;
            if (artisanTransparentBlockData.getArtisanBlock().canBurn()) return;
            event.setCancelled(true);
        }

        @EventHandler(priority = EventPriority.LOWEST)
        private static void onPistonBreak(BlockBreakBlockEvent event) {
            if (!NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(event.getBlock())) return;
            if (!(NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlock(event.getBlock()) instanceof ArtisanTransparentBlockData artisanTransparentBlockData)) return;
            while (!event.getDrops().isEmpty()) event.getDrops().removeFirst();
            for (ItemStack drop : artisanTransparentBlockData.getArtisanBlockState().drops()) {
                event.getDrops().add(drop);
            }
            ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock());
        }

        private static void place(Block bukkitBlock, ArtisanTransparentBlockData artisanCropData) throws Exception {
            CraftWorld craftWorld = (CraftWorld) bukkitBlock.getWorld();
            Level nmsWorld = craftWorld.getHandle();
            BlockPos pos = new BlockPos(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ());
            ArtisanBlockStorageInternal.getInternal().placeArtisanBlock(nmsWorld, pos, artisanCropData);
            nmsWorld.setBlock(pos, stateById(artisanCropData.getArtisanBlockState().actualState()), 3);
        }

        private static void replace(Block bukkitBlock, ArtisanTransparentBlockData artisanCropData) {
            CraftWorld craftWorld = (CraftWorld) bukkitBlock.getWorld();
            Level nmsWorld = craftWorld.getHandle();
            BlockPos pos = new BlockPos(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ());
            ArtisanBlockStorageInternal.getInternal().replaceArtisanBlock(nmsWorld, pos, artisanCropData);
            nmsWorld.setBlock(pos, stateById(artisanCropData.getArtisanBlockState().actualState()), 3);
        }
    }
}
