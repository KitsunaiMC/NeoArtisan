package io.github.moyusowo.neoartisanapi.api.block.event.common;

import io.github.moyusowo.neoartisanapi.api.block.block.base.ArtisanBaseBlock;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockExpEvent;
import org.jetbrains.annotations.NotNull;

/**
 * Event is called when a custom block may drop due to losing support
 * (e.g., the block below it is destroyed).
 * <p>
 * Extends {@link BlockExpEvent} and cannot be cancelled.
 * </p>
 *
 * @see ArtisanBaseBlock
 */
public class ArtisanBlockLoseSupportEvent extends BlockExpEvent {

    private static final HandlerList handlers = new HandlerList();
    private final ArtisanBaseBlock artisanBaseBlock;
    private boolean dropItems;

    /**
     * Constructs a lose support event
     *
     * @param theBlock the block instance that may drop (non-null)
     * @param artisanBaseBlock the associated custom block definition (non-null)
     */
    public ArtisanBlockLoseSupportEvent(@NotNull final Block theBlock, @NotNull ArtisanBaseBlock artisanBaseBlock) {
        super(theBlock, 0);
        this.dropItems = true;
        this.artisanBaseBlock = artisanBaseBlock;
    }

    /**
     * Sets whether to generate dropped items
     *
     * @param dropItems if true, normal dropped items will be generated;
     *                  if false, no items will drop
     */
    public void setDropItems(boolean dropItems) {
        this.dropItems = dropItems;
    }

    /**
     * Checks if dropped items will be generated
     *
     * @return current drop items status
     */
    public boolean isDropItems() {
        return this.dropItems;
    }

    /**
     * Gets the associated custom block definition
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
