package io.github.moyusowo.neoartisan.util;

import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class ReflectionUtil {
    private static final ConcurrentHashMap<String, Field> FIELD_CACHE = new ConcurrentHashMap<>();

    public static final Field BLOCK_UPDATE_POS, BLOCK_UPDATE_STATE, SECTION_UPDATE_POS, SECTION_BLOCK_UPDATE_POS, SECTION_BLOCK_UPDATE_STATE, CHUNK_DATA;

    static {
        try {
            BLOCK_UPDATE_POS = ClientboundBlockUpdatePacket.class.getDeclaredField("pos");
            BLOCK_UPDATE_POS.setAccessible(true);
            BLOCK_UPDATE_STATE = ClientboundBlockUpdatePacket.class.getDeclaredField("blockState");
            BLOCK_UPDATE_STATE.setAccessible(true);
            SECTION_UPDATE_POS = ClientboundSectionBlocksUpdatePacket.class.getDeclaredField("sectionPos");
            SECTION_UPDATE_POS.setAccessible(true);
            SECTION_BLOCK_UPDATE_POS = ClientboundSectionBlocksUpdatePacket.class.getDeclaredField("positions");
            SECTION_BLOCK_UPDATE_POS.setAccessible(true);
            SECTION_BLOCK_UPDATE_STATE = ClientboundSectionBlocksUpdatePacket.class.getDeclaredField("states");
            SECTION_BLOCK_UPDATE_STATE.setAccessible(true);
            CHUNK_DATA = ClientboundLevelChunkPacketData.class.getDeclaredField("buffer");
            CHUNK_DATA.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object getField(Object obj, String fieldName) throws Exception {
        String cacheKey = obj.getClass().getName() + "#" + fieldName;
        Field field = FIELD_CACHE.computeIfAbsent(cacheKey, k -> {
            try {
                Field f = obj.getClass().getDeclaredField(fieldName);
                f.setAccessible(true);
                return f;
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Field not found: " + fieldName, e);
            }
        });
        return field.get(obj);
    }
    public static void setField(Object obj, String fieldName, Object value) throws Exception {
        String cacheKey = obj.getClass().getName() + "#" + fieldName;
        Field field = FIELD_CACHE.computeIfAbsent(cacheKey, k -> {
            try {
                Field f = obj.getClass().getDeclaredField(fieldName);
                f.setAccessible(true);
                return f;
            } catch (NoSuchFieldException e) {
                throw new RuntimeException("Field not found: " + fieldName, e);
            }
        });
        field.set(obj, value);
    }
}
