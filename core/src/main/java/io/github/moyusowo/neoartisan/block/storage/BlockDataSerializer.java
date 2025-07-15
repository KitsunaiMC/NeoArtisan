package io.github.moyusowo.neoartisan.block.storage;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.base.internal.ArtisanBlockDataInternal;
import io.github.moyusowo.neoartisan.util.BlockPos;
import io.github.moyusowo.neoartisan.util.ChunkPos;
import io.github.moyusowo.neoartisan.util.terminate.TerminateMethod;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCropData;
import io.github.moyusowo.neoartisanapi.api.block.full.ArtisanFullBlockData;
import io.github.moyusowo.neoartisanapi.api.block.head.ArtisanHeadBlockData;
import io.github.moyusowo.neoartisanapi.api.block.thin.ArtisanThinBlockData;
import io.github.moyusowo.neoartisanapi.api.block.transparent.ArtisanTransparentBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.persistence.PersistentDataContainer;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

final class BlockDataSerializer {

    private BlockDataSerializer() {}

    private static final String CROP_BLOCK = "crop";
    private static final String TRANSPARENT_BLOCK = "transparent_block";
    private static final String THIN_BLOCK = "thin_block";
    private static final String FULL_BLOCK = "full_block";
    private static final String HEAD_BLOCK = "head_block";

