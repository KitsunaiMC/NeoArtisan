package io.github.moyusowo.neoartisanapi.api.block.event;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlock;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;

public class ArtisanBlockBreakEvent extends BlockBreakEvent {

    protected ArtisanBlock artisanBlock;

    public ArtisanBlockBreakEvent(@NotNull Block theBlock, @NotNull Player player, @NotNull ArtisanBlock artisanBlock) {
        super(theBlock, player);
        this.artisanBlock = artisanBlock;
    }

    public ArtisanBlock getArtisanBlock() {
        return this.artisanBlock;
    }

}
