package io.github.moyusowo.neoartisanapi.api.block.event.common;

import io.github.moyusowo.neoartisanapi.api.block.block.base.ArtisanBaseBlock;
import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Event is called when a player places a custom block.
 * <p>
 * Extends {@link BlockPlaceEvent} and includes the placed custom block instance.
 * Can be used to cancel the placement or modify the block state after placement.
 * </p>
 *
 * @see ArtisanBaseBlock custom block type
 */
public class ArtisanBlockPlaceEvent extends BlockPlaceEvent {

    private static final HandlerList handlers = new HandlerList();

    protected ArtisanBaseBlock artisanBaseBlock;
    protected ArtisanBlockData placedArtisanBlockData;

    /**
     * Constructs a custom block place event
     *
     * @param placedBlock the placed block instance (non-null)
     * @param replacedBlockState the original block state being replaced (non-null)
     * @param placedAgainst the adjacent block that the new block is placed against (non-null)
     * @param itemInHand the item in the player's hand (non-null)
     * @param thePlayer the player placing the block (non-null)
     * @param canBuild whether the player has build permission
     * @param hand the equipment slot used (non-null)
     * @param artisanBaseBlock the associated custom block definition (non-null)
     * @param placedArtisanBlockData the custom BlockData that will be stored (non-null)
     */
    @SuppressWarnings("UnstableApiUsage")
    public ArtisanBlockPlaceEvent(@NotNull Block placedBlock, @NotNull BlockState replacedBlockState, @NotNull Block placedAgainst, @NotNull ItemStack itemInHand, @NotNull Player thePlayer, boolean canBuild, @NotNull EquipmentSlot hand, @NotNull ArtisanBaseBlock artisanBaseBlock, @NotNull ArtisanBlockData placedArtisanBlockData) {
        super(placedBlock, replacedBlockState, placedAgainst, itemInHand, thePlayer, canBuild, hand);
        this.artisanBaseBlock = artisanBaseBlock;
        this.placedArtisanBlockData = placedArtisanBlockData;
    }

    /**
     * Gets the custom block definition of the placed block
     *
     * @return the custom block instance containing block type and state information
     */
    public @NotNull ArtisanBaseBlock getArtisanBlock() {
        return this.artisanBaseBlock;
    }

    /**
     * Gets the custom BlockData that will be stored
     *
     * @return the custom BlockData that will be stored
     * @see ArtisanBlockData
     */
    public ArtisanBlockData getPlacedArtisanBlockData() { return this.placedArtisanBlockData; }

    /**
     * Sets the custom BlockData that will be stored
     *
     * @param placedArtisanBlockData the custom BlockData to be stored
     * @see ArtisanBlockData
     */
    public void setPlacedArtisanBlockData(ArtisanBlockData placedArtisanBlockData) { this.placedArtisanBlockData = placedArtisanBlockData; }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
