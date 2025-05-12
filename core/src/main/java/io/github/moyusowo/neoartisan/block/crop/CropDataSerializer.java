package io.github.moyusowo.neoartisan.block.crop;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisanapi.api.block.crop.CurrentCropStage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class CropDataSerializer {

    private CropDataSerializer() {}

    public static void save() throws IOException {
        File dataFolder = new File(NeoArtisan.instance().getDataFolder(), "block/crop/storage");
        if (!dataFolder.exists()) dataFolder.mkdirs();
        for (World world : Bukkit.getWorlds()) {
            CraftWorld craftWorld = (CraftWorld) world;
            Level level = craftWorld.getHandle();
            Map<ChunkPos, Map<BlockPos, CurrentCropStage>> chunkMap = ArtisanCropStorageImpl.getInstance().getLevelArtisanCrops(level);
            File file = new File(dataFolder, world.getUID() + ".dat");
            try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
                out.writeInt(chunkMap.size());
                for (Map.Entry<ChunkPos, Map<BlockPos, CurrentCropStage>> chunkEntry : chunkMap.entrySet()) {
                    ChunkPos chunkPos = chunkEntry.getKey();
                    out.writeInt(chunkPos.x);
                    out.writeInt(chunkPos.z);
                    Map<BlockPos, CurrentCropStage> blockMap = chunkEntry.getValue();
                    out.writeInt(blockMap.size());
                    for (Map.Entry<BlockPos, CurrentCropStage> blockEntry : blockMap.entrySet()) {
                        BlockPos pos = blockEntry.getKey();
                        out.writeInt(pos.getX());
                        out.writeInt(pos.getY());
                        out.writeInt(pos.getZ());
                        CurrentCropStage currentCropStage = blockEntry.getValue();
                        out.writeUTF(currentCropStage.cropId().getNamespace());
                        out.writeUTF(currentCropStage.cropId().getKey());
                        out.writeInt(currentCropStage.stage());
                    }
                }
            }
        }
    }

    public static void load(Map<Level, Map<ChunkPos, Map<BlockPos, CurrentCropStage>>> storage) throws IOException {
        File dataFolder = new File(NeoArtisan.instance().getDataFolder(), "block/crop/storage");
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
                Map<ChunkPos, Map<BlockPos, CurrentCropStage>> chunkMap = new HashMap<>();
                storage.put(level, chunkMap);
                try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                    int chunkCount = in.readInt();
                    for (int i = 0; i < chunkCount; i++) {
                        ChunkPos chunkPos = new ChunkPos(in.readInt(), in.readInt());
                        Map<BlockPos, CurrentCropStage> blockMap = new HashMap<>();
                        chunkMap.put(chunkPos, blockMap);
                        int blockCount = in.readInt();
                        for (int j = 0; j < blockCount; j++) {
                            BlockPos blockPos = new BlockPos(
                                    in.readInt(),
                                    in.readInt(),
                                    in.readInt()
                            );
                            CurrentCropStage currentCropStage = new CurrentCropStageImpl(
                                    new NamespacedKey(in.readUTF(), in.readUTF()),
                                    in.readInt()
                            );
                            blockMap.put(blockPos, currentCropStage);
                        }
                    }
                }
            }
        }
    }

}
