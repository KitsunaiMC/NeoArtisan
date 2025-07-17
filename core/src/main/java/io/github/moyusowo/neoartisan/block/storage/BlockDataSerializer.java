package io.github.moyusowo.neoartisan.block.storage;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.data.ArtisanBlockDataView;
import io.github.moyusowo.neoartisan.block.storage.internal.ArtisanBlockStorageAsync;
import io.github.moyusowo.neoartisan.block.storage.internal.ArtisanBlockStorageInternal;
import io.github.moyusowo.neoartisan.block.util.ChunkPos;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisan.util.terminate.TerminateMethod;
import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;

import java.io.*;
import java.util.List;

final class BlockDataSerializer {

    private BlockDataSerializer() {}

    @TerminateMethod
    public static void save() {
        for (World world : Bukkit.getWorlds()) {
            NeoArtisan.logger().warning("saving " + world.getName() + "...");
            List<ChunkPos> chunkPosList = ArtisanBlockStorageAsync.getAsync().getWorldArtisanBlockChunks(world.getUID());
            File worldFolder = new File(world.getWorldFolder(), "neoartisan");
            if (!worldFolder.exists()) worldFolder.mkdirs();
            for (ChunkPos chunkPos : chunkPosList) {
                NeoArtisan.logger().warning("chunk: " + chunkPos.x() + ", " + chunkPos.z());
                if (ArtisanBlockStorageAsync.getAsync().checkAndCleanDirtyChunk(chunkPos)) {
                    NeoArtisan.logger().warning("save chunk: " + chunkPos.x() + ", " + chunkPos.z());
                    File chunkFile = new File(worldFolder, "r." + chunkPos.x() + "." + chunkPos.z() + ".neodat");
                    try (DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(chunkFile)))) {
                        List<ArtisanBlockDataView> views = ArtisanBlockStorageAsync.getAsync().getChunkArtisanBlockDataViews(chunkPos);
                        out.writeInt(views.size());
                        for (ArtisanBlockDataView view : views) {
                            out.writeInt(view.location().getBlockX());
                            out.writeInt(view.location().getBlockY());
                            out.writeInt(view.location().getBlockZ());
                            out.writeUTF(view.blockId().getNamespace());
                            out.writeUTF(view.blockId().getKey());
                            out.writeInt(view.stage());
                        }
                    } catch (IOException e) {
                        NeoArtisan.logger().severe("Fail to save custom block data at world: " + world.getName() + ", chunk: " + chunkPos.x() + "," + chunkPos.z() + ": " + e);
                    }
                }
            }
        }
    }

    @InitMethod(priority = InitPriority.STORAGE_LOAD)
    public static void load() {
        for (World world : Bukkit.getWorlds()) {
            try {
                File worldFolder = new File(world.getWorldFolder(), "neoartisan");
                if (!worldFolder.exists()) continue;
                File[] files = worldFolder.listFiles(file -> file.getName().endsWith(".neodat"));
                if (files == null) continue;
                for (File file : files) {
                    try (DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
                        int size = in.readInt();
                        for (int i = 0; i < size; i++) {
                            Location location = new Location(
                                    world, in.readInt(), in.readInt(), in.readInt()
                            );
                            NamespacedKey blockId = new NamespacedKey(
                                    in.readUTF(), in.readUTF()
                            );
                            int stateId = in.readInt();
                            ArtisanBlockStorageInternal.getInternal().placeArtisanBlock(ArtisanBlockData.builder().blockId(blockId).location(location).stage(stateId).build());
                        }
                    }
                }
            } catch (IOException e) {
                NeoArtisan.logger().severe("Fail to load custom block data at world " + world.getName() + ": " + e);
            }
            NeoArtisan.logger().info("successfully loaded custom block data at world " + world.getName());
        }
        NeoArtisan.logger().info("successfully loaded custom block data at all worlds");
    }
}
