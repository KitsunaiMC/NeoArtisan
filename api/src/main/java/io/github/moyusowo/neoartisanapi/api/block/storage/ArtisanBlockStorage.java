package io.github.moyusowo.neoartisanapi.api.block.storage;

import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 自定义方块数据存储服务，负责管理所有已放置的自定义方块实例。
 * <p>
 * 提供对自定义方块的查询和类型检查功能，支持通过坐标或{@link Block}实例访问。
 * 所有方法不是线程安全的，都不能异步环境下调用。
 * </p>
 *
 * @see ArtisanBlockData 基础方块数据接口
 * @see io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI#getArtisanBlockStorage() 获取服务实例
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
    @NotNull
    ArtisanBlockData getArtisanBlockData(@NotNull World world, int x, int y, int z);

    /**
     * 通过方块实例获取自定义方块数据
     *
     * @param block 目标方块（非null）
     * @return 对应的方块数据
     * @throws IllegalArgumentException 如果不存在
     * @apiNote 使用该方法前应先调用 {@link #isArtisanBlock(Block)} 检查
     */
    @NotNull
    default ArtisanBlockData getArtisanBlockData(@NotNull Block block) {
        return getArtisanBlockData(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    /**
     * 通过位置实例获取自定义方块数据
     *
     * @param location 目标位置（非null）
     * @return 对应的方块数据
     * @throws IllegalArgumentException 如果不存在
     * @apiNote 使用该方法前应先调用 {@link #isArtisanBlock(Block)} 检查
     */
    @NotNull
    default ArtisanBlockData getArtisanBlockData(@NotNull Location location) {
        return getArtisanBlockData(location.getBlock());
    }

    /**
     * 检查坐标位置是否为已注册的自定义方块
     *
     * @param world 目标世界（非null）
     * @param x X坐标
     * @param y Y坐标
     * @param z Z坐标
     * @return 如果是有效的自定义方块返回true
     */
    boolean isArtisanBlock(@NotNull World world, int x, int y, int z);

    /**
     * 检查方块是否为已注册的自定义方块
     *
     * @param block 待检查方块（非null）
     * @return 如果是有效的自定义方块返回true
     */
    default boolean isArtisanBlock(@NotNull Block block) {
        return isArtisanBlock(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    /**
     * 检查位置对应方块是否为已注册的自定义方块
     *
     * @param location 待检查位置（非null）
     * @return 如果是有效的自定义方块返回true
     */
    default boolean isArtisanBlock(@NotNull Location location) {
        return isArtisanBlock(location.getBlock());
    }

    /**
     * 设置指定位置的自定义方块数据
     *
     * @param world 目标世界（非null）
     * @param x X坐标
     * @param y Y坐标
     * @param z Z坐标
     * @param artisanBlockData 自定义方块数据（为 {@code null} 表示空气）
     */
    void setArtisanBlockData(@NotNull World world, int x, int y, int z, @Nullable ArtisanBlockData artisanBlockData);

    /**
     * 设置指定位置的自定义方块数据
     *
     * @param block 目标位置
     * @param artisanBlockData 自定义方块数据（为 {@code null} 表示空气）
     */
    default void setArtisanBlockData(@NotNull Block block, @Nullable ArtisanBlockData artisanBlockData) {
        setArtisanBlockData(block.getWorld(), block.getX(), block.getY(), block.getZ(), artisanBlockData);
    }

    /**
     * 设置指定位置的自定义方块数据
     *
     * @param location 目标位置
     * @param artisanBlockData 自定义方块数据（为 {@code null} 表示空气）
     */
    default void setArtisanBlockData(@NotNull Location location, @Nullable ArtisanBlockData artisanBlockData) {
        setArtisanBlockData(location.getBlock(), artisanBlockData);
    }

    /**
     * 获取指定位置的方块ID（包括原版）
     *
     * @param world 目标世界（非null）
     * @param x X坐标
     * @param y Y坐标
     * @param z Z坐标
     */
    @NotNull
    default NamespacedKey getBlockId(@NotNull World world, int x, int y, int z) {
        if (isArtisanBlock(world, x, y, z)) return getArtisanBlockData(world, x, y, z).blockId();
        else return world.getBlockAt(x, y, z).getType().getKey();
    }

    /**
     * 获取指定位置的方块ID（包括原版）
     *
     * @param block 目标方块（非null）
     */
    @NotNull
    default NamespacedKey getBlockId(@NotNull Block block) {
        return getBlockId(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    /**
     * 获取指定位置的方块ID（包括原版）
     *
     * @param location 目标位置（非null）
     */
    @NotNull
    default NamespacedKey getBlockId(@NotNull Location location) {
        return getBlockId(location.getBlock());
    }
}
