package io.github.moyusowo.neoartisanapi.api;

import io.github.moyusowo.neoartisanapi.api.attribute.GlobalAttributeRegistry;
import io.github.moyusowo.neoartisanapi.api.attribute.ItemStackAttributeRegistry;
import io.github.moyusowo.neoartisanapi.api.attribute.PlayerAttributeRegistry;
import io.github.moyusowo.neoartisanapi.api.block.base.BlockRegistry;
import io.github.moyusowo.neoartisanapi.api.block.protection.BlockProtection;
import io.github.moyusowo.neoartisanapi.api.block.storage.ArtisanBlockStorage;
import io.github.moyusowo.neoartisanapi.api.item.ItemRegistry;
import io.github.moyusowo.neoartisanapi.api.persistence.EmptyPersistentDataContainer;
import io.github.moyusowo.neoartisanapi.api.recipe.RecipeRegistry;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
    public static GlobalAttributeRegistry getGlobalAttributeRegistry() {
        return Bukkit.getServicesManager().load(GlobalAttributeRegistry.class);
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
    public static ItemStackAttributeRegistry getItemStackAttributeRegistry() {
        return Bukkit.getServicesManager().load(ItemStackAttributeRegistry.class);
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
    public static PlayerAttributeRegistry getPlayerAttributeRegistry() {
        return Bukkit.getServicesManager().load(PlayerAttributeRegistry.class);
    }

    /**
     * 获取自定义方块注册表实例
     *
     * @return 方块注册表服务实例
     * @throws IllegalStateException 如果服务未正确注册
     * @see BlockRegistry
     */
    public static BlockRegistry getBlockRegistry() {
        return Bukkit.getServicesManager().load(BlockRegistry.class);
    }

    /**
     * 获取自定义物品注册表实例
     *
     * @return 物品注册表服务实例
     * @throws IllegalStateException 如果服务未正确注册
     * @see ItemRegistry
     */
    public static ItemRegistry getItemRegistry() {
        return Bukkit.getServicesManager().load(ItemRegistry.class);
    }

    /**
     * 获取自定义配方注册表实例
     *
     * @return 配方注册表服务实例
     * @throws IllegalStateException 如果服务未正确注册
     * @see RecipeRegistry
     */
    public static RecipeRegistry getRecipeRegistry() {
        return Bukkit.getServicesManager().load(RecipeRegistry.class);
    }

    /**
     * 获取自定义方块数据储存的管理实例
     *
     * @return 自定义方块数据储存的管理实例
     * @throws IllegalStateException 如果服务未正确注册
     * @see ArtisanBlockStorage
     */
    public static ArtisanBlockStorage getArtisanBlockStorage() {
        return Bukkit.getServicesManager().load(ArtisanBlockStorage.class);
    }

    public static BlockProtection getBlockProtection() {
        return Bukkit.getServicesManager().load(BlockProtection.class);
    }

    /**
     * 获取空持久化数据容器实例（内部使用）
     *
     * @return 空数据容器实例
     * @throws IllegalStateException 如果服务未正确注册
     * @apiNote 该方法仅供NeoArtisan内部实现使用
     */
    @ApiStatus.Internal
    public static EmptyPersistentDataContainer emptyPersistentDataContainer() {
        return Bukkit.getServicesManager().load(EmptyPersistentDataContainer.class);
    }


    /**
     * 标记注册表接口中的自动注册方法，该方法会被反射调用完成注册流程。
     * <p>
     * <b>约束条件：</b>
     * <ul>
     *   <li>只能在被注解的方法内使用注册表接口中的注册方法，否则抛出异常</li>
     *   <li>被标记的方法必须满足：
     *     <ul>
     *       <li>返回类型为 {@code static void}</li>
     *       <li>参数列表为空</li>
     *     </ul>
     *   </li>
     * </ul>
     *
     * <b>工作原理：</b>
     * <p>
     * 插件启动时会扫描所有注册表接口，反射调用被 {@code @Register} 标记的方法，
     * 自动完成物品/方块/配方的注册。
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Register {}

}
