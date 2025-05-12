package io.github.moyusowo.neoartisanapi.api.item;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
public interface ItemGenerator {

    static ItemGenerator SimpleGenerator(NamespacedKey registryId, int amount) {
        return new SimpleItemGenerator(registryId, amount);
    }

    static ItemGenerator rangedGenerator(NamespacedKey registryId, int min, int max) {
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
            return ItemRegistry.getItemRegistryManager().getItemStack(this.registryId, this.amount);
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
            return ItemRegistry.getItemRegistryManager().getItemStack(this.registryId, ThreadLocalRandom.current().nextInt(min, max + 1));
        }
    }

    @NotNull NamespacedKey registryId();

    @NotNull ItemStack generate();

}
