package io.github.moyusowo.neoartisan.block.network;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.storage.internal.ArtisanBlockStorageInternal;
import io.github.moyusowo.neoartisan.util.ReflectionUtil;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCropData;
import io.github.moyusowo.neoartisanapi.api.block.thin.ArtisanThinBlockData;
import io.github.moyusowo.neoartisanapi.api.block.transparent.ArtisanTransparentBlockData;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.network.protocol.game.ClientboundLevelChunkWithLightPacket;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.BitStorage;
import net.minecraft.util.SimpleBitStorage;
import net.minecraft.world.level.block.state.BlockState;

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
            promise.addListener(future -> {
                if (future.isSuccess()) {
                    Map<BlockPos, ArtisanBlockData> data = ArtisanBlockStorageInternal.getInternal().getChunkArtisanBlocks(player.level(), packet.getX(), packet.getZ());
                    int i = 0;
                    for (Map.Entry<BlockPos, ArtisanBlockData> entry : data.entrySet()) {
                        if (entry.getValue() instanceof ArtisanCropData artisanCropData) {
                            ClientboundBlockUpdatePacket newPacket = new ClientboundBlockUpdatePacket(entry.getKey(), stateById(artisanCropData.getArtisanBlockState().appearanceState()));
                            ctx.write(newPacket).addListener(f -> {
                                if (!f.isSuccess()) {
                                    NeoArtisan.logger().severe("补充包 " + i + " 发送失败: " + f.cause());
                                }
                            });
                        } else if (entry.getValue() instanceof ArtisanTransparentBlockData artisanTransparentBlockData) {
                            ClientboundBlockUpdatePacket newPacket = new ClientboundBlockUpdatePacket(entry.getKey(), stateById(artisanTransparentBlockData.getArtisanBlockState().appearanceState()));
                            ctx.write(newPacket).addListener(f -> {
                                if (!f.isSuccess()) {
                                    NeoArtisan.logger().severe("补充包 " + i + " 发送失败: " + f.cause());
                                }
                            });
                        } else if (entry.getValue() instanceof ArtisanThinBlockData artisanThinBlockData) {
                            ClientboundBlockUpdatePacket newPacket = new ClientboundBlockUpdatePacket(entry.getKey(), stateById(artisanThinBlockData.getArtisanBlockState().appearanceState()));
                            ctx.write(newPacket).addListener(f -> {
                                if (!f.isSuccess()) {
                                    NeoArtisan.logger().severe("补充包 " + i + " 发送失败: " + f.cause());
                                }
                            });
                        }
                    }
                    ctx.flush();
                } else {
                    NeoArtisan.logger().severe("原始包发送失败: " + future.cause());
                }
            });
        } else if (msg instanceof ClientboundSectionBlocksUpdatePacket packet) {
            handleSectionBlocksUpdate(packet);
        }
        super.write(ctx, msg, promise);
    }

    private void handleSingleBlockUpdate(ClientboundBlockUpdatePacket packet) throws Exception {
        BlockPos blockPos = (BlockPos) ReflectionUtil.getField(packet, "pos");
        BlockState state = (BlockState) ReflectionUtil.getField(packet, "blockState");
        if (ArtisanBlockStorageInternal.getInternal().isArtisanBlock(player.level(), blockPos)
                && ArtisanBlockStorageInternal.getInternal().getArtisanBlock(player.level(), blockPos) instanceof ArtisanCropData artisanCropData) {
            BlockState toState = stateById(artisanCropData.getArtisanBlockState().appearanceState());
            ReflectionUtil.setField(packet, "blockState", toState);
        } else if (ArtisanBlockStorageInternal.getInternal().isArtisanBlock(player.level(), blockPos)
                && ArtisanBlockStorageInternal.getInternal().getArtisanBlock(player.level(), blockPos) instanceof ArtisanTransparentBlockData artisanTransparentBlockData) {
            BlockState toState = stateById(artisanTransparentBlockData.getArtisanBlockState().appearanceState());
            ReflectionUtil.setField(packet, "blockState", toState);
        } else if (ArtisanBlockStorageInternal.getInternal().isArtisanBlock(player.level(), blockPos)
                && ArtisanBlockStorageInternal.getInternal().getArtisanBlock(player.level(), blockPos) instanceof ArtisanThinBlockData artisanThinBlockData) {
            BlockState toState = stateById(artisanThinBlockData.getArtisanBlockState().appearanceState());
            ReflectionUtil.setField(packet, "blockState", toState);
        } else {
            BlockState toState = BlockMappingsManager.getMappedState(state);
            if (toState != null) {
                ReflectionUtil.setField(packet, "blockState", toState);
            }
        }
    }

    private void handleSectionBlocksUpdate(ClientboundSectionBlocksUpdatePacket packet) throws Exception {
        SectionPos sectionPos = (SectionPos) ReflectionUtil.getField(packet, "sectionPos");
        short[] positions = (short[]) ReflectionUtil.getField(packet, "positions");
        BlockPos[] pos = toBlockPos(positions, sectionPos);
        BlockState[] states = (BlockState[]) ReflectionUtil.getField(packet, "states");
        for (int i = 0; i < states.length; i++) {
            if (ArtisanBlockStorageInternal.getInternal().isArtisanBlock(player.level(), pos[i])
                    && ArtisanBlockStorageInternal.getInternal().getArtisanBlock(player.level(), pos[i]) instanceof ArtisanCropData artisanCropData) {
                states[i] = stateById(artisanCropData.getArtisanBlockState().appearanceState());
            } else if (ArtisanBlockStorageInternal.getInternal().isArtisanBlock(player.level(), pos[i])
                    && ArtisanBlockStorageInternal.getInternal().getArtisanBlock(player.level(), pos[i]) instanceof ArtisanTransparentBlockData artisanTransparentBlockData) {
                states[i] = stateById(artisanTransparentBlockData.getArtisanBlockState().appearanceState());
            } else if (ArtisanBlockStorageInternal.getInternal().isArtisanBlock(player.level(), pos[i])
                    && ArtisanBlockStorageInternal.getInternal().getArtisanBlock(player.level(), pos[i]) instanceof ArtisanThinBlockData artisanThinBlockData) {
                states[i] = stateById(artisanThinBlockData.getArtisanBlockState().appearanceState());
            } else {
                BlockState toState = BlockMappingsManager.getMappedState(states[i]);
                if (toState != null) {
                    states[i] = toState;
                }
            }
        }
        ReflectionUtil.setField(packet, "states", states);
    }

    private void handleChunkUpdate(ClientboundLevelChunkWithLightPacket packet) throws Exception {
        ClientboundLevelChunkPacketData chunkData = packet.getChunkData();
        byte[] buffer = (byte[]) ReflectionUtil.getField(chunkData, "buffer");
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
        ReflectionUtil.setField(chunkData, "buffer", newBuf.array());
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
