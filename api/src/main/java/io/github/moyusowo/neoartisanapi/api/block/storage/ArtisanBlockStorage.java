package io.github.moyusowo.neoartisanapi.api.block.storage;

import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * 自定义方块数据存储服务，负责管理所有已放置的自定义方块实例。
 * <p>
 * 提供对自定义方块的查询和类型检查功能，支持通过坐标或{@link Block}实例访问。
 * 部分注明线程安全的方法可以在多线程环境下调用。
 * </p>
 *
 * @see ArtisanBlockData 基础方块数据接口
 * @see io.github.moyusowo.neoartisanapi.api.block.storage.Storages#BLOCK 获取服务实例
 */
public interface ArtisanBlockStorage {
    /**
     * 通过世界坐标获取自定义方块数据
     *
     * @param worldUID 目标世界UID
     * @param x X坐标
     * @param y Y坐标
     * @param z Z坐标
     * @return 对应的方块数据
     * @throws IllegalArgumentException 如果不存在
     * @apiNote 使用该方法前应先调用 {@link #isArtisanBlock(World, int, int, int)} 检查
     * @apiNote 本方法线程安全
     */
    @NotNull
    ArtisanBlockData getArtisanBlockData(@NotNull UUID worldUID, int x, int y, int z);

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
    default ArtisanBlockData getArtisanBlockData(@NotNull World world, int x, int y, int z) {
        return getArtisanBlockData(world.getUID(), x, y, z);
    }

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
     * @param worldUID 目标世界UID（非null）
     * @param x X坐标
     * @param y Y坐标
     * @param z Z坐标
     * @return 如果是有效的自定义方块返回true
     * @apiNote 本方法线程安全
     */
    boolean isArtisanBlock(@NotNull UUID worldUID, int x, int y, int z);

    /**
     * 检查坐标位置是否为已注册的自定义方块
     *
     * @param world 目标世界（非null）
     * @param x X坐标
     * @param y Y坐标
     * @param z Z坐标
     * @return 如果是有效的自定义方块返回true
     */
    default boolean isArtisanBlock(@NotNull World world, int x, int y, int z) {
        return isArtisanBlock(world.getUID(), x, y, z);
    }

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
     * 设置指定位置的自定义方块数据（世界内的实际方块需要手动先行设置）
     *
     * @param worldUID 目标世界UID（非null）
     * @param x X坐标
     * @param y Y坐标
     * @param z Z坐标
     * @param artisanBlockData 自定义方块数据（为 {@code null} 表示空气）
     * @apiNote 本方法线程安全
     */
    void setArtisanBlockData(@NotNull UUID worldUID, int x, int y, int z, @Nullable ArtisanBlockData artisanBlockData);

    /**
     * 设置指定位置的自定义方块数据（世界内的实际方块需要手动先行设置）
     *
     * @param world 目标世界（非null）
     * @param x X坐标
     * @param y Y坐标
     * @param z Z坐标
     * @param artisanBlockData 自定义方块数据（为 {@code null} 表示空气）
     */
    default void setArtisanBlockData(@NotNull World world, int x, int y, int z, @Nullable ArtisanBlockData artisanBlockData) {
        setArtisanBlockData(world.getUID(), x, y, z, artisanBlockData);
    }

    /**
     * 设置指定位置的自定义方块数据（世界内的实际方块需要手动先行设置）
     *
     * @param block 目标位置
     * @param artisanBlockData 自定义方块数据（为 {@code null} 表示空气）
     */
    default void setArtisanBlockData(@NotNull Block block, @Nullable ArtisanBlockData artisanBlockData) {
        setArtisanBlockData(block.getWorld(), block.getX(), block.getY(), block.getZ(), artisanBlockData);
    }

    /**
     * 设置指定位置的自定义方块数据（世界内的实际方块需要手动先行设置）
     *
     * @param location 目标位置
     * @param artisanBlockData 自定义方块数据（为 {@code null} 表示空气）
     */
    default void setArtisanBlockData(@NotNull Location location, @Nullable ArtisanBlockData artisanBlockData) {
        setArtisanBlockData(location.getBlock(), artisanBlockData);
    }
}
