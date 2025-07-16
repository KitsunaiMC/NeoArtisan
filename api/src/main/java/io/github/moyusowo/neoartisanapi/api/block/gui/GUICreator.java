package io.github.moyusowo.neoartisanapi.api.block.gui;

import org.bukkit.Location;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * GUI创建函数式接口，用于在方块放置时延迟创建GUI
 * <p>
 * <b>使用流程：</b>
 * <ol>
 *   <li>附属插件在方块注册时创建GUICreator实例</li>
 *   <li>在GUICreator实现中捕获固定参数（大小/标题等）</li>
 *   <li>框架在方块放置时提供Location调用create()</li>
 * </ol>
 *
 */
@FunctionalInterface
public interface GUICreator {

    /**
     * 在指定位置创建GUI实例
     * @param location GUI绑定的方块位置
     * @return 新建的GUI实例（不为null）
     */
    @NotNull
    @ApiStatus.Internal
    ArtisanBlockGUI create(Location location);

}
