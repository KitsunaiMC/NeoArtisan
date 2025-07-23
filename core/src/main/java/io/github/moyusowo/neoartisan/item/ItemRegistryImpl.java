package io.github.moyusowo.neoartisan.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.RegisterManager;
import io.github.moyusowo.neoartisan.util.data.NamespacedKeyDataType;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import io.github.moyusowo.neoartisanapi.api.item.ItemRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

final class ItemRegistryImpl implements ItemRegistry {

    private static ItemRegistryImpl instance;

    public static ItemRegistryImpl getInstance() {
        return instance;
    }

    private final ConcurrentHashMap<NamespacedKey, ArtisanItemImpl> registry;
    private final Set<NamespacedKey> cachedItemList;
    private final Multimap<String, NamespacedKey> tagToId;
    private final Multimap<Material, String> originalItemTag;
    private final AtomicBoolean cached;

    private ItemRegistryImpl() {
        instance = this;
        registry = new ConcurrentHashMap<>();
        cachedItemList = new HashSet<>();
        cached = new AtomicBoolean(false);
        tagToId = HashMultimap.create();
        originalItemTag = HashMultimap.create();
        Bukkit.getServicesManager().register(
                ItemRegistry.class,
                ItemRegistryImpl.getInstance(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    @InitMethod(priority = InitPriority.REGISTRY_LOAD)
    public static void init() {
        new ItemRegistryImpl();
    }

    @InitMethod(priority = InitPriority.STARTUP)
    public static void initTagToId() {
        instance.registry.values().forEach(
                artisanItem -> artisanItem.getTags().forEach(
                        tag -> instance.tagToId.put(tag, artisanItem.getRegistryId())
                )
        );
    }

    @Override
    public void registerItem(@NotNull ArtisanItem artisanItem) {
        if (RegisterManager.isOpen()) {
            registry.put(artisanItem.getRegistryId(), (ArtisanItemImpl) artisanItem);
            NeoArtisan.logger().info("successfully register item: " + artisanItem.getRegistryId().asString());
        } else {
            throw RegisterManager.REGISTRY_CLOSED;
        }
    }

    @Override
    public void registerTagToMaterial(@NotNull Material material, @NotNull String... tags) {
        if (RegisterManager.isOpen()) {
            originalItemTag.putAll(material, Arrays.asList(tags));
            StringBuilder builder = new StringBuilder();
            builder.append("successfully register tags: ");
            for (String tag : tags) {
                builder.append(tag).append(", ");
            }
            builder.append("to material: ").append(material.name());
            NeoArtisan.logger().info(builder.toString());
        } else {
            throw RegisterManager.REGISTRY_CLOSED;
        }
    }

    public @NotNull Set<NamespacedKey> getAllIds() {
        if (!cached.get()) {
            registry.forEach(
                    (key, item) -> {
                        if (!item.isInternal()) {
                            cachedItemList.add(key);
                        }
                    }
            );
            cached.set(true);
        }
        return Collections.unmodifiableSet(cachedItemList);
    }

    @Override
    public @NotNull NamespacedKey getRegistryId(@Nullable ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) return ArtisanItem.EMPTY;
        if (!itemStack.getItemMeta().getPersistentDataContainer().has(NeoArtisan.getArtisanItemIdKey())) return itemStack.getType().getKey();
        NamespacedKey registryId = itemStack.getItemMeta().getPersistentDataContainer().get(NeoArtisan.getArtisanItemIdKey(), NamespacedKeyDataType.NAMESPACED_KEY);
        return Objects.requireNonNull(registryId);
    }

    @Override
    public boolean hasItem(@Nullable NamespacedKey registryId) {
        if (registryId == null) return false;
        else if (isArtisanItem(registryId)) return true;
        else return registryId.getNamespace().equals("minecraft") && Material.getMaterial(registryId.getKey().toUpperCase()) != null;
    }

    @Override
    public @NotNull ItemStack getItemStack(NamespacedKey registryId, int count) {
        if (isArtisanItem(registryId)) return ((ArtisanItemImpl) getArtisanItem(registryId)).getItemStack(count);
        Material material = Material.getMaterial(registryId.getKey().toUpperCase());
        if (material == null) throw new IllegalArgumentException("You should use has method to check before get!");
        ItemStack itemStack = new ItemStack(material);
        itemStack.setAmount(Math.min(count, itemStack.getMaxStackSize()));
        return itemStack;
    }

    @Override
    public @NotNull ItemStack getItemStack(NamespacedKey registryId) {
        return getItemStack(registryId, 1);
    }

    @Override
    public boolean isArtisanItem(@Nullable NamespacedKey registryId) {
        if (registryId == null) return false;
        return registry.containsKey(registryId);
    }

    @Override
    public boolean isArtisanItem(@Nullable ItemStack itemStack) {
        if (itemStack == null) return false;
        return isArtisanItem(getRegistryId(itemStack));
    }

    @Override
    public @NotNull ArtisanItem getArtisanItem(@NotNull NamespacedKey registryId) {
        ArtisanItem artisanItem = registry.get(registryId);
        if (artisanItem == null) throw new IllegalArgumentException("You should use has method to check before get!");
        return artisanItem;
    }

    @Override
    public @NotNull ArtisanItem getArtisanItem(ItemStack itemStack) {
        ArtisanItem artisanItem = registry.get(getRegistryId(itemStack));
        if (artisanItem == null) throw new IllegalArgumentException("You should use has method to check before get!");
        return artisanItem;
    }

    @Override
    public @NotNull @Unmodifiable Collection<NamespacedKey> getIdByTag(String tag) {
        if (tagToId.containsKey(tag)) {
            return Collections.unmodifiableCollection(tagToId.get(tag));
        }
        return Collections.emptySet();
    }

    @Override
    public @NotNull @Unmodifiable Collection<String> getTagsById(NamespacedKey id) {
        if (isArtisanItem(id)) return getArtisanItem(id).getTags();
        else if (id.getNamespace().equals("minecraft")) {
            Material material = Material.matchMaterial(id.getKey());
            if (material == null) return Set.of();
            if (originalItemTag.containsKey(material)) return Collections.unmodifiableCollection(originalItemTag.get(material));
            return Set.of();
        }
        return Set.of();
    }
}
