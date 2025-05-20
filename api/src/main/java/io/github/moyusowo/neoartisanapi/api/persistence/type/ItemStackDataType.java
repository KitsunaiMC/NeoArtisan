package io.github.moyusowo.neoartisanapi.api.persistence.type;

import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataAdapterContext;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public class ItemStackDataType implements PersistentDataType<byte[], ItemStack> {

    public static ItemStackDataType ITEM_STACK = new ItemStackDataType();

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
