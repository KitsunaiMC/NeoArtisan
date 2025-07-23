package io.github.moyusowo.neoartisanapi.api.recipe;

import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.recipe.choice.Choice;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

/**
 * 自定义配方系统的核心抽象接口，定义所有配方类型的公共契约。
 *
 * <p><b>设计哲学：</b></p>
 * <ul>
 *   <li><b>不变性</b> - 所有实现类应当是不可变的</li>
 *   <li><b>生成式设计</b> - 结果物品通过生成器动态创建</li>
 * </ul>
 *
 * @see ArtisanShapedRecipe 有序合成配方
 * @see ArtisanShapelessRecipe 无序合成配方
 * @see ArtisanFurnaceRecipe 熔炉配方
 * @see ArtisanSmokingRecipe 烟熏炉配方
 * @see ArtisanCampfireRecipe 营火配方
 * @see ArtisanBlastingRecipe 高炉配方
 * @implNote 可以实现自己的配方类型
 */
public interface ArtisanRecipe {
    /**
     * 获取配方的全局唯一标识符
     */
    @NotNull
    NamespacedKey getKey();

    /**
     * 获取配方类型
     *
     * @see RecipeType
     */
    @NotNull
    NamespacedKey getType();

    /**
     * 获取配方所需的输入材料的不可变列表
     *
     * @see Choice
     */
    @Unmodifiable
    @NotNull
    List<Choice> getInputs();

    /**
     * 获取结果物品生成器的不可变列表
     *
     * @see ItemGenerator
     */
    @Unmodifiable
    @NotNull
    List<ItemGenerator> getResultGenerators();

    boolean matches(ItemStack @NotNull [] matrix);

}