    @TerminateMethod
    public static void save() {
        try {
            File dataFolder = new File(NeoArtisan.instance().getDataFolder(), "block/storage");
            if (!dataFolder.exists()) dataFolder.mkdirs();
            for (World world : Bukkit.getWorlds()) {
                Map<ChunkPos, Map<BlockPos, ArtisanBlockData>> chunkMap = ArtisanBlockStorageImpl.getInstance().getLevelArtisanBlocks(world.getUID());
                File file = new File(dataFolder, world.getUID() + ".dat");
                try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
                    out.writeInt(chunkMap.size());
                    for (Map.Entry<ChunkPos, Map<BlockPos, ArtisanBlockData>> chunkEntry : chunkMap.entrySet()) {
                        ChunkPos chunkPos = chunkEntry.getKey();
                        out.writeInt(chunkPos.x());
                        out.writeInt(chunkPos.z());
                        Map<BlockPos, ArtisanBlockData> blockMap = chunkEntry.getValue();
                        out.writeInt(blockMap.size());
                        for (Map.Entry<BlockPos, ArtisanBlockData> blockEntry : blockMap.entrySet()) {
                            switch (blockEntry.getValue()) {
                                case ArtisanCropData artisanCropData -> {
                                    out.writeUTF(CROP_BLOCK);
                                    BlockPos pos = blockEntry.getKey();
                                    out.writeInt(pos.x());
                                    out.writeInt(pos.y());
                                    out.writeInt(pos.z());
                                    out.writeUTF(artisanCropData.blockId().getNamespace());
                                    out.writeUTF(artisanCropData.blockId().getKey());
                                    out.writeInt(artisanCropData.stage());
                                }
                                case ArtisanTransparentBlockData artisanTransparentBlockData -> {
                                    out.writeUTF(TRANSPARENT_BLOCK);
                                    BlockPos pos = blockEntry.getKey();
                                    out.writeInt(pos.x());
                                    out.writeInt(pos.y());
                                    out.writeInt(pos.z());
                                    out.writeUTF(artisanTransparentBlockData.blockId().getNamespace());
                                    out.writeUTF(artisanTransparentBlockData.blockId().getKey());
                                    out.writeInt(artisanTransparentBlockData.stage());
                                }
                                case ArtisanThinBlockData artisanThinBlockData -> {
                                    out.writeUTF(THIN_BLOCK);
                                    BlockPos pos = blockEntry.getKey();
                                    out.writeInt(pos.x());
                                    out.writeInt(pos.y());
                                    out.writeInt(pos.z());
                                    out.writeUTF(artisanThinBlockData.blockId().getNamespace());
                                    out.writeUTF(artisanThinBlockData.blockId().getKey());
                                    out.writeInt(artisanThinBlockData.stage());
                                }
                                case ArtisanFullBlockData artisanFullBlockData -> {
                                    out.writeUTF(FULL_BLOCK);
                                    BlockPos pos = blockEntry.getKey();
                                    out.writeInt(pos.x());
                                    out.writeInt(pos.y());
                                    out.writeInt(pos.z());
                                    out.writeUTF(artisanFullBlockData.blockId().getNamespace());
                                    out.writeUTF(artisanFullBlockData.blockId().getKey());
                                    out.writeInt(artisanFullBlockData.stage());
                                }
                                case ArtisanHeadBlockData artisanHeadBlockData -> {
                                    out.writeUTF(HEAD_BLOCK);
                                    BlockPos pos = blockEntry.getKey();
                                    out.writeInt(pos.x());
                                    out.writeInt(pos.y());
                                    out.writeInt(pos.z());
                                    out.writeUTF(artisanHeadBlockData.blockId().getNamespace());
                                    out.writeUTF(artisanHeadBlockData.blockId().getKey());
                                    out.writeInt(artisanHeadBlockData.stage());
                                }
                                case null, default ->
                                        throw new IllegalArgumentException("BlockType can not be Serializer!");
                            }
                            byte[] pdcByte = blockEntry.getValue().getPersistentDataContainer().serializeToBytes();
                            out.writeInt(pdcByte.length);
                            out.write(pdcByte);
                        }
                    }
                }
            }
            if (NeoArtisan.isDebugMode()) {
                NeoArtisan.logger().info("自定义方块数据自动保存成功！");
            }
        } catch (IOException e) {
            NeoArtisan.logger().severe("自定义方块数据保存失败: " + e);
        }
    }

    public static void load(Map<UUID, Map<ChunkPos, Map<BlockPos, ArtisanBlockData>>> storage) {
        try {
            File dataFolder = new File(NeoArtisan.instance().getDataFolder(), "block/storage");
            if (!dataFolder.exists()) return;
            File[] files = dataFolder.listFiles();
            if (files == null) return;
            storage.clear();
            for (File file : files) {
                if (file.isFile() && file.getName().toLowerCase().endsWith(".dat")) {
                    UUID uuid = UUID.fromString(file.getName().substring(0, file.getName().length() - 4));
                    World world = Bukkit.getWorld(uuid);
                    if (world == null) throw new IllegalArgumentException("UUID can not match!");Map<ChunkPos, Map<BlockPos, ArtisanBlockData>> chunkMap = new HashMap<>();
                    storage.put(uuid, chunkMap);
                    try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                        int chunkCount = in.readInt();
                        for (int i = 0; i < chunkCount; i++) {
                            ChunkPos chunkPos = new ChunkPos(in.readInt(), in.readInt());
                            Map<BlockPos, ArtisanBlockData> blockMap = new HashMap<>();
                            chunkMap.put(chunkPos, blockMap);
                            int blockCount = in.readInt();
                            for (int j = 0; j < blockCount; j++) {
                                String type = in.readUTF();
                                switch (type) {
                                    case CROP_BLOCK -> {
                                        BlockPos blockPos = new BlockPos(
                                                in.readInt(),
                                                in.readInt(),
                                                in.readInt()
                                        );
                                        ArtisanCropData artisanCropData = ArtisanCropData.factory().builder()
                                                .blockId(new NamespacedKey(in.readUTF(), in.readUTF()))
                                                .stage(in.readInt())
                                                .location(new Location(world, blockPos.x(), blockPos.y(), blockPos.z()))
                                                .build();
                                        int length = in.readInt();
                                        byte[] pdcByte = in.readNBytes(length);
                                        PersistentDataContainer persistentDataContainer = NeoArtisanAPI.emptyPersistentDataContainer().emptyPersistentDataContainer();
                                        persistentDataContainer.readFromBytes(pdcByte, true);
                                        ArtisanBlockDataInternal.asInternal(artisanCropData).setPersistentDataContainer(persistentDataContainer);
                                        blockMap.put(blockPos, artisanCropData);
                                    }
                                    case TRANSPARENT_BLOCK -> {
                                        BlockPos blockPos = new BlockPos(
                                                in.readInt(),
                                                in.readInt(),
                                                in.readInt()
                                        );
                                        ArtisanTransparentBlockData artisanTransparentBlockData = ArtisanTransparentBlockData.factory().builder()
                                                .blockId(new NamespacedKey(in.readUTF(), in.readUTF()))
                                                .stage(in.readInt())
                                                .location(new Location(world, blockPos.x(), blockPos.y(), blockPos.z()))
                                                .build();
                                        int length = in.readInt();
                                        byte[] pdcByte = in.readNBytes(length);
                                        PersistentDataContainer persistentDataContainer = NeoArtisanAPI.emptyPersistentDataContainer().emptyPersistentDataContainer();
                                        persistentDataContainer.readFromBytes(pdcByte, true);
                                        ArtisanBlockDataInternal.asInternal(artisanTransparentBlockData).setPersistentDataContainer(persistentDataContainer);
                                        blockMap.put(blockPos, artisanTransparentBlockData);
                                    }
                                    case THIN_BLOCK -> {
                                        BlockPos blockPos = new BlockPos(
                                                in.readInt(),
                                                in.readInt(),
                                                in.readInt()
                                        );
                                        ArtisanThinBlockData artisanThinBlockData = ArtisanThinBlockData.factory().builder()
                                                .blockId(new NamespacedKey(in.readUTF(), in.readUTF()))
                                                .stage(in.readInt())
                                                .location(new Location(world, blockPos.x(), blockPos.y(), blockPos.z()))
                                                .build();
                                        int length = in.readInt();
                                        byte[] pdcByte = in.readNBytes(length);
                                        PersistentDataContainer persistentDataContainer = NeoArtisanAPI.emptyPersistentDataContainer().emptyPersistentDataContainer();
                                        persistentDataContainer.readFromBytes(pdcByte, true);
                                        ArtisanBlockDataInternal.asInternal(artisanThinBlockData).setPersistentDataContainer(persistentDataContainer);
                                        blockMap.put(blockPos, artisanThinBlockData);
                                    }
                                    case FULL_BLOCK -> {
                                        BlockPos blockPos = new BlockPos(
                                                in.readInt(),
                                                in.readInt(),
                                                in.readInt()
                                        );
                                        ArtisanFullBlockData artisanFullBlockData = ArtisanFullBlockData.factory().builder()
                                                .blockId(new NamespacedKey(in.readUTF(), in.readUTF()))
                                                .stage(in.readInt())
                                                .location(new Location(world, blockPos.x(), blockPos.y(), blockPos.z()))
                                                .build();
                                        int length = in.readInt();
                                        byte[] pdcByte = in.readNBytes(length);
                                        PersistentDataContainer persistentDataContainer = NeoArtisanAPI.emptyPersistentDataContainer().emptyPersistentDataContainer();
                                        persistentDataContainer.readFromBytes(pdcByte, true);
                                        ArtisanBlockDataInternal.asInternal(artisanFullBlockData).setPersistentDataContainer(persistentDataContainer);
                                        blockMap.put(blockPos, artisanFullBlockData);
                                    }
                                    case HEAD_BLOCK -> {
                                        BlockPos blockPos = new BlockPos(
                                                in.readInt(),
                                                in.readInt(),
                                                in.readInt()
                                        );
                                        ArtisanHeadBlockData artisanHeadBlockData = ArtisanHeadBlockData.factory().builder()
                                                .blockId(new NamespacedKey(in.readUTF(), in.readUTF()))
                                                .stage(in.readInt())
                                                .location(new Location(world, blockPos.x(), blockPos.y(), blockPos.z()))
                                                .build();
                                        int length = in.readInt();
                                        byte[] pdcByte = in.readNBytes(length);
                                        PersistentDataContainer persistentDataContainer = NeoArtisanAPI.emptyPersistentDataContainer().emptyPersistentDataContainer();
                                        persistentDataContainer.readFromBytes(pdcByte, true);
                                        ArtisanBlockDataInternal.asInternal(artisanHeadBlockData).setPersistentDataContainer(persistentDataContainer);
                                        blockMap.put(blockPos, artisanHeadBlockData);
                                    }
                                    default -> throw new IllegalArgumentException("BlockType can not be Serializer!");

                                }
                            }
                        }
                    }
                }
            }
            NeoArtisan.logger().info("自定义方块数据读取成功！");
        } catch (IOException e) {
            NeoArtisan.logger().severe("自定义方块数据读取失败: " + e);
        }
    }
}
