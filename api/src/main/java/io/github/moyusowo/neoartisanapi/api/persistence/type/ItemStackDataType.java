package io.github.moyusowo.neoartisanapi.api.persistence.type;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

/**
 * 专为 {@link org.bukkit.persistence.PersistentDataContainer} 设计的 {@link ItemStack} 序列化处理器，
 * 提供高效的物品堆栈二进制序列化支持。
 * <p>
 * 本实现通过 {@link ItemStack#serializeAsBytes()} 和 {@link ItemStack#deserializeBytes(byte[])}
 * 实现物品数据的无损存储，适用于需要将物品数据嵌入NBT结构的场景。
 * </p>
 *
 * <h3>典型用法：</h3>
 * <pre>{@code
 * // 存储物品到PDC
 * ItemStack diamond = new ItemStack(Material.DIAMOND);
 * container.set(CUSTOM_ITEM_KEY, ItemStackDataType.ITEM_STACK, diamond);
 *
 * // 从PDC读取物品
 * ItemStack stored = container.get(CUSTOM_ITEM_KEY, ItemStackDataType.ITEM_STACK);
 * }</pre>
 *
 * <h3>技术特性：</h3>
 * <ul>
 *   <li><b>版本安全</b> - 使用PaperAPI官方序列化方案</li>
 *   <li><b>线程安全</b> - 单例设计，可安全跨线程使用</li>
 * </ul>
 *
 * @see org.bukkit.persistence.PersistentDataType
 * @see org.bukkit.inventory.ItemStack#serializeAsBytes()
 * @see org.bukkit.inventory.ItemStack#deserializeBytes(byte[])
 */
public class ItemStackDataType implements PersistentDataType<byte[], ItemStack> {

    /**
     * 全局唯一实例
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
