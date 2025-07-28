package io.github.moyusowo.neoartisanapi.api.item;

import com.google.common.base.Preconditions;
import io.github.moyusowo.neoartisanapi.api.registry.Registries;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Custom item generator interface, used to generate ItemStacks of specified registry IDs on demand.
 * <p>
 * Provides two built-in implementations:
 * <ul>
 *   <li>{@link SimpleItemGenerator} - Generates items with fixed quantity</li>
 *   <li>{@link RangedItemGenerator} - Generates items within a random quantity range</li>
 * </ul>
 *
 */
@SuppressWarnings("unused")
public interface ItemGenerator {
    /**
     * Creates a generator that produces items with a fixed quantity
     *
     * @param registryId item registry ID, cannot be null
     * @param amount the fixed quantity to generate (if the number is invalid, the actual usage will take the nearest boundary value)
     * @return a configured item generator instance
     * @see SimpleItemGenerator
     */
    static ItemGenerator simpleGenerator(@NotNull NamespacedKey registryId, int amount) {
        return new SimpleItemGenerator(registryId, amount);
    }

    /**
     * Creates a generator that produces items with a random quantity
     *
     * @param registryId item registry ID, cannot be null
     * @param min minimum quantity to generate
     * @param max maximum quantity to generate
     * @return a configured item generator instance
     * @see RangedItemGenerator
     */
    static ItemGenerator rangedGenerator(@NotNull NamespacedKey registryId, int min, int max) {
        return new RangedItemGenerator(registryId, min, max);
    }

    /**
     * Creates a generator that produces items with a certain probability
     *
     * @param registryId item registry ID, cannot be null
     * @param amount quantity to generate
     * @param chance generation chance (decimal from 0 to 1)
     * @return a configured item generator instance
     * @see ChanceItemGenerator
     */
    static ItemGenerator chanceGenerator(@NotNull NamespacedKey registryId, int amount, @Range(from = 0, to = 1) double chance) {
        return new ChanceItemGenerator(registryId, amount, chance);
    }

    class SimpleItemGenerator implements ItemGenerator {

        protected final int amount;
        protected final NamespacedKey registryId;

        protected SimpleItemGenerator(@NotNull NamespacedKey registryId, int amount) {
            this.amount = amount;
            this.registryId = registryId;
        }

        @Override
        public @NotNull NamespacedKey registryId() {
            return this.registryId;
        }

        @Override
        public @NotNull ItemStack generate() {
            return Registries.ITEM.getItemStack(this.registryId, this.amount);
        }
    }

    class RangedItemGenerator implements ItemGenerator {

        protected final int min, max;
        protected final NamespacedKey registryId;

        protected RangedItemGenerator(@NotNull NamespacedKey registryId, int min, int max) {
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
            return Registries.ITEM.getItemStack(this.registryId, ThreadLocalRandom.current().nextInt(min, max + 1));
        }
    }

    class ChanceItemGenerator implements ItemGenerator {

        protected final int amount;
        protected final double chance;
        protected final NamespacedKey registryId;

        protected ChanceItemGenerator(@NotNull NamespacedKey registryId, int amount, double chance) {
            this.amount = amount;
            Preconditions.checkArgument(chance > 0 && chance < 1);
            this.chance = chance;
            this.registryId = registryId;
        }

        @Override
        public @NotNull NamespacedKey registryId() {
            return this.registryId;
        }

        @Override
        public @NotNull ItemStack generate() {
            if (ThreadLocalRandom.current().nextDouble() < chance) {
                return Registries.ITEM.getItemStack(this.registryId, amount);
            } else {
                return ItemStack.empty();
            }
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
