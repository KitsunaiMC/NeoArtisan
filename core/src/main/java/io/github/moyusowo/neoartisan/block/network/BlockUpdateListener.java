package io.github.moyusowo.neoartisan.block.network;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.attribute.Attributes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChunkData;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerMultiBlockChange;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes;
import io.github.moyusowo.neoartisan.block.state.listener.BlockBreakSynchronize;
import io.github.moyusowo.neoartisan.block.storage.internal.ArtisanBlockStorageAsync;
import io.github.moyusowo.neoartisan.block.util.BlockPos;
import io.github.moyusowo.neoartisan.block.util.ChunkPos;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;

import java.util.UUID;

final class BlockUpdateListener implements PacketListener {

    @InitMethod(priority = InitPriority.DEFAULT)
    static void init() {
        PacketEvents.getAPI().getEventManager().registerListener(
                new BlockUpdateListener(), PacketListenerPriority.NORMAL
        );
    }

    private BlockUpdateListener() {}

    @Override
    public void onPacketSend(PacketSendEvent event) {
        switch (event.getPacketType()) {
            case PacketType.Play.Server.CHUNK_DATA -> onChunkDataSend(event);
            case PacketType.Play.Server.BLOCK_CHANGE -> onSingleBlockChange(event);
            case PacketType.Play.Server.MULTI_BLOCK_CHANGE -> onMultiBlockChange(event);
            case PacketType.Play.Server.UPDATE_ATTRIBUTES -> {
                WrapperPlayServerUpdateAttributes wrapperPlayServerUpdateAttributes = new WrapperPlayServerUpdateAttributes(event);
                if (wrapperPlayServerUpdateAttributes.getProperties().stream().anyMatch(property -> property.getAttribute() == Attributes.BLOCK_BREAK_SPEED)) {
                    if (wrapperPlayServerUpdateAttributes.getProperties().stream().anyMatch(property -> property.getModifiers().stream().anyMatch(propertyModifier -> propertyModifier.getName().getNamespace().equals(BlockBreakSynchronize.TEMPLATE_HIDDEN_MULTIPLY.getNamespace()) && propertyModifier.getName().getKey().equals(BlockBreakSynchronize.TEMPLATE_HIDDEN_MULTIPLY.getKey())))) {
                        event.setCancelled(true);
                    }
                }
            }
            default -> {}
        }
    }

    private void onChunkDataSend(PacketSendEvent event) {
        final UUID playerUUID = event.getUser().getUUID();
        final UUID worldUID = AsyncPlayerWorld.getPlayerWorld(playerUUID);
        final int minHeight = event.getUser().getMinWorldHeight();
        final WrapperPlayServerChunkData wrapperPlayServerChunkData = new WrapperPlayServerChunkData(event);
        final int chunkX = wrapperPlayServerChunkData.getColumn().getX(), chunkZ = wrapperPlayServerChunkData.getColumn().getZ();
        if (!ArtisanBlockStorageAsync.getAsync().hasArtisanBlockInChunk(new ChunkPos(worldUID, chunkX, chunkZ))) return;
        final BaseChunk[] baseChunks = wrapperPlayServerChunkData.getColumn().getChunks();
        for (int sectionIndex = 0; sectionIndex < baseChunks.length; sectionIndex++) {
            final BaseChunk baseChunk = baseChunks[sectionIndex];
            for (int sectionX = 0; sectionX < 16; sectionX++) {
                for (int sectionY = 0; sectionY < 16; sectionY++) {
                    for (int sectionZ = 0; sectionZ < 16; sectionZ++) {
                        final int x = (chunkX << 4) + sectionX;
                        final int y = (sectionIndex << 4) + sectionY + minHeight;
                        final int z = (chunkZ << 4) + sectionZ;
                        final int blockStateId = baseChunk.getBlockId(sectionX, sectionY, sectionZ);
                        final Integer toBlockStateId;
                        final BlockPos blockPos = new BlockPos(worldUID, x, y, z);
                        if (ArtisanBlockStorageAsync.getAsync().isArtisanBlock(blockPos)) {
                            toBlockStateId = ArtisanBlockStorageAsync.getAsync().getArtisanBlockDataView(blockPos).state().appearanceState();
                        } else {
                            toBlockStateId = BlockMappingsManager.getMappedStateId(blockStateId);
                        }
                        if (toBlockStateId != null) {
                            baseChunk.set(sectionX, sectionY, sectionZ, toBlockStateId);
                        }
                    }
                }
            }
        }
        event.markForReEncode(true);
    }

    private void onSingleBlockChange(PacketSendEvent event) {
        final UUID playerUUID = event.getUser().getUUID();
        final WrapperPlayServerBlockChange wrapperPlayServerBlockChange = new WrapperPlayServerBlockChange(event);
        final int blockStateId = wrapperPlayServerBlockChange.getBlockId();
        final Integer toBlockStateId;
        final UUID worldUID = AsyncPlayerWorld.getPlayerWorld(playerUUID);
        final int x = wrapperPlayServerBlockChange.getBlockPosition().x, y = wrapperPlayServerBlockChange.getBlockPosition().y, z = wrapperPlayServerBlockChange.getBlockPosition().z;
        final BlockPos blockPos = new BlockPos(worldUID, x, y, z);
        if (ArtisanBlockStorageAsync.getAsync().isArtisanBlock(blockPos)) {
            toBlockStateId = ArtisanBlockStorageAsync.getAsync().getArtisanBlockDataView(blockPos).state().appearanceState();
        } else {
            toBlockStateId = BlockMappingsManager.getMappedStateId(blockStateId);
        }
        if (toBlockStateId != null) {
            wrapperPlayServerBlockChange.setBlockID(toBlockStateId);
        }
        event.markForReEncode(true);
    }

    private void onMultiBlockChange(PacketSendEvent event) {
        final UUID playerUUID = event.getUser().getUUID();
        final UUID worldUID = AsyncPlayerWorld.getPlayerWorld(playerUUID);
        final WrapperPlayServerMultiBlockChange wrapperPlayServerMultiBlockChange = new WrapperPlayServerMultiBlockChange(event);
        for (WrapperPlayServerMultiBlockChange.EncodedBlock encodedBlock : wrapperPlayServerMultiBlockChange.getBlocks()) {
            final int blockStateId = encodedBlock.getBlockId();
            final Integer toBlockStateId;
            final BlockPos blockPos = new BlockPos(worldUID, encodedBlock.getX(), encodedBlock.getY(), encodedBlock.getZ());
            if (ArtisanBlockStorageAsync.getAsync().isArtisanBlock(blockPos)) {
                toBlockStateId = ArtisanBlockStorageAsync.getAsync().getArtisanBlockDataView(blockPos).state().appearanceState();
            } else {
                toBlockStateId = BlockMappingsManager.getMappedStateId(blockStateId);
            }
            if (toBlockStateId != null) {
                encodedBlock.setBlockId(toBlockStateId);
            }
        }
        event.markForReEncode(true);
    }
}
