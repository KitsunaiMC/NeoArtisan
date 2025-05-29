package io.github.moyusowo.neoartisanapi.api.recipe;

import org.bukkit.NamespacedKey;

/**
 * 熔炉配方构建器，用于熔炉的自定义合成配方。
 *
 * <p>通过此接口可以：</p>
 * <ul>
 *   <li>将字符符号绑定到特定材料</li>
 *   <li>设置合成结果物品和数量</li>
 *   <li>最终注册配方到服务器</li>
 * </ul>
 *
 * @see RecipeRegistry#createFurnaceRecipe(NamespacedKey, NamespacedKey, int, int, int)
 * @since 1.0.2
 */
public interface ArtisanFurnaceRecipe {

    /**
     * 完成配方构建并注册到服务器。
     *
     * <p><b>必须调用此方法配方才会生效！</b></p>
     *
     * <p>调用过本方法之后不能再调用。</p>
     *
     * @see RecipeRegistry
     */
    void build();
}
