package io.github.moyusowo.neoartisan.block.state.listener;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.storage.internal.ArtisanBlockStorageInternal;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.event.common.ArtisanBlockBreakEvent;
import io.github.moyusowo.neoartisanapi.api.block.event.common.ArtisanBlockLoseSupportEvent;
import io.github.moyusowo.neoartisanapi.api.block.protection.Protections;
import io.github.moyusowo.neoartisanapi.api.block.storage.Storages;
import io.github.moyusowo.neoartisanapi.api.block.util.PistonMoveBlockReaction;
import io.papermc.paper.event.block.BlockBreakBlockEvent;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

final class BaseStateListener implements Listener {
    private BaseStateListener() {}

    @InitMethod(priority = InitPriority.LISTENER)
    public static void init() {
        NeoArtisan.registerListener(new BaseStateListener());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerBreak(BlockBreakEvent event) {
        // if ArtisanBlock
        if (!Storages.BLOCK.isArtisanBlock(event.getBlock())) return;
        // check player permission
        if (!Protections.BLOCK.canBreak(event.getPlayer(), event.getBlock().getLocation())) return;
        ArtisanBlockData artisanBlockData = Storages.BLOCK.getArtisanBlockData(event.getBlock());
        event.setCancelled(true);
        ArtisanBlockBreakEvent artisanBlockBreakEvent = new ArtisanBlockBreakEvent(
                event.getBlock(),
                event.getPlayer(),
                artisanBlockData.getArtisanBlock()
        );
        artisanBlockBreakEvent.setExpToDrop(event.getExpToDrop());
        artisanBlockBreakEvent.callEvent();
        if (artisanBlockBreakEvent.isCancelled()) return;
        if (artisanBlockBreakEvent.getExpToDrop() > 0) {
            ExperienceOrb orb = (ExperienceOrb) event.getBlock().getWorld().spawnEntity(
                    event.getBlock().getLocation(),
                    EntityType.EXPERIENCE_ORB
            );
            orb.setExperience(artisanBlockBreakEvent.getExpToDrop());
        }
        event.getBlock().setType(Material.AIR);
        if (event.getPlayer().getGameMode() != GameMode.CREATIVE) {
            if (artisanBlockBreakEvent.isDropItems()) {
                for (ItemStack drop : artisanBlockData.getArtisanBlockState().drops()) {
                    event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation().add(0.5, 0.5, 0.5), drop);
                }
            }
        }
        ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerBreakUnder(BlockBreakEvent event) {
        final Block upsideBlock = event.getBlock().getRelative(BlockFace.UP);
        // if UP is ArtisanBlock
        if (!Storages.BLOCK.isArtisanBlock(upsideBlock)) return;
        // check player permission
        if (!Protections.BLOCK.canBreak(event.getPlayer(), upsideBlock.getLocation())) return;
        // check if state can float
        ArtisanBlockData artisanBlockData = Storages.BLOCK.getArtisanBlockData(upsideBlock);
        if (artisanBlockData.getArtisanBlockState().canSurviveFloating()) return;
        upsideBlock.setType(Material.AIR);
        ArtisanBlockLoseSupportEvent artisanBlockLoseSupportEvent = new ArtisanBlockLoseSupportEvent(
                upsideBlock,
                artisanBlockData.getArtisanBlock()
        );
        artisanBlockLoseSupportEvent.callEvent();
        if (artisanBlockLoseSupportEvent.getExpToDrop() > 0) {
            ExperienceOrb orb = (ExperienceOrb) upsideBlock.getWorld().spawnEntity(
                    upsideBlock.getLocation(),
                    EntityType.EXPERIENCE_ORB
            );
            orb.setExperience(artisanBlockLoseSupportEvent.getExpToDrop());
        }
        if (artisanBlockLoseSupportEvent.isDropItems()) {
            for (ItemStack drop : artisanBlockData.getArtisanBlockState().drops()) {
                upsideBlock.getWorld().dropItemNaturally(upsideBlock.getLocation().add(0.5, 0.5, 0.5), drop);
            }
        }
        ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(upsideBlock);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockBreak(BlockBreakBlockEvent event) {
        final Block upsideBlock = event.getBlock().getRelative(BlockFace.UP);
        // check if ArtisanBlock
        if (Storages.BLOCK.isArtisanBlock(event.getBlock())) {
            ArtisanBlockData artisanBlockData = Storages.BLOCK.getArtisanBlockData(event.getBlock());
            event.getDrops().clear();
            for (ItemStack drop : artisanBlockData.getArtisanBlockState().drops()) {
                event.getDrops().add(drop);
            }
            ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock());
        }
        // check if up ArtisanBlock
        else if (Storages.BLOCK.isArtisanBlock(upsideBlock)) {
            // check if state can float
            ArtisanBlockData artisanBlockData = Storages.BLOCK.getArtisanBlockData(upsideBlock);
            if (artisanBlockData.getArtisanBlockState().canSurviveFloating()) return;
            upsideBlock.setType(Material.AIR);
            ArtisanBlockLoseSupportEvent artisanBlockLoseSupportEvent = new ArtisanBlockLoseSupportEvent(
                    upsideBlock,
                    artisanBlockData.getArtisanBlock()
            );
            artisanBlockLoseSupportEvent.callEvent();
            if (artisanBlockLoseSupportEvent.getExpToDrop() > 0) {
                ExperienceOrb orb = (ExperienceOrb) upsideBlock.getWorld().spawnEntity(
                        upsideBlock.getLocation(),
                        EntityType.EXPERIENCE_ORB
                );
                orb.setExperience(artisanBlockLoseSupportEvent.getExpToDrop());
            }
            if (artisanBlockLoseSupportEvent.isDropItems()) {
                for (ItemStack drop : artisanBlockData.getArtisanBlockState().drops()) {
                    upsideBlock.getWorld().dropItemNaturally(upsideBlock.getLocation().add(0.5, 0.5, 0.5), drop);
                }
            }
            ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(upsideBlock);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockExplode(BlockExplodeEvent event) {
        final List<Block> artisanBlocks = new ArrayList<>();
        final List<Block> affectedBlocks = new ArrayList<>();
        final Iterator<Block> iterator = event.blockList().iterator();
        while (iterator.hasNext()) {
            Block block = iterator.next();
            if (!Storages.BLOCK.isArtisanBlock(block)) {
                if (Storages.BLOCK.isArtisanBlock(block.getRelative(BlockFace.UP))) {
                    affectedBlocks.add(block.getRelative(BlockFace.UP));
                }
                continue;
            }
            artisanBlocks.add(block);
            iterator.remove();
        }
        for (Block affectedBlock : affectedBlocks) {
            ArtisanBlockData artisanBlockData = Storages.BLOCK.getArtisanBlockData(affectedBlock);
            if (artisanBlockData.getArtisanBlockState().canSurviveFloating()) return;
            affectedBlock.setType(Material.AIR);
            ArtisanBlockLoseSupportEvent artisanBlockLoseSupportEvent = new ArtisanBlockLoseSupportEvent(
                    affectedBlock,
                    artisanBlockData.getArtisanBlock()
            );
            artisanBlockLoseSupportEvent.callEvent();
            if (artisanBlockLoseSupportEvent.getExpToDrop() > 0) {
                ExperienceOrb orb = (ExperienceOrb) affectedBlock.getWorld().spawnEntity(
                        affectedBlock.getLocation(),
                        EntityType.EXPERIENCE_ORB
                );
                orb.setExperience(artisanBlockLoseSupportEvent.getExpToDrop());
            }
            if (artisanBlockLoseSupportEvent.isDropItems()) {
                for (ItemStack drop : artisanBlockData.getArtisanBlockState().drops()) {
                    affectedBlock.getWorld().dropItemNaturally(affectedBlock.getLocation().add(0.5, 0.5, 0.5), drop);
                }
            }
            ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(affectedBlock);
        }
        for (Block artisanBlock : artisanBlocks) {
            ArtisanBlockData artisanBlockData = Storages.BLOCK.getArtisanBlockData(artisanBlock);
            if (ThreadLocalRandom.current().nextDouble() < event.getYield()) {
                for (ItemStack drop : artisanBlockData.getArtisanBlockState().drops()) {
                    artisanBlock.getWorld().dropItemNaturally(artisanBlock.getLocation().add(0.5, 0.5, 0.5), drop);
                }
            }
            artisanBlock.setType(Material.AIR);
            ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(artisanBlock);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityExplode(EntityExplodeEvent event) {
        final List<Block> artisanBlocks = new ArrayList<>();
        final List<Block> affectedBlocks = new ArrayList<>();
        final Iterator<Block> iterator = event.blockList().iterator();
        while (iterator.hasNext()) {
            Block block = iterator.next();
            if (!Storages.BLOCK.isArtisanBlock(block)) {
                if (Storages.BLOCK.isArtisanBlock(block.getRelative(BlockFace.UP))) {
                    affectedBlocks.add(block.getRelative(BlockFace.UP));
                }
                continue;
            }
            artisanBlocks.add(block);
            iterator.remove();
        }
        for (Block affectedBlock : affectedBlocks) {
            ArtisanBlockData artisanBlockData = Storages.BLOCK.getArtisanBlockData(affectedBlock);
            if (artisanBlockData.getArtisanBlockState().canSurviveFloating()) return;
            affectedBlock.setType(Material.AIR);
            ArtisanBlockLoseSupportEvent artisanBlockLoseSupportEvent = new ArtisanBlockLoseSupportEvent(
                    affectedBlock,
                    artisanBlockData.getArtisanBlock()
            );
            artisanBlockLoseSupportEvent.callEvent();
            if (artisanBlockLoseSupportEvent.getExpToDrop() > 0) {
                ExperienceOrb orb = (ExperienceOrb) affectedBlock.getWorld().spawnEntity(
                        affectedBlock.getLocation(),
                        EntityType.EXPERIENCE_ORB
                );
                orb.setExperience(artisanBlockLoseSupportEvent.getExpToDrop());
            }
            if (artisanBlockLoseSupportEvent.isDropItems()) {
                for (ItemStack drop : artisanBlockData.getArtisanBlockState().drops()) {
                    affectedBlock.getWorld().dropItemNaturally(affectedBlock.getLocation().add(0.5, 0.5, 0.5), drop);
                }
            }
            ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(affectedBlock);
        }
        for (Block artisanBlock : artisanBlocks) {
            ArtisanBlockData artisanBlockData = Storages.BLOCK.getArtisanBlockData(artisanBlock);
            if (ThreadLocalRandom.current().nextDouble() < event.getYield()) {
                for (ItemStack drop : artisanBlockData.getArtisanBlockState().drops()) {
                    artisanBlock.getWorld().dropItemNaturally(artisanBlock.getLocation().add(0.5, 0.5, 0.5), drop);
                }
            }
            artisanBlock.setType(Material.AIR);
            ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(artisanBlock);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        if (Storages.BLOCK.isArtisanBlock(event.getBlock())) {
            if (event.getEntityType() == EntityType.ENDERMAN) {
                event.setCancelled(true);
            } else if (event.getEntityType() == EntityType.WITHER) {
                event.getBlock().setType(Material.AIR);
                ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock());
            }
        } else if (Storages.BLOCK.isArtisanBlock(event.getBlock().getRelative(BlockFace.UP))) {
            ArtisanBlockData artisanBlockData = Storages.BLOCK.getArtisanBlockData(event.getBlock().getRelative(BlockFace.UP));
            if (artisanBlockData.getArtisanBlockState().canSurviveFloating()) return;
            if (event.getEntityType() == EntityType.ENDERMAN) {
                event.setCancelled(true);
            } else if (event.getEntityType() == EntityType.WITHER) {
                event.getBlock().setType(Material.AIR);
                ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(event.getBlock());
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            if (!Storages.BLOCK.isArtisanBlock(block)) continue;
            ArtisanBlockData artisanBlockData = Storages.BLOCK.getArtisanBlockData(block);
            if (artisanBlockData.getArtisanBlockState().pistonMoveReaction() == PistonMoveBlockReaction.RESIST || artisanBlockData.getArtisanBlock().hasBlockEntity()) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPistonExtendUnder(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            final Block upsideBlock = block.getRelative(BlockFace.UP);
            if (!Storages.BLOCK.isArtisanBlock(upsideBlock)) continue;
            ArtisanBlockData artisanBlockData = Storages.BLOCK.getArtisanBlockData(upsideBlock);
            if (artisanBlockData.getArtisanBlockState().canSurviveFloating()) continue;
            upsideBlock.setType(Material.AIR);
            ArtisanBlockLoseSupportEvent artisanBlockLoseSupportEvent = new ArtisanBlockLoseSupportEvent(
                    upsideBlock,
                    artisanBlockData.getArtisanBlock()
            );
            artisanBlockLoseSupportEvent.callEvent();
            if (artisanBlockLoseSupportEvent.getExpToDrop() > 0) {
                ExperienceOrb orb = (ExperienceOrb) upsideBlock.getWorld().spawnEntity(
                        upsideBlock.getLocation(),
                        EntityType.EXPERIENCE_ORB
                );
                orb.setExperience(artisanBlockLoseSupportEvent.getExpToDrop());
            }
            if (artisanBlockLoseSupportEvent.isDropItems()) {
                for (ItemStack drop : artisanBlockData.getArtisanBlockState().drops()) {
                    upsideBlock.getWorld().dropItemNaturally(upsideBlock.getLocation().add(0.5, 0.5, 0.5), drop);
                }
            }
            ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(upsideBlock);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        for (Block block : event.getBlocks()) {
            if (!Storages.BLOCK.isArtisanBlock(block)) continue;
            ArtisanBlockData artisanBlockData = Storages.BLOCK.getArtisanBlockData(block);
            if (artisanBlockData.getArtisanBlockState().pistonMoveReaction() == PistonMoveBlockReaction.BREAK) continue;
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPistonRetractUnder(BlockPistonRetractEvent event) {
        for (Block block : event.getBlocks()) {
            final Block upsideBlock = block.getRelative(BlockFace.UP);
            if (!Storages.BLOCK.isArtisanBlock(upsideBlock)) continue;
            ArtisanBlockData artisanBlockData = Storages.BLOCK.getArtisanBlockData(upsideBlock);
            if (artisanBlockData.getArtisanBlockState().canSurviveFloating()) continue;
            upsideBlock.setType(Material.AIR);
            ArtisanBlockLoseSupportEvent artisanBlockLoseSupportEvent = new ArtisanBlockLoseSupportEvent(
                    upsideBlock,
                    artisanBlockData.getArtisanBlock()
            );
            artisanBlockLoseSupportEvent.callEvent();
            if (artisanBlockLoseSupportEvent.getExpToDrop() > 0) {
                ExperienceOrb orb = (ExperienceOrb) upsideBlock.getWorld().spawnEntity(
                        upsideBlock.getLocation(),
                        EntityType.EXPERIENCE_ORB
                );
                orb.setExperience(artisanBlockLoseSupportEvent.getExpToDrop());
            }
            if (artisanBlockLoseSupportEvent.isDropItems()) {
                for (ItemStack drop : artisanBlockData.getArtisanBlockState().drops()) {
                    upsideBlock.getWorld().dropItemNaturally(upsideBlock.getLocation().add(0.5, 0.5, 0.5), drop);
                }
            }
            ArtisanBlockStorageInternal.getInternal().removeArtisanBlock(upsideBlock);
        }
    }
}
