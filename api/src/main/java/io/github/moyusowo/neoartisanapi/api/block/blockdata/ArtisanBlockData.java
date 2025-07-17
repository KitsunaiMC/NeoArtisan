package io.github.moyusowo.neoartisanapi.api.block.blockdata;

import io.github.moyusowo.neoartisanapi.api.block.block.ArtisanBaseBlock;
import io.github.moyusowo.neoartisanapi.api.block.blockstate.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.block.task.LifecycleTaskManager;
import io.github.moyusowo.neoartisanapi.api.util.BuilderFactoryUtil;
import io.github.moyusowo.neoartisanapi.api.block.gui.ArtisanBlockGUI;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
     * @return 0到{@link ArtisanBlock#getTotalStates()}之间的整数
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
     * @return 可读写的bukkitAPI数据容器，生命周期与方块实例相同
     */
    @NotNull
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
