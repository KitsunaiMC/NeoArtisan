package io.github.moyusowo.neoartisanapi.api.block.storage;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockType;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCropData;
import io.github.moyusowo.neoartisanapi.api.block.thin.ArtisanThinBlockData;
import io.github.moyusowo.neoartisanapi.api.block.transparent.ArtisanTransparentBlockData;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * 自定义方块数据存储服务，负责管理所有已放置的自定义方块实例。
 * <p>
 * 提供对自定义方块的查询和类型检查功能，支持通过坐标或{@link Block}实例访问。
 * 所有方法都是线程安全的，可在异步环境下调用。
 * </p>
 *
 * @see ArtisanBlockData 基础方块数据接口
 * @see io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI#getArtisanBlockStorage() 获取服务实例
 * @since 1.0.0
 */
public interface ArtisanBlockStorage {

    /**
     * 通过世界坐标获取自定义方块数据
     *
     * @param world 目标世界
     * @param x X坐标
     * @param y Y坐标
     * @param z Z坐标
     * @return 对应的方块数据
     * @throws IllegalArgumentException 如果不存在
     * @apiNote 使用该方法前应先调用 {@link #isArtisanBlock(World, int, int, int)} 检查
     */
    ArtisanBlockData getArtisanBlockData(@NotNull World world, int x, int y, int z);

    /**
     * 通过方块实例获取自定义方块数据
     *
     * @param block 目标方块（非null）
     * @return 对应的方块数据
     * @throws IllegalArgumentException 如果不存在
     * @apiNote 使用该方法前应先调用 {@link #isArtisanBlock(Block)} 检查
     */
    ArtisanBlockData getArtisanBlockData(@NotNull Block block);

    /**
     * 检查方块是否为已注册的自定义方块
     *
     * @param block 待检查方块（非null）
     * @return 如果是有效的自定义方块返回true
     */
    boolean isArtisanBlock(@NotNull Block block);

    /**
     * 检查坐标位置是否为已注册的自定义方块
     *
     * @param world 目标世界（非null）
     * @param x X坐标
     * @param y Y坐标
     * @param z Z坐标
     * @return 如果是有效的自定义方块返回true
     * @throws IllegalArgumentException 如果坐标超出世界边界
     */
    boolean isArtisanBlock(@NotNull World world, int x, int y, int z);

    /**
     * 检查方块是否为自定义作物（实验性功能）
     *
     * @param block 待检查方块（非null）
     * @return 如果是则返回true
     */
    @Deprecated(since = "1.0.1", forRemoval = true)
    @ApiStatus.Experimental
    default boolean isArtisanCrop(@NotNull Block block) {
        return this.isArtisanCrop(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    /**
     * 检查坐标位置是否为自定义作物（实验性功能）
     *
     * @param world 目标世界（非null）
     * @param x X坐标
     * @param y Y坐标
     * @param z Z坐标
     * @return 如果是则返回true
     */
    @Deprecated(since = "1.0.1", forRemoval = true)
    @ApiStatus.Experimental
    default boolean isArtisanCrop(World world, int x, int y, int z) {
        return this.isArtisanBlock(world, x, y, z) && (this.getArtisanBlockData(world, x, y, z) instanceof ArtisanCropData);
    }

    /**
     * 检查方块是否为薄型自定义方块（实验性功能）
     *
     * @param block 待检查方块（非null）
     * @return 如果是则返回true
     */
    @Deprecated(since = "1.0.1", forRemoval = true)
    @ApiStatus.Experimental
    default boolean isArtisanThinBlock(@NotNull Block block) {
        return this.isArtisanThinBlock(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    /**
     * 检查坐标位置是否为薄型自定义方块（实验性功能）
     *
     * @param world 目标世界（非null）
     * @param x X坐标
     * @param y Y坐标
     * @param z Z坐标
     * @return 如果是则返回true
     */
    @Deprecated(since = "1.0.1", forRemoval = true)
    @ApiStatus.Experimental
    default boolean isArtisanThinBlock(@NotNull World world, int x, int y, int z) {
        return this.isArtisanBlock(world, x, y, z) && (this.getArtisanBlockData(world, x, y, z) instanceof ArtisanThinBlockData);
    }

    /**
     * 检查方块是否为透明自定义方块（实验性功能）
     *
     * @param block 待检查方块（非null）
     * @return 如果是则返回true
     */
    @Deprecated(since = "1.0.1", forRemoval = true)
    @ApiStatus.Experimental
    default boolean isArtisanTransparentBlock(@NotNull Block block) {
        return this.isArtisanTransparentBlock(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    /**
     * 检查坐标位置是否为透明自定义方块（实验性功能）
     *
     * @param world 目标世界（非null）
     * @param x X坐标
     * @param y Y坐标
     * @param z Z坐标
     * @return 如果是则返回true
     */
    @Deprecated(since = "1.0.1", forRemoval = true)
    @ApiStatus.Experimental
    default boolean isArtisanTransparentBlock(@NotNull World world, int x, int y, int z) {
        return this.isArtisanBlock(world, x, y, z) && (this.getArtisanBlockData(world, x, y, z) instanceof ArtisanTransparentBlockData);
    }
}
