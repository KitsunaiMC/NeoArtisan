package io.github.moyusowo.neoartisan.block.network;

import com.sun.jdi.InternalException;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public final class PaletteFields {
    private PaletteFields() {}

    public static final Field SINGLETON_PALETTE_STATE, MAP_PALATTE_ID_TO_STATE, MAP_PALETTE_STATE_TO_ID, LIST_PALETTE_DATA;

    static {
        try {
            SINGLETON_PALETTE_STATE = Class.forName("com.github.retrooper.packetevents.protocol.world.chunk.palette.SingletonPalette").getDeclaredField("state");
            SINGLETON_PALETTE_STATE.setAccessible(true);
            MAP_PALETTE_STATE_TO_ID = Class.forName("com.github.retrooper.packetevents.protocol.world.chunk.palette.MapPalette").getDeclaredField("stateToId");
            MAP_PALETTE_STATE_TO_ID.setAccessible(true);
            MAP_PALATTE_ID_TO_STATE = Class.forName("com.github.retrooper.packetevents.protocol.world.chunk.palette.MapPalette").getDeclaredField("idToState");
            MAP_PALATTE_ID_TO_STATE.setAccessible(true);
            LIST_PALETTE_DATA = Class.forName("com.github.retrooper.packetevents.protocol.world.chunk.palette.ListPalette").getDeclaredField("data");
            LIST_PALETTE_DATA.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new InternalException("No such field: " + e);
        } catch (ClassNotFoundException e) {
            throw new InternalException("Class not found: " + e);
        }
    }

    public static Object getField(@NotNull Field field, @NotNull Object obj) {
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new InternalException("Illegal access: " + e);
        }
    }

    public static void setField(@NotNull Field field, @NotNull Object obj, @NotNull Object value) {
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw new InternalException("Illegal access: " + e);
        }
    }
}
