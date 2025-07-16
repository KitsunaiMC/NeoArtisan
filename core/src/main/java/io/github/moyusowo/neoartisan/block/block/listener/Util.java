package io.github.moyusowo.neoartisan.block.block.listener;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import io.github.moyusowo.neoartisan.block.storage.internal.ArtisanBlockStorageInternal;
import io.github.moyusowo.neoartisan.util.BlockPos;
import io.github.moyusowo.neoartisanapi.api.block.blockdata.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.blockstate.ArtisanSkullState;
import io.github.moyusowo.neoartisanapi.api.block.util.SoundProperty;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Skull;
import org.bukkit.block.data.BlockData;

import java.util.UUID;

final class Util {
    private Util() {}

    static void place(Block bukkitBlock, ArtisanBlockData artisanBlockData) {
        World world = bukkitBlock.getWorld();
        BlockPos blockPos = new BlockPos(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ());
        ArtisanBlockStorageInternal.getInternal().placeArtisanBlock(world.getUID(), blockPos, artisanBlockData);
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

    static void replace(Block bukkitBlock, ArtisanBlockData artisanBlockData) {
        World world = bukkitBlock.getWorld();
        BlockPos pos = new BlockPos(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ());
        ArtisanBlockStorageInternal.getInternal().replaceArtisanBlock(world.getUID(), pos, artisanBlockData);
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

}
