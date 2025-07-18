package io.github.moyusowo.neoartisan.block.state.listener;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import org.bukkit.event.Listener;

final class ArtisanBlockBreakListener implements Listener {
    private ArtisanBlockBreakListener() {}

    @InitMethod(priority = InitPriority.LISTENER)
    public static void init() {
        NeoArtisan.registerListener(new ArtisanBlockBreakListener());
    }
}
