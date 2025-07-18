package io.github.moyusowo.neoartisanapi.api.block.data;

import io.github.moyusowo.neoartisanapi.api.block.block.base.ArtisanBaseBlock;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.block.task.LifecycleTaskManager;
import io.github.moyusowo.neoartisanapi.api.util.BuilderFactoryUtil;
import io.github.moyusowo.neoartisanapi.api.block.gui.ArtisanBlockGUI;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 表示世界中实际存在的自定义方块实例的数据容器。
 * <p>
 * 此接口封装了一个已放置的自定义方块的所有运行时状态，包括：
 * <ul>
 *   <li>物理位置和显示状态</li>
 *   <li>关联的GUI实例（如果有）</li>
 *   <li>方块生命周期事件管理器</li>
 *   <li>自定义持久化数据储存器</li>
 *   <li>当前方块状态索引</li>
 * </ul>
 * </p>
 *
 * @see ArtisanBaseBlock 自定义方块类型定义
 * @see ArtisanBaseBlockState 方块状态系统
 */
public interface ArtisanBlockData {
    @NotNull
    static Builder builder() {
        return BuilderFactoryUtil.getBuilder(BuilderFactory.class).builder();
    }

    interface BuilderFactory {
        @NotNull
        Builder builder();
    }

    /**
     * 获取与此方块关联的GUI
     *
     * @return GUI实例，如果方块不支持则返回 {@code null}
     */
    @Nullable
    ArtisanBlockGUI getGUI();

    /**
     * 获取方块在世界中的物理位置
     *
     * @return 不可变的位置对象，包含世界、坐标信息
     */
    @NotNull
    Location getLocation();

    /**
     * 获取方块的ID
     *
     * @return 与 {@link ArtisanBaseBlock#getBlockId()} 一致地命名空间键
     * @see ArtisanBaseBlock#getBlockId()
     */
    @NotNull
    NamespacedKey blockId();

    /**
     * 获取当前方块状态索引
     *
     * @return 0 到 {@link ArtisanBaseBlock#getTotalStates()} 之间的整数（不包括右边界）
     * @implNote 该值应与 {@link #getArtisanBlockState()} 返回的状态一致
     */
    int stage();

    /**
     * 获取关联的自定义方块定义
     *
     * @return 不可变的方块类型实例
     */
    @NotNull
    ArtisanBaseBlock getArtisanBlock();

    /**
     * 获取当前方块状态实例
     *
     * @return 通过 {@link ArtisanBaseBlock#getState(int)} 获取的不可变状态
     * @see ArtisanBaseBlock#getState(int)
     */
    @NotNull
    ArtisanBaseBlockState getArtisanBlockState();

    /**
     * 获取方块的持久化数据容器
     * <p>
     * 用于存储一些要添加的其他自定义数据
     * </p>
     *
     * @return 可读写的bukkitAPI数据容器，生命周期与方块实例相同。若方块不存在方块实体则返回null
     * @see ArtisanBaseBlock#hasBlockEntity()
     */
    @Nullable
    PersistentDataContainer getPersistentDataContainer();

    @NotNull
    LifecycleTaskManager getLifecycleTaskManager();

    interface Builder {
        @NotNull
        Builder location(@NotNull Location location);

        @NotNull
        Builder blockId(@NotNull NamespacedKey blockId);

        @NotNull
        Builder stage(int stage);

        @NotNull
        ArtisanBlockData build();
    }
}
