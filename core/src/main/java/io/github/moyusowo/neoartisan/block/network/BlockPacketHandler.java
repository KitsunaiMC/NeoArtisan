package io.github.moyusowo.neoartisan.block.network;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.storage.internal.ArtisanBlockStorageInternal;
import io.github.moyusowo.neoartisan.util.ReflectionUtil;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockData;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.BitStorage;
import net.minecraft.util.SimpleBitStorage;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.github.moyusowo.neoartisan.block.util.BlockStateUtil.stateById;

public class BlockPacketHandler extends ChannelDuplexHandler {

    private final ServerPlayer player;

    public BlockPacketHandler(ServerPlayer player) {
        this.player = player;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ClientboundBlockUpdatePacket packet) {
            handleSingleBlockUpdate(packet);
        } else if (msg instanceof ClientboundLevelChunkWithLightPacket packet) {
            handleChunkUpdate(packet);
            handleChunkArtisanBlock(ctx, promise, packet.getX(), packet.getZ());
        } else if (msg instanceof ClientboundSectionBlocksUpdatePacket packet) {
            handleSectionBlocksUpdate(packet);
        } else if (msg instanceof ClientboundBundlePacket packet) {
            final List<ChunkPos> chunks = new ArrayList<>();
            packet.subPackets().forEach(
                    subpacket -> {
                        if (subpacket instanceof ClientboundBlockUpdatePacket p) {
                            try {
                                handleSingleBlockUpdate(p);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        } else if (subpacket instanceof ClientboundSectionBlocksUpdatePacket p) {
                            try {
                                handleSectionBlocksUpdate(p);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        } else if (subpacket instanceof ClientboundLevelChunkWithLightPacket p) {
                            try {
                                handleChunkUpdate(p);
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                            chunks.add(new ChunkPos(p.getX(), p.getZ()));
                        }
                    }
            );
            if (!chunks.isEmpty()) {
                for (ChunkPos chunkPos : chunks) {
                    handleChunkArtisanBlock(ctx, promise, chunkPos.x, chunkPos.z);
                }
            }
        }
        super.write(ctx, msg, promise);
    }

    private void handleSingleBlockUpdate(ClientboundBlockUpdatePacket packet) throws Exception {
        BlockPos blockPos = (BlockPos) ReflectionUtil.BLOCK_UPDATE_POS.get(packet);
        BlockState state = (BlockState) ReflectionUtil.BLOCK_UPDATE_STATE.get(packet);
        if (ArtisanBlockStorageInternal.getInternal().isArtisanBlock(player.level(), blockPos)) {
            BlockState toState = stateById(ArtisanBlockStorageInternal.getInternal().getArtisanBlock(player.level(), blockPos).getArtisanBlockState().appearanceState());
            ReflectionUtil.BLOCK_UPDATE_STATE.set(packet, toState);
        } else {
            BlockState toState = BlockMappingsManager.getMappedState(state);
            if (toState != null) {
                ReflectionUtil.BLOCK_UPDATE_STATE.set(packet, toState);
            }
        }
    }

    private void handleSectionBlocksUpdate(ClientboundSectionBlocksUpdatePacket packet) throws Exception {
        SectionPos sectionPos = (SectionPos) ReflectionUtil.SECTION_UPDATE_POS.get(packet);
        short[] positions = (short[]) ReflectionUtil.SECTION_BLOCK_UPDATE_POS.get(packet);
        BlockPos[] pos = toBlockPos(positions, sectionPos);
        BlockState[] states = (BlockState[]) ReflectionUtil.SECTION_BLOCK_UPDATE_STATE.get(packet);
        for (int i = 0; i < states.length; i++) {
            if (ArtisanBlockStorageInternal.getInternal().isArtisanBlock(player.level(), pos[i])) {
                states[i] = stateById(ArtisanBlockStorageInternal.getInternal().getArtisanBlock(player.level(), pos[i]).getArtisanBlockState().appearanceState());
            } else {
                BlockState toState = BlockMappingsManager.getMappedState(states[i]);
                if (toState != null) {
                    states[i] = toState;
                }
            }
        }
        ReflectionUtil.SECTION_BLOCK_UPDATE_STATE.set(packet, states);
    }

    private void handleChunkArtisanBlock(ChannelHandlerContext ctx, ChannelPromise promise, final int chunkX, final int chunkZ) {
        promise.addListener(future -> {
            if (future.isSuccess()) {
                Map<BlockPos, ArtisanBlockData> data = ArtisanBlockStorageInternal.getInternal().getChunkArtisanBlocks(player.level(), chunkX, chunkZ);
                List<Packet<? super ClientGamePacketListener>> bundle = new ArrayList<>();
                for (Map.Entry<BlockPos, ArtisanBlockData> entry : data.entrySet()) {
                    ClientboundBlockUpdatePacket newPacket = new ClientboundBlockUpdatePacket(
                            entry.getKey(),
                            stateById(entry.getValue().getArtisanBlockState().appearanceState())
                    );
                    bundle.add(newPacket);
                }
                ClientboundBundlePacket bundlePacket = new ClientboundBundlePacket(bundle);
                ctx.write(bundlePacket).addListener(f -> {
                    if (!f.isSuccess()) {
                        NeoArtisan.logger().severe("补充包发送失败: " + f.cause());
                    }
                });
                ctx.flush();
            } else {
                NeoArtisan.logger().severe("原始包发送失败: " + future.cause());
            }
        });
    }

    private void handleChunkUpdate(ClientboundLevelChunkWithLightPacket packet) throws Exception {
        ClientboundLevelChunkPacketData chunkData = packet.getChunkData();
        byte[] buffer = (byte[]) ReflectionUtil.CHUNK_DATA.get(chunkData);
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.wrappedBuffer(buffer));
        FriendlyByteBuf newBuf = new FriendlyByteBuf(Unpooled.buffer());
        while (buf.readerIndex() < buffer.length) {
            int nonEmptyBlock = buf.readShort();
            newBuf.writeShort(nonEmptyBlock);
            int bitsPerBlock = buf.readByte();
            newBuf.writeByte(bitsPerBlock);
            if (bitsPerBlock == 0) {
                int stateId = buf.readVarInt();
                Integer toStateId = BlockMappingsManager.getMappedStateId(stateId);
                newBuf.writeVarInt(Objects.requireNonNullElse(toStateId, stateId));
                long[] data = buf.readLongArray();
                newBuf.writeLongArray(data);
            } else if (bitsPerBlock <= 8) {
                int sizeOfPalette = buf.readVarInt();
                newBuf.writeVarInt(sizeOfPalette);
                int[] palette = new int[sizeOfPalette];
                for (int i = 0; i < sizeOfPalette; i++) {
                    palette[i] = buf.readVarInt();
                    Integer toStateId = BlockMappingsManager.getMappedStateId(palette[i]);
                    if (toStateId != null) {
                        newBuf.writeVarInt(toStateId);
                    } else {
                        newBuf.writeVarInt(palette[i]);
                    }
                }
                long[] data = buf.readLongArray();
                newBuf.writeLongArray(data);
            } else {
                long[] data = buf.readLongArray();
                BitStorage storage = new SimpleBitStorage(bitsPerBlock, 4096, data);
                for (int pos = 0; pos < 4096; pos++) {
                    int stateId = storage.get(pos);
                    Integer toStateId = BlockMappingsManager.getMappedStateId(stateId);
                    if (toStateId != null) {
                        storage.set(pos, toStateId);
                    }
                }
                newBuf.writeLongArray(storage.getRaw());
            }
            int bitPerBiome = buf.readByte();
            newBuf.writeByte(bitPerBiome);
            if (bitPerBiome == 0) {
                int sizeOfPalette = buf.readVarInt();
                newBuf.writeVarInt(sizeOfPalette);
                long[] data = buf.readLongArray();
                newBuf.writeLongArray(data);
            } else if (bitPerBiome <= 3) {
                int sizeOfPalette = buf.readVarInt();
                newBuf.writeVarInt(sizeOfPalette);
                int[] palette = new int[sizeOfPalette];
                for (int i = 0; i < sizeOfPalette; i++) {
                    palette[i] = buf.readVarInt();
                    newBuf.writeVarInt(palette[i]);
                }
                long[] data = buf.readLongArray();
                newBuf.writeLongArray(data);
            } else {
                long[] data = buf.readLongArray();
                newBuf.writeLongArray(data);
            }
        }
        ReflectionUtil.CHUNK_DATA.set(chunkData, newBuf.array());
    }

    private BlockPos[] toBlockPos(short[] positions, SectionPos sectionPos) {
        int sectionX = sectionPos.x();
        int sectionY = sectionPos.y();
        int sectionZ = sectionPos.z();
        BlockPos[] blockPos = new BlockPos[positions.length];
        for (int i = 0; i < positions.length; i++) {
            int x = (positions[i] >> 8) & 0xF;
            int y = (positions[i] >> 4) & 0xF;
            int z = positions[i] & 0xF;
            blockPos[i] = new BlockPos(
                    (sectionX << 4) + x,
                    (sectionY << 4) + y,
                    (sectionZ << 4) + z
            );
        }
        return blockPos;
    }
}
