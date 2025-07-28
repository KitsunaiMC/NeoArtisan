package io.github.moyusowo.neoartisanapi.api.util;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * {@link ItemStack} serialization handler designed for {@link org.bukkit.persistence.PersistentDataContainer},
 * providing efficient item stack binary serialization support.
 * <p>
 * This implementation uses {@link ItemStack#serializeAsBytes()} and {@link ItemStack#deserializeBytes(byte[])}
 * to achieve lossless storage of item data, suitable for scenarios where item data needs to be embedded in NBT structures.
 * </p>
 *
 * <h3>Typical usage:</h3>
 * <pre>{@code
 * // Store item to PDC
 * ItemStack diamond = new ItemStack(Material.DIAMOND);
 * container.set(CUSTOM_ITEM_KEY, ItemStackDataType.ITEM_STACK, diamond);
 *
 * // Read item from PDC
 * ItemStack stored = container.get(CUSTOM_ITEM_KEY, ItemStackDataType.ITEM_STACK);
 * }</pre>
 *
 * <h3>Technical features:</h3>
 * <ul>
 *   <li><b>Version safe</b> - Uses PaperAPI official serialization scheme</li>
 *   <li><b>Thread safe</b> - Singleton design, can be safely used across threads</li>
 * </ul>
 *
 * @see org.bukkit.persistence.PersistentDataType
 * @see org.bukkit.inventory.ItemStack#serializeAsBytes()
 * @see org.bukkit.inventory.ItemStack#deserializeBytes(byte[])
 */
public class ItemStackDataType implements PersistentDataType<byte[], ItemStack> {

    /**
     * Global singleton instance
     */
    public static final ItemStackDataType ITEM_STACK = new ItemStackDataType();

    private ItemStackDataType() {}

    @Override
    public @NotNull Class<byte[]> getPrimitiveType() {
        return byte[].class;
    }

    @Override
    public @NotNull Class<ItemStack> getComplexType() {
        return ItemStack.class;
    }

    @Override
    public byte @NotNull [] toPrimitive(@NotNull ItemStack complex, @NotNull PersistentDataAdapterContext context) {
        return complex.serializeAsBytes();
    }

    @Override
    public @NotNull ItemStack fromPrimitive(byte @NotNull [] primitive, @NotNull PersistentDataAdapterContext context) {
        return ItemStack.deserializeBytes(primitive);
    }
}