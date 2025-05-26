package io.github.moyusowo.neoartisanapi.api.item;

import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 自定义物品生成器接口，用于按需生成指定注册ID的物品堆(ItemStack)。
 * <p>
 * 提供两种内置实现：
 * <ul>
 *   <li>{@link SimpleItemGenerator} - 生成固定数量的物品</li>
 *   <li>{@link RangedItemGenerator} - 生成随机数量范围内的物品</li>
 * </ul>
 *
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public interface ItemGenerator {

    /**
     * 创建生成固定数量物品的生成器
     *
     * @param registryId 物品注册ID，不能为null
     * @param amount 生成的固定数量（数字无效的话实际使用时会取接近一侧的边界值）
     * @return 配置好的物品生成器实例
     * @see SimpleItemGenerator
     */
    static ItemGenerator simpleGenerator(@NotNull NamespacedKey registryId, int amount) {
        return new SimpleItemGenerator(registryId, amount);
    }

    /**
     * 创建生成随机数量物品的生成器
     *
     * @param registryId 物品注册ID，不能为null
     * @param min 最小生成数量
     * @param max 最大生成数量
     * @return 配置好的物品生成器实例
     * @see RangedItemGenerator
     */
    static ItemGenerator rangedGenerator(@NotNull NamespacedKey registryId, int min, int max) {
        return new RangedItemGenerator(registryId, min, max);
    }

    class SimpleItemGenerator implements ItemGenerator {

        private final int amount;
        private final NamespacedKey registryId;

        private SimpleItemGenerator(NamespacedKey registryId, int amount) {
            this.amount = amount;
            this.registryId = registryId;
        }

        @Override
        public @NotNull NamespacedKey registryId() {
            return this.registryId;
        }

        @Override
        public @NotNull ItemStack generate() {
            return NeoArtisanAPI.getItemRegistry().getItemStack(this.registryId, this.amount);
        }
    }

    class RangedItemGenerator implements ItemGenerator {

        private final int min, max;
        private final NamespacedKey registryId;

        private RangedItemGenerator(NamespacedKey registryId, int min, int max) {
            this.min = min;
            this.max = max;
            this.registryId = registryId;
        }

        @Override
        public @NotNull NamespacedKey registryId() {
            return this.registryId;
        }

        @Override
        public @NotNull ItemStack generate() {
            return NeoArtisanAPI.getItemRegistry().getItemStack(this.registryId, ThreadLocalRandom.current().nextInt(min, max + 1));
        }
    }

    /**
     * 获取生成器关联的物品注册ID
     *
     * @return 非空的注册表NamespacedKey
     */
    @NotNull NamespacedKey registryId();

    /**
     * 生成新的物品堆实例
     *
     * @return 新生成的物品堆，保证非空且已通过验证
     * @throws IllegalArgumentException 如果物品ID未注册
     */
    @NotNull ItemStack generate();

}
