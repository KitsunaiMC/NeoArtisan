package io.github.moyusowo.neoartisan.block.storage;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.internal.ArtisanBlockStateInternal;
import io.github.moyusowo.neoartisan.util.terminate.TerminateMethod;
import io.github.moyusowo.neoartisanapi.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.ArtisanBlockState;
import io.github.moyusowo.neoartisanapi.api.block.crop.CurrentCropStage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
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
                CraftWorld craftWorld = (CraftWorld) world;
                Level level = craftWorld.getHandle();
                Map<ChunkPos, Map<BlockPos, ArtisanBlockState>> chunkMap = ArtisanBlockStorageImpl.getInstance().getLevelArtisanBlocks(level);
                File file = new File(dataFolder, world.getUID() + ".dat");
                try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
                    out.writeInt(chunkMap.size());
                    for (Map.Entry<ChunkPos, Map<BlockPos, ArtisanBlockState>> chunkEntry : chunkMap.entrySet()) {
                        ChunkPos chunkPos = chunkEntry.getKey();
                        out.writeInt(chunkPos.x);
                        out.writeInt(chunkPos.z);
                        Map<BlockPos, ArtisanBlockState> blockMap = chunkEntry.getValue();
                        out.writeInt(blockMap.size());
                        for (Map.Entry<BlockPos, ArtisanBlockState> blockEntry : blockMap.entrySet()) {
                            if (blockEntry.getValue() instanceof CurrentCropStage currentCropStage) {
                                out.writeUTF("crop");
                                BlockPos pos = blockEntry.getKey();
                                out.writeInt(pos.getX());
                                out.writeInt(pos.getY());
                                out.writeInt(pos.getZ());
                                out.writeUTF(currentCropStage.cropId().getNamespace());
                                out.writeUTF(currentCropStage.cropId().getKey());
                                out.writeInt(currentCropStage.stage());
                            }
                            byte[] pdcByte = blockEntry.getValue().getPersistentDataContainer().serializeToBytes();
                            out.writeInt(pdcByte.length);
                            out.write(pdcByte);
                        }
                    }
                }
            }
            NeoArtisan.logger().info("作物数据自动保存成功！");
        } catch (IOException e) {
            NeoArtisan.logger().severe("作物数据保存失败: " + e);
        }
    }

    public static void load(Map<Level, Map<ChunkPos, Map<BlockPos, ArtisanBlockState>>> storage) {
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
                    CraftWorld craftWorld = (CraftWorld) world;
                    if (craftWorld == null) throw new IllegalArgumentException("UUID can not match!");
                    Level level = craftWorld.getHandle();
                    Map<ChunkPos, Map<BlockPos, ArtisanBlockState>> chunkMap = new HashMap<>();
                    storage.put(level, chunkMap);
                    try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                        int chunkCount = in.readInt();
                        for (int i = 0; i < chunkCount; i++) {
                            ChunkPos chunkPos = new ChunkPos(in.readInt(), in.readInt());
                            Map<BlockPos, ArtisanBlockState> blockMap = new HashMap<>();
                            chunkMap.put(chunkPos, blockMap);
                            int blockCount = in.readInt();
                            for (int j = 0; j < blockCount; j++) {
                                String type = in.readUTF();
                                if (type.equals("crop")) {
                                    BlockPos blockPos = new BlockPos(
                                            in.readInt(),
                                            in.readInt(),
                                            in.readInt()
                                    );
                                    CurrentCropStage currentCropStage = NeoArtisanAPI.getCropRegistry().emptyCropStage(
                                            new NamespacedKey(in.readUTF(), in.readUTF()),
                                            in.readInt()
                                    );
                                    int length = in.readInt();
                                    byte[] pdcByte = in.readNBytes(length);
                                    PersistentDataContainer persistentDataContainer = NeoArtisan.emptyPersistentDataContainer();
                                    persistentDataContainer.readFromBytes(pdcByte, true);
                                    ArtisanBlockStateInternal.asInternal(currentCropStage).setPersistentDataContainer(persistentDataContainer);
                                    blockMap.put(blockPos, currentCropStage);
                                }

                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            NeoArtisan.logger().severe("作物数据读取失败: " + e);
        }
    }
}
