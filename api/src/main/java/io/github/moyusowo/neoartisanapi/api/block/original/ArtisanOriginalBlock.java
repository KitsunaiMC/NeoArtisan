package io.github.moyusowo.neoartisanapi.api.block.original;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlock;
import io.github.moyusowo.neoartisanapi.api.block.gui.GUICreator;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ArtisanOriginalBlock extends ArtisanBlock {

    static Builder builder() {
        return Bukkit.getServicesManager().load(Builder.class);
    }

    @Override
    @NotNull
    ArtisanOriginalBlockState getState(int n);

    interface Builder {

        Builder blockId(NamespacedKey blockId);

        Builder states(List<ArtisanOriginalBlockState> states);

        Builder guiCreator(GUICreator creator);

        ArtisanOriginalBlock build();
    }
}
