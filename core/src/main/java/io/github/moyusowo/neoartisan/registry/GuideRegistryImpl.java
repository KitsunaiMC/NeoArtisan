package io.github.moyusowo.neoartisan.registry;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.guide.category.GuideCategoryManager;
import io.github.moyusowo.neoartisan.guide.generator.FurnaceLikeGuide;
import io.github.moyusowo.neoartisan.guide.generator.ShapedGuide;
import io.github.moyusowo.neoartisan.guide.generator.ShapelessGuide;
import io.github.moyusowo.neoartisan.registry.internal.GuideRegistryInternal;
import io.github.moyusowo.neoartisan.util.data.NamespacedKeyDataType;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.recipe.RecipeType;
import io.github.moyusowo.neoartisanapi.api.recipe.guide.GuideGUIGenerator;
import io.github.moyusowo.neoartisanapi.api.recipe.guide.ItemCategories;
import io.github.moyusowo.neoartisanapi.api.registry.GuideRegistry;
import io.github.moyusowo.neoartisanapi.api.registry.Registries;
import io.papermc.paper.datacomponent.DataComponentTypes;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;

final class GuideRegistryImpl implements GuideRegistryInternal {

    private static final NamespacedKey CATEGORY_KEY = new NamespacedKey("neoartisan", "category");
    private static final NamespacedKey BOOK_KEY = new NamespacedKey("neoartisan", "book");
    private static final Map<Player, String> openBooks = new HashMap<>();

    private static GuideRegistryImpl instance;

    @InitMethod(priority = InitPriority.REGISTRY_LOAD)
    static void init() {
        new GuideRegistryImpl();
    }

    private final Map<NamespacedKey, GuideGUIGenerator> generators;
    private final Map<NamespacedKey, ItemStack> categorys;
    private final Map<String, Guide> books;

    private record Guide(GuideCategoryManager manager, ItemStack itemStack) {}

