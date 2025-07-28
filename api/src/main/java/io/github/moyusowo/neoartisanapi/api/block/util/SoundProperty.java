package io.github.moyusowo.neoartisanapi.api.block.util;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;


/**
 * Represents a configurable sound property, encapsulating sound key, volume, and pitch parameters.
 * <p>
 * Provides static factory methods to create sound configurations from {@link Sound} enum or string keys.
 * This class is immutable - all instances have fixed properties once created.
 * </p>
 *
 *
 * @see Sound Bukkit sounds
 */
public final class SoundProperty {

    /**
     * Sound namespaced key
     * <p>
     * - For vanilla sounds, uses Minecraft namespace key format (e.g. "minecraft:entity.player.levelup")<br>
     * - For custom sounds, must match sounds.json definition in resource pack
     * </p>
     */
    public final NamespacedKey key;

    /**
     * Sound playback volume
     * <p>
     * Typical range is 0.0 (silent) to 1.0 (maximum), but larger values are supported<br>
     * Note: Actual audible range depends on client settings
     * </p>
     */
    public final float volume;

    /**
     * Sound playback pitch
     * <p>
     * Recommended range is 0.5 (low pitch) to 2.0 (high pitch)<br>
     * 1.0 represents original pitch
     * </p>
     */
    public final float pitch;

    private SoundProperty(NamespacedKey key, float volume, float pitch) {
        this.key = key;
        this.volume = volume;
        this.pitch = pitch;
    }

    /**
     * Creates sound property from Bukkit Sound enum
     *
     * @param sound vanilla sound enum (non-null)
     * @param volume volume
     * @param pitch pitch
     * @return sound property instance, or null if sound is not registered
     * @throws IllegalArgumentException when the sound cannot be found in bukkit registry
     */
    public static @NotNull SoundProperty of(@NotNull Sound sound, float volume, float pitch) {
        var namespacedKey = Registry.SOUNDS.getKey(sound);
        if (namespacedKey != null) return new SoundProperty(namespacedKey, volume, pitch);
        else throw new IllegalArgumentException("Could not find that sound in bukkit Registry!");
    }

    /**
     * Creates sound property from string key
     *
     * @param key sound resource key (format: "namespace:path" or "path")
     * @param volume volume
     * @param pitch pitch
     * @return sound property instance (non-null)
     */
    public static @NotNull SoundProperty of(@NotNull NamespacedKey key, float volume, float pitch) {
        return new SoundProperty(key, volume, pitch);
    }

    /**
     * Creates sound property from Bukkit Sound enum (default volume 1.0, pitch 1.0)
     *
     * @param sound vanilla sound enum (non-null)
     * @return sound property instance, or null if sound is not registered
     */
    public static @NotNull SoundProperty of(@NotNull Sound sound) {
        return of(sound, 1.0f, 1.0f);
    }

    /**
     * Creates sound property from string key (default volume 1.0, pitch 1.0)
     *
     * @param key sound resource key
     * @return sound property instance (non-null)
     */
    public static @NotNull SoundProperty of(@NotNull NamespacedKey key) {
        return of(key, 1.0f, 1.0f);
    }
}

