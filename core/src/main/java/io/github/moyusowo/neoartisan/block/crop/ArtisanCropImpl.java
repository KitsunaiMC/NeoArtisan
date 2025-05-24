package io.github.moyusowo.neoartisan.block.crop;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.storage.internal.ArtisanBlockStorageInternal;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockBase;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlock;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCrop;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCropData;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCropState;
import io.github.moyusowo.neoartisanapi.api.block.event.ArtisanBlockPlaceEvent;
import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static io.github.moyusowo.neoartisan.block.util.BlockStateUtil.stateById;

class ArtisanCropImpl extends ArtisanBlockBase implements ArtisanCrop {

    @InitMethod(order = InitPriority.HIGH)
    private static void init() {
        Bukkit.getServicesManager().register(
                Builder.class,
                new BuilderImpl(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private final int boneMealMinGrowth, boneMealMaxGrowth;

    public ArtisanCropImpl(NamespacedKey cropId, List<ArtisanCropState> stages, int boneMealMinGrowth, int boneMealMaxGrowth) {
        super(cropId, stages, null);
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

        public BuilderImpl() {
            blockId = null;
            stages = null;
            boneMealMaxGrowth = -1;
            boneMealMinGrowth = -1;
        }

        @Override
        public Builder blockId(NamespacedKey blockId) {
            this.blockId = blockId;
            return this;
        }

        @Override
        public Builder stages(List<ArtisanCropState> stages) {
            this.stages = stages;
            return this;
        }

        @Override
        public Builder boneMealMinGrowth(int boneMealMinGrowth) {
            this.boneMealMinGrowth = boneMealMinGrowth;
            return this;
        }

        @Override
        public Builder boneMealMaxGrowth(int boneMealMaxGrowth) {
            this.boneMealMaxGrowth = boneMealMaxGrowth;
            return this;
        }

        @Override
        public ArtisanBlock build() {
            if (blockId == null || stages == null || boneMealMinGrowth == -1 || boneMealMaxGrowth == -1) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanCropImpl(blockId, stages, boneMealMinGrowth, boneMealMaxGrowth);
        }
    }

    public static final class ArtisanCropBehavior implements Listener {

        private static final HashMap<Block, ArtisanCropData> grownCrop = new HashMap<>();

        private ArtisanCropBehavior() {}

        @InitMethod
        static void init() {
            NeoArtisan.registerListener(new ArtisanCropBehavior());
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
            if (!(NeoArtisanAPI.getBlockRegistry().getArtisanBlock(artisanItem.getBlockId()) instanceof ArtisanCrop)) return;
            if (event.getClickedBlock().getType() != Material.FARMLAND) return;
            if (event.getBlockFace() != BlockFace.UP) return;
            if (event.getClickedBlock().getRelative(BlockFace.UP).getType() != Material.AIR) return;
            event.setCancelled(true);
            ArtisanBlockPlaceEvent artisanBlockPlaceEvent = new ArtisanBlockPlaceEvent(
                    event.getClickedBlock().getRelative(BlockFace.UP),
                    event.getClickedBlock().getRelative(BlockFace.UP).getState(),
                    event.getClickedBlock(),
                    event.getItem(),
                    event.getPlayer(),
                    true,
                    EquipmentSlot.HAND,
                    NeoArtisanAPI.getBlockRegistry().getArtisanBlock(artisanItem.getBlockId())
            );
            Bukkit.getPluginManager().callEvent(artisanBlockPlaceEvent);
            if (artisanBlockPlaceEvent.isCancelled()) return;
            place(event.getClickedBlock().getRelative(BlockFace.UP), new ArtisanCropDataImpl(artisanItem.getBlockId(), 0, event.getClickedBlock().getRelative(BlockFace.UP).getLocation()));
            if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
                event.getItem().setAmount(event.getItem().getAmount() - 1);
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onBreak(BlockBreakEvent event) {
            if (event.isCancelled()) return;
            if (!NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(event.getBlock())) return;
            if (!(NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlock(event.getBlock()) instanceof ArtisanCropData artisanCropData)) return;
            if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
                ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock());
                return;
            }
            event.setCancelled(true);
            event.getPlayer().giveExp(event.getExpToDrop());
            event.getBlock().setType(Material.AIR);
            for (ItemStack drop : artisanCropData.getArtisanBlockState().drops()) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
            }
            ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock());
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onBlockBreakUnderCrop(BlockBreakEvent event) {
            if (event.isCancelled()) return;
            if (!NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(event.getBlock().getRelative(BlockFace.UP))) return;
            if (!(NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlock(event.getBlock().getRelative(BlockFace.UP)) instanceof ArtisanCropData artisanCropData)) return;
            event.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
            for (ItemStack drop : artisanCropData.getArtisanBlockState().drops()) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getRelative(BlockFace.UP).getLocation(), drop);
            }
            ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock().getRelative(BlockFace.UP));
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onPistonBreakOrMoveUnderCrop(BlockPistonExtendEvent event) {
            if (event.isCancelled()) return;
            for (Block block : event.getBlocks()) {
                if (!NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(block.getRelative(BlockFace.UP))) continue;
                if (!(NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlock(block.getRelative(BlockFace.UP)) instanceof ArtisanCropData artisanCropData)) continue;
                block.getRelative(BlockFace.UP).setType(Material.AIR);
                for (ItemStack drop : artisanCropData.getArtisanBlockState().drops()) {
                    block.getWorld().dropItemNaturally(block.getRelative(BlockFace.UP).getLocation(), drop);
                }
                ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(block.getRelative(BlockFace.UP));
            }
        }

