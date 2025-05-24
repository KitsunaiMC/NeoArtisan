package io.github.moyusowo.neoartisanapi.api.block.event;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlock;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.block.BlockExpEvent;
import org.jetbrains.annotations.NotNull;

public class ArtisanBlockLoseSupportEvent extends BlockExpEvent {

    private final ArtisanBlock artisanBlock;
    private boolean dropItems;

    public ArtisanBlockLoseSupportEvent(@NotNull final Block theBlock, @NotNull ArtisanBlock artisanBlock) {
        super(theBlock, 0);
        this.dropItems = true;
        this.artisanBlock = artisanBlock;
    }

    public void setDropItems(boolean dropItems) {
        this.dropItems = dropItems;
    }

    public boolean isDropItems() {
        return this.dropItems;
    }

    public ArtisanBlock getArtisanBlock() {
        return this.artisanBlock;
    }
}
