package io.github.moyusowo.neoartisanapi.api.block;

import io.github.moyusowo.neoartisanapi.api.block.block.base.ArtisanBaseBlock;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

/**
 * 自定义方块注册表，负责管理所有 {@link ArtisanBlock} 的注册和查询。
 * <p>
 * 本注册表通过 {@link io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI#getBlockRegistry()} 获取实例，
 * 所有注册操作必须通过 {@link io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI.Register} 注解标记的方法执行。
 * </p>
 *
 * @see ArtisanBlock 自定义方块接口
 * @see io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI.Register 注册方法标记注解
 */
public interface BlockRegistry {

    /**
     * 注册自定义方块到中央注册表
     * <p>
     * <b>重要安全限制：</b>
     * <ul>
     *   <li>必须通过 {@link io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI.Register} 注解标记的方法调用</li>
     *   <li>直接手动调用将抛出错误提示</li>
     *   <li>每个方块ID只能注册一次</li>
     * </ul>
     * </p>
     *
     * @param artisanBlock 要注册的自定义方块实例（非null）
     * @throws IllegalArgumentException 如果方块ID已存在或参数无效
     * @see io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI.Register 合法的注册入口注解
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
