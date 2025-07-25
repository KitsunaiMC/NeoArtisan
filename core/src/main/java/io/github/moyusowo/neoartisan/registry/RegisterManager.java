package io.github.moyusowo.neoartisan.registry;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.test.BlockTest;
import io.github.moyusowo.neoartisan.test.ItemTest;
import io.github.moyusowo.neoartisan.test.RecipeTest;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisan.util.init.Initializer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

public final class RegisterManager implements Listener {

    public static final IllegalAccessError REGISTRY_CLOSED = new IllegalAccessError("Registry closed! Please register in the Enable method.");

    private RegisterManager() {}

    private static Status status = Status.CLOSED;

    @InitMethod(priority = InitPriority.REGISTRY_OPEN)
    static void openRegister() {
        status = Status.OPEN;
    }

    @InitMethod(priority = InitPriority.LISTENER)
    static void init() {
        NeoArtisan.registerListener(new RegisterManager());
    }

    public static boolean isOpen() {
        return status == Status.OPEN;
    }

    private enum Status {
        OPEN,
        CLOSED
    }

    @EventHandler
    public void onStartup(ServerLoadEvent event) {
        if (event.getType() == ServerLoadEvent.LoadType.STARTUP) {
            ItemTest.register();
            RecipeTest.register();
            BlockTest.register();
            status = Status.CLOSED;
            Initializer.executeStartup();
        }
    }

}
