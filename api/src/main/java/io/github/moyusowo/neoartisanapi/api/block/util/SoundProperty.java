package io.github.moyusowo.neoartisanapi.api.block.util;

import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;

/**
 * 表示一个可配置的音效属性，封装音效键、音量和音调参数。
 * <p>
 * 提供静态工厂方法，支持从 {@link Sound} 枚举或字符串键创建音效配置。
 * 本类为不可变类，所有实例一旦创建后属性不可更改。
 * </p>
 *
 * <p><b>使用示例：</b></p>
 * <pre>{@code
 * // 从Sound枚举创建
 * SoundProperty prop1 = SoundProperty.of(Sound.ENTITY_PLAYER_LEVELUP, 0.8f, 1.2f);
 *
 * // 从字符串键创建（适用于自定义音效）
 * SoundProperty prop2 = SoundProperty.of("custom.sound.example", 1.0f, 1.0f);
 * }</pre>
 *
 * @see Sound Bukkit音效枚举
 */
public final class SoundProperty {

    /**
     * 音效资源键
     * <p>
     * - 对于原版音效，格式为Minecraft命名空间键（如"minecraft:entity.player.levelup"）<br>
     * - 对于自定义音效，需与资源包中的sounds.json定义一致
     * </p>
     */
    public final NamespacedKey key;

    /**
     * 音效播放音量
     * <p>
     * 取值范围通常为0.0（无声）到1.0（最大），但支持更大值<br>
     * 注意：实际可听范围与客户端设置有关
     * </p>
     */
    public final float volume;

    /**
     * 音效播放音调
     * <p>
     * 取值范围建议0.5（低音）到2.0（高音）<br>
     * 1.0表示原始音高
     * </p>
     */
    public final float pitch;

    /**
     * 私有构造器，强制使用工厂方法创建实例
     *
     * @param key 音效资源键（非null）
     * @param volume 音量（建议0.0-1.0）
     * @param pitch 音调（建议0.5-2.0）
     */
    private SoundProperty(NamespacedKey key, float volume, float pitch) {
        this.key = key;
        this.volume = volume;
        this.pitch = pitch;
    }

    /**
     * 从Bukkit Sound枚举创建音效属性
     *
     * @param sound 原版音效枚举（非null）
     * @param volume 音量
     * @param pitch 音调
     * @return 音效属性实例，如果sound未注册返回null
     * @throws IllegalArgumentException 当不能在bukkit注册表内检测到该音效时抛出
     */
    public static @NotNull SoundProperty of(@NotNull Sound sound, float volume, float pitch) {
        var namespacedKey = Registry.SOUNDS.getKey(sound);
        if (namespacedKey != null) return new SoundProperty(namespacedKey, volume, pitch);
        else throw new IllegalArgumentException("Could not find that sound in bukkit Registry!");
    }

    /**
     * 从字符串键创建音效属性
     *
     * @param key 音效资源键（格式："namespace:path"或"path"）
     * @param volume 音量
     * @param pitch 音调
     * @return 音效属性实例（非null）
     */
    public static @NotNull SoundProperty of(@NotNull NamespacedKey key, float volume, float pitch) {
        return new SoundProperty(key, volume, pitch);
    }

    /**
     * 从Bukkit Sound枚举创建音效属性（默认音量1.0，音调1.0）
     *
     * @param sound 原版音效枚举（非null）
     * @return 音效属性实例，如果sound未注册返回null
     */
    public static @NotNull SoundProperty of(@NotNull Sound sound) {
        return of(sound, 1.0f, 1.0f);
    }

    /**
     * 从字符串键创建音效属性（默认音量1.0，音调1.0）
     *
     * @param key 音效资源键
     * @return 音效属性实例（非null）
     */
    public static @NotNull SoundProperty of(@NotNull NamespacedKey key) {
        return of(key, 1.0f, 1.0f);
    }
}

