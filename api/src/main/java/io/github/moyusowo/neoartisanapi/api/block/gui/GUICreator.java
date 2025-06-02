package io.github.moyusowo.neoartisanapi.api.block.gui;

import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

/**
 * GUI创建函数式接口，用于延迟初始化方块GUI
 * <p>
 * 通常用于 {@link io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlock#createGUI(Location)} 的实现
 * </p>
 *
 * @see ArtisanBlockGUI 创建的GUI类型
 * @since 1.0.0
 */
@FunctionalInterface
public interface GUICreator {

    /**
     * 在指定位置创建GUI实例
     * @param location GUI绑定的方块位置
     * @return 新建的GUI实例（不为null）
     */
    @NotNull
    ArtisanBlockGUI create(Location location);

}
