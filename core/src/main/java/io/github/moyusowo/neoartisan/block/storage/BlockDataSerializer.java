package io.github.moyusowo.neoartisan.block.storage;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.blockdata.ArtisanBlockDataInternal;
import io.github.moyusowo.neoartisan.block.util.BlockPos;
import io.github.moyusowo.neoartisan.block.util.ChunkPos;
import io.github.moyusowo.neoartisan.util.terminate.TerminateMethod;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.blockdata.ArtisanBlockData;
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
                            BlockPos pos = blockEntry.getKey();
                            out.writeInt(pos.x());
                            out.writeInt(pos.y());
                            out.writeInt(pos.z());
                            out.writeUTF(blockEntry.getValue().blockId().getNamespace());
                            out.writeUTF(blockEntry.getValue().blockId().getKey());
                            out.writeInt(blockEntry.getValue().stage());
                            byte[] pdcByte = blockEntry.getValue().getPersistentDataContainer().serializeToBytes();
                            out.writeInt(pdcByte.length);
                            out.write(pdcByte);
                        }
                    }
                }
            }
        } catch (IOException e) {
            NeoArtisan.logger().severe("Fail to save custom block data: " + e);
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
                    if (world == null) {
                        NeoArtisan.logger().severe("UUID " + uuid + "can not match world! ignoring file...");
                        continue;
                    }
                    Map<ChunkPos, Map<BlockPos, ArtisanBlockData>> chunkMap = new HashMap<>();
                    storage.put(uuid, chunkMap);
                    try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                        int chunkCount = in.readInt();
                        for (int i = 0; i < chunkCount; i++) {
                            ChunkPos chunkPos = new ChunkPos(in.readInt(), in.readInt());
                            Map<BlockPos, ArtisanBlockData> blockMap = new HashMap<>();
                            chunkMap.put(chunkPos, blockMap);
                            int blockCount = in.readInt();
                            for (int j = 0; j < blockCount; j++) {
                                BlockPos blockPos = new BlockPos(
                                        in.readInt(),
                                        in.readInt(),
                                        in.readInt()
                                );
                                ArtisanBlockData artisanBlockData = ArtisanBlockData.builder()
                                        .blockId(new NamespacedKey(in.readUTF(), in.readUTF()))
                                        .stage(in.readInt())
                                        .location(new Location(world, blockPos.x(), blockPos.y(), blockPos.z()))
                                        .build();
                                int length = in.readInt();
                                byte[] pdcByte = in.readNBytes(length);
                                PersistentDataContainer persistentDataContainer = NeoArtisanAPI.emptyPersistentDataContainer().emptyPersistentDataContainer();
                                persistentDataContainer.readFromBytes(pdcByte, true);
                                ((ArtisanBlockDataInternal) artisanBlockData).setPersistentDataContainer(persistentDataContainer);
                                blockMap.put(blockPos, artisanBlockData);
                            }
                        }
                    }
                }
            }
            NeoArtisan.logger().info("successfully loaded custom block data from file!");
        } catch (IOException e) {
            NeoArtisan.logger().severe("fail to load custom block data from file: " + e);
        }
    }
}
