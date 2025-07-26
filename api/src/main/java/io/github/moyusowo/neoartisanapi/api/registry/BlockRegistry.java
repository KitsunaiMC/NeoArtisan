package io.github.moyusowo.neoartisanapi.api.registry;

import io.github.moyusowo.neoartisanapi.api.block.block.base.ArtisanBaseBlock;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * 自定义方块注册表，负责管理所有 {@link ArtisanBaseBlock} 的注册和查询。
 * <p>
 * 本注册表通过 {@link io.github.moyusowo.neoartisanapi.api.registry.Registries#BLOCK} 获取实例，
 * 所有注册操作必须在插件的 `onEnable` 方法执行
 * </p>
 *
 * @see ArtisanBaseBlock 自定义方块接口
 */
@ApiStatus.NonExtendable
public interface BlockRegistry {

    /**
     * 注册自定义方块到中央注册表
     *
     * @param artisanBlock 要注册的自定义方块实例（非null）
     * @throws IllegalArgumentException 如果方块ID已存在或参数无效
     */
    void register(@NotNull ArtisanBaseBlock artisanBlock);

    /**
     * 检查指定ID是否已注册为自定义方块
     *
     * @param blockId 要检查的命名空间键（非null）
     * @return 如果ID对应已注册方块返回true
     */
    boolean isArtisanBlock(NamespacedKey blockId);

    /**
     * 获取已注册的自定义方块实例
     *
     * @param blockId 方块的命名空间键（非null）
     * @return 对应的自定义方块实例
     * @throws IllegalArgumentException 如果ID未注册
     */
    @NotNull ArtisanBaseBlock getArtisanBlock(@NotNull NamespacedKey blockId);

}
