package io.github.moyusowo.neoartisan.block.block.listener;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import io.github.moyusowo.neoartisan.block.storage.internal.ArtisanBlockStorageInternal;
import io.github.moyusowo.neoartisan.block.task.LifecycleTaskManagerInternal;
import io.github.moyusowo.neoartisan.block.util.BlockPos;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.state.ArtisanSkullState;
import io.github.moyusowo.neoartisanapi.api.block.util.SoundProperty;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.block.data.BlockData;

import java.util.UUID;

public final class Util {
    private Util() {}

    public static void place(Block bukkitBlock, ArtisanBlockData artisanBlockData) {
        World world = bukkitBlock.getWorld();
        ArtisanBlockStorageInternal.getInternal().placeArtisanBlock(artisanBlockData);
        BlockData blockData = Bukkit.createBlockData(WrappedBlockState.getByGlobalId(artisanBlockData.getArtisanBlockState().actualState()).toString());
        world.setBlockData(bukkitBlock.getLocation(), blockData);
        if (artisanBlockData.getArtisanBlockState() instanceof ArtisanSkullState state) {
            Skull skull = (Skull) bukkitBlock.getState();
            final PlayerProfile playerProfile = Bukkit.createProfile(UUID.randomUUID());
            playerProfile.setProperty(new ProfileProperty("textures", state.getUrlBase64()));
            skull.setPlayerProfile(playerProfile);
            skull.update(true);
        }
        SoundProperty soundProperty = artisanBlockData.getArtisanBlock().getPlaceSoundProperty();
        if (soundProperty != null) {
            bukkitBlock.getWorld().playSound(
                    net.kyori.adventure.sound.Sound.sound(
                            soundProperty.key.key(),
                            net.kyori.adventure.sound.Sound.Source.BLOCK,
                            soundProperty.pitch,
                            soundProperty.volume
                    ),
                    bukkitBlock.getX(),
                    bukkitBlock.getY(),
                    bukkitBlock.getZ()
            );
        }
    }

    public static void replace(Block bukkitBlock, ArtisanBlockData artisanBlockData) {
        World world = bukkitBlock.getWorld();
        ArtisanBlockStorageInternal.getInternal().replaceArtisanBlock(artisanBlockData);
        BlockData blockData = Bukkit.createBlockData(WrappedBlockState.getByGlobalId(artisanBlockData.getArtisanBlockState().actualState()).toString());
        world.setBlockData(bukkitBlock.getLocation(), blockData);
        if (artisanBlockData.getArtisanBlockState() instanceof ArtisanSkullState state) {
            Skull skull = (Skull) bukkitBlock.getState();
            final PlayerProfile playerProfile = Bukkit.createProfile(UUID.randomUUID());
            playerProfile.setProperty(new ProfileProperty("textures", state.getUrlBase64()));
            skull.setPlayerProfile(playerProfile);
            skull.update(true);
        }
    }

    public static boolean hasNextStage(ArtisanBlockData artisanBlockData) {
        return artisanBlockData.stage() < artisanBlockData.getArtisanBlock().getTotalStates() - 1;
    }

    public static ArtisanBlockData getNextStage(ArtisanBlockData artisanBlockData) {
        return ArtisanBlockData.builder()
                .blockId(artisanBlockData.blockId())
                .location(artisanBlockData.getLocation())
                .stage(artisanBlockData.stage() + 1)
                .build();
    }

}
