package io.github.moyusowo.neoartisanapi.api.block.transparent;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlock;
import io.github.moyusowo.neoartisanapi.api.block.gui.ArtisanBlockGUI;
import io.github.moyusowo.neoartisanapi.api.block.gui.GUICreator;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface ArtisanTransparentBlock extends ArtisanBlock {

    static Builder builder() {
        return Bukkit.getServicesManager().load(Builder.class);
    }

    boolean canBurn();

    @NotNull ArtisanTransparentBlockState getState(int n);

    interface Builder {

        Builder blockId(NamespacedKey blockId);

        Builder states(List<ArtisanTransparentBlockState> states);

        Builder canBurn(boolean canBurn);

        Builder guiCreator(GUICreator creator);

        ArtisanTransparentBlock build();
    }
}
