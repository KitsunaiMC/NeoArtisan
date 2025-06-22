package io.github.moyusowo.neoartisanapi.api.block.base;

import io.github.moyusowo.neoartisanapi.api.block.gui.ArtisanBlockGUI;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

/**
 * 表示世界中实际存在的自定义方块实例的数据容器。
 * <p>
 * 此接口封装了一个已放置的自定义方块的所有运行时状态，包括：
 * <ul>
 *   <li>物理位置和显示状态</li>
 *   <li>关联的GUI实例（如果有）</li>
 *   <li>自定义持久化数据存储</li>
 *   <li>当前方块状态索引</li>
 * </ul>
 * </p>
 *
 * @see ArtisanBlock 对应的自定义方块类型定义
 * @see ArtisanBlockState 方块状态系统
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public interface ArtisanBlockData {

    /**
     * 获取与此方块关联的GUI
     *
     * @return GUI实例，如果方块不支持则返回 {@code null}
     */
    @Nullable ArtisanBlockGUI getGUI();

    /**
     * 获取方块在世界中的物理位置
     *
     * @return 不可变的位置对象，包含世界、坐标信息
     */
    Location getLocation();

    /**
     * 获取方块的ID
     *
     * @return 与 {@link ArtisanBlock#getBlockId()} 一致的命名空间键
     * @see ArtisanBlock#getBlockId()
     */
    NamespacedKey blockId();

    /**
     * 获取当前方块状态索引
     *
     * @return 0到{@link ArtisanBlock#getTotalStates()}之间的整数
     * @implNote 该值应与 {@link #getArtisanBlockState()} 返回的状态一致
     */
    int stage();

    /**
     * 获取关联的自定义方块定义
     *
     * @return 不可变的方块类型实例
     */
    ArtisanBlock getArtisanBlock();

    /**
     * 获取当前方块状态实例
     *
     * @return 通过 {@link ArtisanBlock#getState(int)} 获取的不可变状态
     * @see ArtisanBlock#getState(int)
     */
    ArtisanBlockState getArtisanBlockState();

    /**
     * 获取方块的持久化数据容器
     * <p>
     * 用于存储一些要添加的其他自定义数据
     * </p>
     *
     * @return 可读写的bukkitAPI数据容器，生命周期与方块实例相同
     */
    PersistentDataContainer getPersistentDataContainer();

    @ApiStatus.Internal
    interface BaseBuilder {}
}