        @EventHandler(priority = EventPriority.LOWEST)
        private static void onWaterOrPistonBreak(BlockBreakBlockEvent event) {
            if (!NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(event.getBlock())) return;
            if (!(NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlock(event.getBlock()) instanceof ArtisanCropData artisanCropData)) return;
            while (!event.getDrops().isEmpty()) event.getDrops().removeFirst();
            for (ItemStack drop : artisanCropData.getArtisanBlockState().drops()) {
                event.getDrops().add(drop);
            }
            ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock());
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onEntityChangeFarmland(EntityChangeBlockEvent event) {
            if (event.isCancelled()) return;
            if (!NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(event.getBlock().getRelative(BlockFace.UP))) return;
            if (!(NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlock(event.getBlock().getRelative(BlockFace.UP)) instanceof ArtisanCropData artisanCropData)) return;
            if (event.getBlock().getType() != Material.FARMLAND) return;
            event.setCancelled(true);
            event.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
            event.getBlock().setType(Material.DIRT);
            for (ItemStack drop: artisanCropData.getArtisanBlockState().drops()) {
                event.getBlock().getWorld().dropItemNaturally(event.getBlock().getRelative(BlockFace.UP).getLocation(), drop);
            }
            ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock().getRelative(BlockFace.UP));
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onGrow(BlockGrowEvent event) {
            if (event.isCancelled()) return;
            if (!NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(event.getBlock())) return;
            if (!(NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlock(event.getBlock()) instanceof ArtisanCropData artisanCropData)) return;
            event.setCancelled(true);
            if (artisanCropData.hasNextStage()) {
                grownCrop.put(event.getBlock(), artisanCropData);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        grownCrop.remove(event.getBlock());
                    }
                }.runTaskLater(NeoArtisan.instance(), 0L);
                replace(event.getBlock(), artisanCropData.getNextStage());
            }
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        private static void onFertilize(BlockFertilizeEvent event) {
            if (event.isCancelled()) return;
            for (BlockState blockState : event.getBlocks()) {
                if (NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(blockState.getBlock())) {
                    Object object = NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlock(blockState.getBlock());
                    if (object instanceof ArtisanCropData) {
                        ArtisanCropData artisanCropData = grownCrop.get(blockState.getBlock());
                        replace(blockState.getBlock(), artisanCropData);
                        grownCrop.remove(blockState.getBlock());
                        event.setCancelled(true);
                        if (artisanCropData.hasNextStage()) {
                            replace(blockState.getBlock(), artisanCropData.getNextFertilizeStage());
                            playBoneMealEffects(blockState.getLocation());
                        }
                    }
                }
            }
        }

        private static void place(Block bukkitBlock, ArtisanCropData artisanCropData) throws Exception {
            CraftWorld craftWorld = (CraftWorld) bukkitBlock.getWorld();
            Level nmsWorld = craftWorld.getHandle();
            BlockPos pos = new BlockPos(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ());
            ArtisanBlockStorageInternal.getInternal().placeArtisanBlock(nmsWorld, pos, artisanCropData);
            nmsWorld.setBlock(pos, stateById(artisanCropData.getArtisanBlockState().actualState()), 3);
        }

        private static void replace(Block bukkitBlock, ArtisanCropData artisanCropData) {
            CraftWorld craftWorld = (CraftWorld) bukkitBlock.getWorld();
            Level nmsWorld = craftWorld.getHandle();
            BlockPos pos = new BlockPos(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ());
            ArtisanBlockStorageInternal.getInternal().replaceArtisanBlock(nmsWorld, pos, artisanCropData);
            nmsWorld.setBlock(pos, stateById(artisanCropData.getArtisanBlockState().actualState()), 3);
        }

        private static void playBoneMealEffects(Location loc) {
            World world = loc.getWorld();
            world.spawnParticle(
                    Particle.HAPPY_VILLAGER,
                    loc.clone().add(0.5, 0.5, 0.5),
                    20, 0.3, 0.3, 0.3, 0.5
            );
            for (int i = 0; i < 5; i++) {
                world.spawnParticle(
                        Particle.HAPPY_VILLAGER,
                        loc.clone().add(
                                0.5 + ThreadLocalRandom.current().nextGaussian() * 0.3,
                                0.1,
                                0.5 + ThreadLocalRandom.current().nextGaussian() * 0.3
                        ),
                        2, 0, 0.1, 0, 0.2
                );
            }
            world.playSound(loc, Sound.ITEM_BONE_MEAL_USE, 1.0f, 1.2f);
        }
    }

}
