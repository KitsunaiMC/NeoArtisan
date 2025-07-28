package io.github.moyusowo.neoartisanapi.api.block.event.common;

import io.github.moyusowo.neoartisanapi.api.block.block.base.ArtisanBaseBlock;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Event is called when a player breaks a custom block.
 * <p>
 * Extends {@link BlockBreakEvent} and includes the broken custom block instance.
 * Can be used to cancel the breaking action or modify dropped items (through parent methods).
 * </p>
 *
 * @see ArtisanBaseBlock
 */
public class ArtisanBlockBreakEvent extends BlockBreakEvent {

    private static final HandlerList handlers = new HandlerList();

    protected ArtisanBaseBlock artisanBaseBlock;

    /**
     * Constructs a custom block break event
     *
     * @param theBlock the broken block instance (non-null)
     * @param player the player breaking the block (non-null)
     * @param artisanBaseBlock the associated custom block definition (non-null)
     */
    public ArtisanBlockBreakEvent(@NotNull Block theBlock, @NotNull Player player, @NotNull ArtisanBaseBlock artisanBaseBlock) {
        super(theBlock, player);
        this.artisanBaseBlock = artisanBaseBlock;
    }

    /**
     * Gets the custom block definition of the broken block
     *
     * @return the custom block instance
     */
    public @NotNull ArtisanBaseBlock getArtisanBlock() {
        return this.artisanBaseBlock;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }

}