    @SuppressWarnings("UnstableApiUsage")
    private GuideRegistryImpl() {
        instance = this;
        generators = new HashMap<>();
        generators.put(RecipeType.SHAPED, new ShapedGuide());
        generators.put(RecipeType.SHAPELESS, new ShapelessGuide());
        generators.put(RecipeType.SMOKING, new FurnaceLikeGuide());
        generators.put(RecipeType.BLASTING, new FurnaceLikeGuide());
        generators.put(RecipeType.FURNACE, new FurnaceLikeGuide());
        generators.put(RecipeType.CAMPFIRE, new FurnaceLikeGuide());
        categorys = new HashMap<>();
        registerCategory(ItemCategories.ORIGINAL, () -> {
            ItemStack itemStack = ItemStack.of(Material.GRASS_BLOCK);
            itemStack.setData(DataComponentTypes.ITEM_NAME, MiniMessage.miniMessage().deserialize("原版物品"));
            return itemStack;
        });
        registerCategory(ItemCategories.COMBAT, () -> {
            ItemStack itemStack = ItemStack.of(Material.DIAMOND_SWORD);
            itemStack.setData(DataComponentTypes.ITEM_NAME, MiniMessage.miniMessage().deserialize("武器"));
            return itemStack;
        });
        registerCategory(ItemCategories.DECORATIONS, () -> {
            ItemStack itemStack = ItemStack.of(Material.FLOWER_POT);
            itemStack.setData(DataComponentTypes.ITEM_NAME, MiniMessage.miniMessage().deserialize("装饰"));
            return itemStack;
        });
        registerCategory(ItemCategories.FOOD, () -> {
            ItemStack itemStack = ItemStack.of(Material.BREAD);
            itemStack.setData(DataComponentTypes.ITEM_NAME, MiniMessage.miniMessage().deserialize("食物"));
            return itemStack;
        });
        registerCategory(ItemCategories.MISC, () -> {
            ItemStack itemStack = ItemStack.of(Material.CLAY_BALL);
            itemStack.setData(DataComponentTypes.ITEM_NAME, MiniMessage.miniMessage().deserialize("杂项"));
            return itemStack;
        });
        registerCategory(ItemCategories.TOOLS, () -> {
            ItemStack itemStack = ItemStack.of(Material.GOLDEN_PICKAXE);
            itemStack.setData(DataComponentTypes.ITEM_NAME, MiniMessage.miniMessage().deserialize("工具"));
            return itemStack;
        });
        Bukkit.getServicesManager().register(
                GuideRegistry.class,
                GuideRegistryImpl.instance,
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
        books = new HashMap<>();
    }

    @Override
    public void setGuideGenerator(@NotNull NamespacedKey recipeType, @NotNull GuideGUIGenerator generator) {
        if (!generators.containsKey(recipeType)) generators.put(recipeType, generator);
        else generators.replace(recipeType, generator);
    }

    @Override
    public void registerCategory(@NotNull NamespacedKey category, @NotNull Supplier<ItemStack> iconItemStack) {
        final ItemStack itemStack = iconItemStack.get();
        itemStack.editPersistentDataContainer(persistentDataContainer -> persistentDataContainer.set(CATEGORY_KEY, NamespacedKeyDataType.NAMESPACED_KEY, category));
        if (!categorys.containsKey(category)) categorys.put(category, itemStack);
        else categorys.replace(category, itemStack);
    }

    @Override
    public void registerCategory(@NotNull NamespacedKey category, @NotNull ItemStack iconItemStack) {
        final ItemStack itemStack = iconItemStack.clone();
        itemStack.editPersistentDataContainer(persistentDataContainer -> persistentDataContainer.set(CATEGORY_KEY, NamespacedKeyDataType.NAMESPACED_KEY, category));
        if (!categorys.containsKey(category)) categorys.put(category, itemStack);
        else categorys.replace(category, itemStack);
    }

    @Override
    public void registerCategory(@NotNull NamespacedKey category, @NotNull NamespacedKey iconItemId) {
        final ItemStack itemStack = Registries.ITEM.getItemStack(iconItemId);
        itemStack.editPersistentDataContainer(persistentDataContainer -> persistentDataContainer.set(CATEGORY_KEY, NamespacedKeyDataType.NAMESPACED_KEY, category));
        if (!categorys.containsKey(category)) categorys.put(category, itemStack);
        else categorys.replace(category, itemStack);
    }

    @Override
    public void registerGuideBook(@NotNull Plugin plugin, @NotNull Supplier<ItemStack> bookItemStack, @NotNull Set<NamespacedKey> categories) {
        final ItemStack itemStack = bookItemStack.get();
        final String key = plugin.getName();
        itemStack.editPersistentDataContainer(persistentDataContainer -> persistentDataContainer.set(BOOK_KEY, NamespacedKeyDataType.STRING, key));
        books.put(key, new Guide(new GuideCategoryManager(categories), itemStack));
    }

    @Override
    public void registerGuideBook(@NotNull Plugin plugin, @NotNull ItemStack bookItemStack, @NotNull Set<NamespacedKey> categories) {
        final ItemStack itemStack = bookItemStack.clone();
        final String key = plugin.getName();
        itemStack.editPersistentDataContainer(persistentDataContainer -> persistentDataContainer.set(BOOK_KEY, NamespacedKeyDataType.STRING, key));
        books.put(key, new Guide(new GuideCategoryManager(categories), itemStack));
    }

    @Override
    public void registerGuideBook(@NotNull Plugin plugin, @NotNull NamespacedKey bookItemId, @NotNull Set<NamespacedKey> categories) {
        final ItemStack itemStack = Registries.ITEM.getItemStack(bookItemId);
        final String key = plugin.getName();
        itemStack.editPersistentDataContainer(persistentDataContainer -> persistentDataContainer.set(BOOK_KEY, NamespacedKeyDataType.STRING, key));
        books.put(key, new Guide(new GuideCategoryManager(categories), itemStack));
    }

    @Override
    public void openGuideBook(@NotNull Plugin plugin, @NotNull Player player) {
        openGuideBook(plugin.getName(), player);
    }

    @Override
    public @NotNull ItemStack getGuideBook(@NotNull Plugin plugin) {
        if (books.containsKey(plugin.getName())) {
            return books.get(plugin.getName()).itemStack.clone();
        }
        return ItemStack.empty();
    }

    @Override
    public @NotNull Optional<GuideGUIGenerator> getGuide(@NotNull NamespacedKey recipeType) {
        if (generators.containsKey(recipeType)) return Optional.of(generators.get(recipeType));
        return Optional.empty();
    }

    @Override
    public @NotNull Optional<ItemStack> getCategory(@NotNull NamespacedKey category) {
        if (categorys.containsKey(category)) return Optional.of(categorys.get(category));
        return Optional.empty();
    }

    @Override
    public @NotNull Optional<NamespacedKey> getCategoryPDC(@NotNull ItemStack itemStack) {
        if (itemStack.getItemMeta().getPersistentDataContainer().has(CATEGORY_KEY)) {
            NamespacedKey key = itemStack.getItemMeta().getPersistentDataContainer().get(CATEGORY_KEY, NamespacedKeyDataType.NAMESPACED_KEY);
            assert key != null;
            return Optional.of(key);
        }
        return Optional.empty();
    }

    @Override
    public @NotNull Optional<String> getBookPDC(@NotNull ItemStack itemStack) {
        if (itemStack.getPersistentDataContainer().has(BOOK_KEY)) {
            String key = itemStack.getItemMeta().getPersistentDataContainer().get(BOOK_KEY, PersistentDataType.STRING);
            assert key != null;
            return Optional.of(key);
        }
        return Optional.empty();
    }

    @Override
    public void returnToGuideBook(@NotNull Player player) {
        final String name = openBooks.getOrDefault(player, "");
        if (name.isEmpty()) return;
        if (books.containsKey(name)) {
            books.get(name).manager.openCategoryGuide(player, 0);
        }
    }

    @Override
    public void openGuideBook(@NotNull String name, @NotNull Player player) {
        if (books.containsKey(name)) {
            books.get(name).manager.openCategoryGuide(player, 0);
            openBooks.put(player, name);
        }
    }

    @Override
    public @NotNull Optional<String> getPlayerCurrentBook(@NotNull Player player) {
        if (openBooks.containsKey(player)) {
            return Optional.of(openBooks.get(player));
        }
        return Optional.empty();
    }
}
