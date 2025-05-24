package io.github.moyusowo.neoartisanapi.api.block.thin;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlock;
import io.github.moyusowo.neoartisanapi.api.block.gui.GUICreator;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ArtisanThinBlock extends ArtisanBlock {

    static Builder builder() {
        return Bukkit.getServicesManager().load(Builder.class);
    }

    @NotNull ArtisanThinBlockState getState(int n);

    interface Builder {

        Builder blockId(NamespacedKey blockId);

        Builder states(List<ArtisanThinBlockState> states);

        Builder guiCreator(GUICreator creator);

        ArtisanThinBlock build();
    }
}
