package io.github.moyusowo.neoartisan.block.packetblock;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.storage.internal.ArtisanBlockStorageInternal;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisanapi.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCrop;
import io.github.moyusowo.neoartisanapi.api.block.event.ArtisanBlockPlaceEvent;
import io.github.moyusowo.neoartisanapi.api.block.packetblock.ArtisanPacketBlock;
import io.github.moyusowo.neoartisanapi.api.block.packetblock.ArtisanPacketBlockData;
import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import static io.github.moyusowo.neoartisan.block.util.BlockStateUtil.stateById;

final class ArtisanPacketBlockBehavior implements Listener {
    private ArtisanPacketBlockBehavior() {}

    @InitMethod
    static void init() {
        NeoArtisan.registerListener(new ArtisanPacketBlockBehavior());
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
        if (!(NeoArtisanAPI.getBlockRegistry().getArtisanBlock(artisanItem.getBlockId()) instanceof ArtisanPacketBlock)) return;
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
                ArtisanPacketBlockData.builder()
                        .blockId(artisanItem.getBlockId())
                        .stage(NeoArtisanAPI.getBlockRegistry().getArtisanBlock(artisanItem.getBlockId()).getDefaultState())
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
        if (!(NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlock(event.getBlock()) instanceof ArtisanPacketBlockData artisanPacketBlockData)) return;
        if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
            ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock());
            return;
        }
        event.setCancelled(true);
        event.getPlayer().giveExp(event.getExpToDrop());
        event.getBlock().setType(Material.AIR);
        for (ItemStack drop : artisanPacketBlockData.getArtisanBlockState().drops()) {
            event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
        }
        ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock());
    }

    @EventHandler
    private static void onDamage(BlockDamageEvent event) {
        NeoArtisan.logger().info("speed: " + event.getBlock().getDestroySpeed(event.getItemInHand(), true));
    }

    private static void place(Block bukkitBlock, ArtisanPacketBlockData artisanCropData) throws Exception {
        CraftWorld craftWorld = (CraftWorld) bukkitBlock.getWorld();
        Level nmsWorld = craftWorld.getHandle();
        BlockPos pos = new BlockPos(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ());
        ArtisanBlockStorageInternal.getInternal().placeArtisanBlock(nmsWorld, pos, artisanCropData);
        nmsWorld.setBlock(pos, stateById(artisanCropData.getArtisanBlockState().actualState()), 3);
    }

    private static void replace(Block bukkitBlock, ArtisanPacketBlockData artisanCropData) {
        CraftWorld craftWorld = (CraftWorld) bukkitBlock.getWorld();
        Level nmsWorld = craftWorld.getHandle();
        BlockPos pos = new BlockPos(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ());
        ArtisanBlockStorageInternal.getInternal().replaceArtisanBlock(nmsWorld, pos, artisanCropData);
        nmsWorld.setBlock(pos, stateById(artisanCropData.getArtisanBlockState().actualState()), 3);
    }

}
