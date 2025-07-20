package io.github.moyusowo.neoartisanapi.api;

import io.github.moyusowo.neoartisanapi.api.attribute.GlobalAttributeRegistry;
import io.github.moyusowo.neoartisanapi.api.attribute.ItemStackAttributeRegistry;
import io.github.moyusowo.neoartisanapi.api.attribute.PlayerAttributeRegistry;
import io.github.moyusowo.neoartisanapi.api.block.BlockRegistry;
import io.github.moyusowo.neoartisanapi.api.block.protection.BlockProtection;
import io.github.moyusowo.neoartisanapi.api.block.storage.ArtisanBlockStorage;
import io.github.moyusowo.neoartisanapi.api.item.ItemRegistry;
import io.github.moyusowo.neoartisanapi.api.recipe.RecipeRegistry;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

/**
 * NeoArtisan 插件的主API入口，提供对所有注册表系统的访问接口。
 * <p>
 * 通过 {@link org.bukkit.plugin.ServicesManager} 实现模块化服务发现，确保API与实现解耦。
 * 所有注册表实例通过Bukkit服务管理器获取，核心模块需要在插件启用时注册对应服务。
 *
 * <h2>使用示例</h2>
 * <pre>{@code
 * // 获取物品注册表
 * ItemRegistry registry = NeoArtisanAPI.getItemRegistry();
 * }</pre>
 *
 * @see org.bukkit.plugin.ServicesManager
 */
public final class NeoArtisanAPI {
    /**
     * 获取物品全局属性注册表实例，用于管理不依赖特定物品/玩家的通用物品属性
     *
     * @return 全局属性注册表服务实例
     * @throws IllegalStateException 如果服务未正确注册
     * @see GlobalAttributeRegistry
     */
    @NotNull
    public static GlobalAttributeRegistry getGlobalAttributeRegistry() {
        final GlobalAttributeRegistry registry = Bukkit.getServicesManager().load(GlobalAttributeRegistry.class);
        if (registry == null) throw new IllegalStateException("global item attribute registry has not yet loaded!");
        return registry;
    }

    /**
     * 获取物品堆属性注册表实例，用于管理绑定到特定物品堆(PDC)的属性
     *
     * <p>基于并兼容BukkitAPI的PDC操作，该注册表更多为规范而声明</p>
     *
     * @return 物品堆属性注册表服务实例
     * @throws IllegalStateException 如果服务未正确注册
     * @see ItemStackAttributeRegistry
     */
    @NotNull
    public static ItemStackAttributeRegistry getItemStackAttributeRegistry() {
        final ItemStackAttributeRegistry registry = Bukkit.getServicesManager().load(ItemStackAttributeRegistry.class);
        if (registry == null) throw new IllegalStateException("ItemStack attribute registry has not yet loaded!");
        return registry;
    }


    /**
     * 获取玩家属性注册表实例，用于管理玩家特有的属性数据
     *
     *  <p>基于并兼容BukkitAPI的PDC操作，该注册表更多为规范而声明</p>
     *
     * @return 玩家属性注册表服务实例
     * @throws IllegalStateException 如果服务未正确注册
     * @see PlayerAttributeRegistry
     */
    @NotNull
    public static PlayerAttributeRegistry getPlayerAttributeRegistry() {
        final PlayerAttributeRegistry registry = Bukkit.getServicesManager().load(PlayerAttributeRegistry.class);
        if (registry == null) throw new IllegalStateException("player attribute registry has not yet loaded!");
        return registry;
    }

    /**
     * 获取自定义方块注册表实例
     *
     * @return 方块注册表服务实例
     * @throws IllegalStateException 如果服务未正确注册
     * @see BlockRegistry
     */
    @NotNull
    public static BlockRegistry getBlockRegistry() {
        final BlockRegistry registry = Bukkit.getServicesManager().load(BlockRegistry.class);
        if (registry == null) throw new IllegalStateException("block registry has not yet loaded!");
        return registry;
    }

    /**
     * 获取自定义物品注册表实例
     *
     * @return 物品注册表服务实例
     * @throws IllegalStateException 如果服务未正确注册
     * @see ItemRegistry
     */
    @NotNull
    public static ItemRegistry getItemRegistry() {
        final ItemRegistry registry = Bukkit.getServicesManager().load(ItemRegistry.class);
        if (registry == null) throw new IllegalStateException("item registry has not yet loaded!");
        return registry;
    }

    /**
     * 获取自定义配方注册表实例
     *
     * @return 配方注册表服务实例
     * @throws IllegalStateException 如果服务未正确注册
     * @see RecipeRegistry
     */
    @NotNull
    public static RecipeRegistry getRecipeRegistry() {
        final RecipeRegistry registry = Bukkit.getServicesManager().load(RecipeRegistry.class);
        if (registry == null) throw new IllegalStateException("recipe registry has not yet loaded!");
        return registry;
    }

    /**
     * 获取自定义方块数据储存的管理实例
     *
     * @return 自定义方块数据储存的管理实例
     * @throws IllegalStateException 如果服务未正确注册
     * @see ArtisanBlockStorage
     */
    @NotNull
    public static ArtisanBlockStorage getArtisanBlockStorage() {
        final ArtisanBlockStorage storage = Bukkit.getServicesManager().load(ArtisanBlockStorage.class);
        if (storage == null) throw new IllegalStateException("block storage api has not yet loaded!");
        return storage;
    }

    /**
     * 获取方块保护模块实现
     *
     * @return 方块保护模块实现
     * @throws IllegalStateException 如果服务未正确注册
     * @see BlockProtection
     */
    @NotNull
    public static BlockProtection getBlockProtection() {
        final BlockProtection protection = Bukkit.getServicesManager().load(BlockProtection.class);
        if (protection == null) throw new IllegalStateException("block protection api has not yet loaded!");
        return protection;
    }

}
