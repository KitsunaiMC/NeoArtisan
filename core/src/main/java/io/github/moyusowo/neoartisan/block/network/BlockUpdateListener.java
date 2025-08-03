package io.github.moyusowo.neoartisan.block.network;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListener;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.protocol.attribute.Attributes;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.protocol.world.chunk.BaseChunk;
import com.github.retrooper.packetevents.protocol.world.chunk.impl.v_1_18.Chunk_v1_18;
import com.github.retrooper.packetevents.protocol.world.chunk.palette.*;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerBlockChange;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerChunkData;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerMultiBlockChange;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes;
import com.sun.jdi.InternalException;
import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.data.ArtisanBlockDataView;
import io.github.moyusowo.neoartisan.block.state.listener.BlockBreakSynchronize;
import io.github.moyusowo.neoartisan.block.storage.internal.ArtisanBlockStorageAsync;
import io.github.moyusowo.neoartisan.block.util.BlockPos;
import io.github.moyusowo.neoartisan.block.util.ChunkPos;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;

import java.util.HashMap;
import java.util.List;
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
            default -> { return; }
        }
    }

    @SuppressWarnings("unchecked")
    private void onChunkDataSend(PacketSendEvent event) {
        final UUID playerUUID = event.getUser().getUUID();
        final UUID worldUID = AsyncPlayerWorld.getPlayerWorld(playerUUID);
        final int minHeight = event.getUser().getMinWorldHeight();
        final WrapperPlayServerChunkData wrapperPlayServerChunkData = new WrapperPlayServerChunkData(event);
        final int chunkX = wrapperPlayServerChunkData.getColumn().getX(), chunkZ = wrapperPlayServerChunkData.getColumn().getZ();
        final BaseChunk[] baseChunks = wrapperPlayServerChunkData.getColumn().getChunks();
        for (int sectionIndex = 0; sectionIndex < baseChunks.length; sectionIndex++) {
            final Chunk_v1_18 chunk = (Chunk_v1_18) baseChunks[sectionIndex];
            if (chunk.getChunkData().palette instanceof SingletonPalette singletonPalette) {
                final int fromState = (int) PaletteFields.getField(PaletteFields.SINGLETON_PALETTE_STATE, singletonPalette);
                final Integer toState = BlockMappingsManager.getMappedStateId(fromState);
                if (toState != null) {
                    PaletteFields.setField(PaletteFields.SINGLETON_PALETTE_STATE, singletonPalette, toState);
                }
            } else if (chunk.getChunkData().palette instanceof MapPalette mapPalette) {
                final int[] idToState = (int[]) PaletteFields.getField(PaletteFields.MAP_PALATTE_ID_TO_STATE, mapPalette);
                final HashMap<Object, Integer> stateToId = (HashMap<Object, Integer>) PaletteFields.getField(PaletteFields.MAP_PALETTE_STATE_TO_ID, mapPalette);
                for (int i = 0; i < idToState.length; i++) {
                    final int fromState = idToState[i];
                    final Integer toState = BlockMappingsManager.getMappedStateId(fromState);
                    if (toState != null) {
                        idToState[i] = toState;
                    }
                }
                for (Object fromState : stateToId.keySet()) {
                    final Integer toState = BlockMappingsManager.getMappedStateId((Integer) fromState);
                    if (toState != null) {
                        stateToId.replace(fromState, toState);
                    }
                }
                PaletteFields.setField(PaletteFields.MAP_PALATTE_ID_TO_STATE, mapPalette, idToState);
                PaletteFields.setField(PaletteFields.MAP_PALETTE_STATE_TO_ID, mapPalette, stateToId);
            } else if (chunk.getChunkData().palette instanceof ListPalette listPalette) {
                final int[] data = (int[]) PaletteFields.getField(PaletteFields.LIST_PALETTE_DATA, listPalette);
                for (int i = 0; i < data.length; i++) {
                    final int fromState = data[i];
                    final Integer toState = BlockMappingsManager.getMappedStateId(fromState);
                    if (toState != null) {
                        data[i] = toState;
                    }
                }
                PaletteFields.setField(PaletteFields.LIST_PALETTE_DATA, listPalette, data);
            } else if (chunk.getChunkData().palette instanceof GlobalPalette) {
                for (int sectionX = 0; sectionX < 16; sectionX++) {
                    for (int sectionY = 0; sectionY < 16; sectionY++) {
                        for (int sectionZ = 0; sectionZ < 16; sectionZ++) {
                            final int x = (chunkX << 4) + sectionX;
                            final int y = (sectionIndex << 4) + sectionY + minHeight;
                            final int z = (chunkZ << 4) + sectionZ;
                            final int blockStateId = chunk.getBlockId(sectionX, sectionY, sectionZ);
                            final Integer toBlockStateId;
                            final BlockPos blockPos = new BlockPos(worldUID, x, y, z);
                            if (ArtisanBlockStorageAsync.getAsync().isArtisanBlock(blockPos)) {
                                toBlockStateId = ArtisanBlockStorageAsync.getAsync().getArtisanBlockDataView(blockPos).state().appearanceState();
                            } else {
                                toBlockStateId = BlockMappingsManager.getMappedStateId(blockStateId);
                            }
                            if (toBlockStateId != null) {
                                chunk.set(sectionX, sectionY, sectionZ, toBlockStateId);
                            }
                        }
                    }
                }
            } else {
                throw new InternalException("Unknown palette type: " + chunk.getChunkData().palette.getClass().getName());
            }
            List<ArtisanBlockDataView> views = ArtisanBlockStorageAsync.getAsync().getChunkArtisanBlockDataViews(new ChunkPos(worldUID, chunkX, chunkZ));
            for (ArtisanBlockDataView view : views) {
                if (view.location().getBlockY() >= (sectionIndex << 4) + minHeight && view.location().getBlockY() < (sectionIndex << 4) + minHeight + 16) {
                    chunk.set(view.location().getBlockX() - (chunkX << 4), view.location().getBlockY() - ((sectionIndex << 4) + minHeight), view.location().getBlockZ() - (chunkZ << 4), view.state().appearanceState());
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
