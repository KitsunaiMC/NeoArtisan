package io.github.moyusowo.neoartisanapi.api.block.event;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlock;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ArtisanBlockPlaceEvent extends BlockPlaceEvent {

    protected ArtisanBlock artisanBlock;

    public ArtisanBlockPlaceEvent(@NotNull Block placedBlock, @NotNull BlockState replacedBlockState, @NotNull Block placedAgainst, @NotNull ItemStack itemInHand, @NotNull Player thePlayer, boolean canBuild, @NotNull EquipmentSlot hand, @NotNull ArtisanBlock artisanBlock) {
        super(placedBlock, replacedBlockState, placedAgainst, itemInHand, thePlayer, canBuild, hand);
        this.artisanBlock = artisanBlock;
    }

    public ArtisanBlock getArtisanBlock() {
        return this.artisanBlock;
    }
}
