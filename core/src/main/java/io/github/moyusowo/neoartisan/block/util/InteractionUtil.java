package io.github.moyusowo.neoartisan.block.util;

import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;
import org.bukkit.block.data.type.*;

import java.util.Set;

public final class InteractionUtil {

    private InteractionUtil() {}

    private static final Set<Class<?>> INTERACTABLE = Set.of(
            Barrel.class,
            Bed.class,
            Bell.class,
            BrewingStand.class,
            Cake.class,
            Chest.class,
            CommandBlock.class,
            Comparator.class,
            Crafter.class,
            DaylightDetector.class,
            Dispenser.class,
            Door.class,
            EnderChest.class,
            Furnace.class,
            Grindstone.class,
            HangingSign.class,
            Hopper.class,
            NoteBlock.class,
            Openable.class,
            Repeater.class,
            RespawnAnchor.class,
            Sign.class,
            Switch.class,
            TrapDoor.class,
            WallHangingSign.class,
            WallSign.class
    );

    public static boolean isInteractable(Block block) {
        return INTERACTABLE.stream().anyMatch(clazz -> clazz.isInstance(block.getBlockData()));
    }
}
