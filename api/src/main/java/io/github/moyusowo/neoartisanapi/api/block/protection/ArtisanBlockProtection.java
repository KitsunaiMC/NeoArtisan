package io.github.moyusowo.neoartisanapi.api.block.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * 方块保护检查服务，用于验证玩家在指定位置的交互权限。
 * <p>
 * 本服务为 <b>核心安全机制</b>，所有涉及方块破坏、放置和交互的操作都必须通过此接口进行权限验证。
 * 内置实现已集成主流保护插件（如 WorldGuard、GriefPrevention），确保与服务器生态兼容。
 * </p>
 *
 * <p><b>附属插件开发必读：</b></p>
 * <ol>
 *   <li>在实现任何方块操作逻辑前，<strong>必须</strong>调用本接口方法进行权限检查</li>
 *   <li>忽略权限检查将导致：<br>
 *       - 与其他保护插件冲突<br>
 *       - 玩家绕过领地权限<br>
 *       - 产生安全漏洞</li>
 * </ol>
 *
 * @see io.github.moyusowo.neoartisanapi.api.block.protection.Protections#BLOCK 获取服务实例
 */
public interface ArtisanBlockProtection {

    /**
     * 检查玩家是否可以破坏指定位置的方块
     *
     * @param player   操作玩家（非null）
     * @param location 方块位置（非null）
     * @return 若有破坏权限返回 true，否则 false
     *
     * @apiNote 附属插件实现破坏逻辑时必须调用此方法
     */
    boolean canBreak(@NotNull Player player, @NotNull Location location);

    /**
     * 检查玩家是否可以在指定位置放置方块
     *
     * @param player   操作玩家（非null）
     * @param location 目标位置（非null）
     * @return 若有放置权限返回 true，否则 false
     *
     * @apiNote 在方块放置事件前必须验证此权限
     */
    boolean canPlace(@NotNull Player player, @NotNull Location location);

    /**
     * 检查玩家是否可以与指定位置的方块交互（如打开容器）
     *
     * @param player   操作玩家（非null）
     * @param location 方块位置（非null）
     * @return 若有交互权限返回 true，否则 false
     *
     * @apiNote 适用于箱子、熔炉或自定义GUI等容器的交互权限验证
     */
    boolean canInteract(@NotNull Player player, @NotNull Location location);
}
