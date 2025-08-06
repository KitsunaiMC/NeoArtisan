package io.github.moyusowo.neoartisan.registry;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.data.NamespacedKeyDataType;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import io.github.moyusowo.neoartisanapi.api.registry.ItemRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
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

    private final ConcurrentHashMap<NamespacedKey, ArtisanItem> registry;
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
        instance.originalItemTag.forEach(
                (material, tag) -> instance.tagToId.put(tag, material.getKey())
        );
    }

    // 我的世界原版矿辞
    @InitMethod
    public static void initOriginalItemTag() {
        // 关键词：蔬菜
        instance.registerTagToMaterial(Material.BEETROOT, "beetroot", "crop", "vegetable", "sweet");
        instance.registerTagToMaterial(Material.CARROT, "carrot", "crop", "vegetable", "cabbage_roll_ingredient");
        instance.registerTagToMaterial(Material.GOLDEN_CARROT, "carrot", "vegetable", "golden_food");
        instance.registerTagToMaterial(Material.POTATO, "potato", "crop", "vegetable", "raw");
        instance.registerTagToMaterial(Material.BAKED_POTATO, "potato", "vegetable", "cooked", "baked_potato", "baking");
        instance.registerTagToMaterial(Material.POISONOUS_POTATO, "potato", "crop", "vegetable", "poisonous");
        instance.registerTagToMaterial(Material.DRIED_KELP, "kelp", "cooked", "vegetable", "sushi_ingredient");
        instance.registerTagToMaterial(Material.KELP, "kelp", "crop", "vegetable", "sushi_ingredient", "raw", "water_plant", "plant");
        instance.registerTagToMaterial(Material.SEAGRASS, "seagrass", "vegetable", "sushi_ingredient", "water_plant", "plant");
        instance.registerTagToMaterial(Material.SEA_PICKLE, "sea_pickle", "crop", "vegetable", "sushi_ingredient", "pickle", "water_plant", "plant");
        instance.registerTagToMaterial(Material.PUMPKIN, "pumpkin", "crop", "vegetable");
        instance.registerTagToMaterial(Material.CARVED_PUMPKIN, "pumpkin", "vegetable");
        instance.registerTagToMaterial(Material.JACK_O_LANTERN, "pumpkin", "vegetable");
        instance.registerTagToMaterial(Material.FERN, "fern", "plant", "vegetable");
        instance.registerTagToMaterial(Material.LARGE_FERN, "fern", "plant", "vegetable", "tall_plant");
        // 关键词：水果
        instance.registerTagToMaterial(Material.APPLE, "apple", "fruit");
        instance.registerTagToMaterial(Material.GOLDEN_APPLE, "apple", "golden_food", "fruit");
        instance.registerTagToMaterial(Material.ENCHANTED_GOLDEN_APPLE, "apple", "golden_food", "fruit");
        instance.registerTagToMaterial(Material.MELON, "melon", "crop", "fruit");
        instance.registerTagToMaterial(Material.MELON_SLICE, "melon", "fruit", "slice");
        instance.registerTagToMaterial(Material.GLISTERING_MELON_SLICE, "melon", "golden_food", "fruit", "slice");
        instance.registerTagToMaterial(Material.SWEET_BERRIES, "berry", "crop", "fruit");
        instance.registerTagToMaterial(Material.GLOW_BERRIES, "berry", "crop", "fruit", "bone_broth_ingredient", "glow");
        instance.registerTagToMaterial(Material.CHORUS_FRUIT, "chorus", "crop", "fruit", "raw");
        instance.registerTagToMaterial(Material.POPPED_CHORUS_FRUIT, "chorus", "cooked", "fruit");
        //关键词：肉类
        instance.registerTagToMaterial(Material.BEEF, "beef", "meat", "raw", "raw_beef");
        instance.registerTagToMaterial(Material.COOKED_BEEF, "beef", "meat", "cooked", "cooked_beef");
        instance.registerTagToMaterial(Material.PORKCHOP, "porkchop", "meat", "raw", "raw_porkchop");
        instance.registerTagToMaterial(Material.COOKED_PORKCHOP, "porkchop", "meat", "cooked", "cooked_porkchop");
        instance.registerTagToMaterial(Material.MUTTON, "mutton", "meat", "raw", "raw_mutton");
        instance.registerTagToMaterial(Material.COOKED_MUTTON, "mutton", "meat", "cooked", "cooked_mutton");
        instance.registerTagToMaterial(Material.CHICKEN, "chicken", "meat", "raw", "raw_chicken");
        instance.registerTagToMaterial(Material.COOKED_CHICKEN, "chicken", "meat", "cooked", "cooked_chicken");
        instance.registerTagToMaterial(Material.RABBIT, "rabbit", "rabbit_meat", "meat", "raw", "raw_rabbit");
        instance.registerTagToMaterial(Material.COOKED_RABBIT, "rabbit", "rabbit_meat", "meat", "cooked", "cooked_rabbit");
        instance.registerTagToMaterial(Material.RABBIT_FOOT, "rabbit", "meat", "foot");
        instance.registerTagToMaterial(Material.ROTTEN_FLESH, "rotten_flash", "meat", "poisonous");
        //关键词：眼睛
        instance.registerTagToMaterial(Material.SPIDER_EYE, "spider_eye", "eye", "poisonous");
        instance.registerTagToMaterial(Material.FERMENTED_SPIDER_EYE, "spider_eye", "eye", "poisonous", "fermented");
        instance.registerTagToMaterial(Material.ENDER_PEARL, "ender", "eye", "pearl");
        instance.registerTagToMaterial(Material.ENDER_EYE, "ender", "eye");
        //关键词：海鲜
        instance.registerTagToMaterial(Material.COD, "cod", "fish", "raw", "raw_cod", "seafood");
        instance.registerTagToMaterial(Material.COOKED_COD, "cod", "fish", "cooked", "cooked_cod", "seafood");
        instance.registerTagToMaterial(Material.SALMON, "salmon", "fish", "raw", "raw_salmon", "seafood");
        instance.registerTagToMaterial(Material.COOKED_SALMON, "salmon", "fish", "cooked", "cooked_salmon", "seafood");
        instance.registerTagToMaterial(Material.TROPICAL_FISH, "tropical_fish", "fish", "raw", "raw_tropical_fish", "seafood");
        instance.registerTagToMaterial(Material.PUFFERFISH, "pufferfish", "fish", "raw", "raw_pufferfish", "seafood", "poisonous");
        instance.registerTagToMaterial(Material.NAUTILUS_SHELL, "nautilus_shell", "shell", "seafood");
        //关键词：蛋
        instance.registerTagToMaterial(Material.EGG, "chicken_egg", "egg");
        instance.registerTagToMaterial(Material.TURTLE_EGG, "turtle_egg", "egg");
        instance.registerTagToMaterial(Material.SNIFFER_EGG, "sniffer_egg", "egg");
        instance.registerTagToMaterial(Material.DRAGON_EGG, "dragon_egg", "egg");
        instance.registerTagToMaterial(Material.FROGSPAWN, "frogspawn", "egg");
        //关键词：蛋(1.21.5)
        //instance.registerTagToMaterial(Material.BLUE_EGG, "chicken_egg", "egg");
        //instance.registerTagToMaterial(Material.BROWN_EGG, "chicken_egg", "egg");

        //关键词：烘焙
        instance.registerTagToMaterial(Material.BREAD, "bread", "wheat_bread", "baking");
        instance.registerTagToMaterial(Material.COOKIE, "cookie", "cocoa_cookie", "baking");
        instance.registerTagToMaterial(Material.CAKE, "cake", "egg_and_milk_cake", "baking");
        instance.registerTagToMaterial(Material.PUMPKIN_PIE, "pie", "pumpkin_pie", "baking");
        //关键词：汤
        instance.registerTagToMaterial(Material.MUSHROOM_STEW, "stew", "soup", "bowled");
        instance.registerTagToMaterial(Material.BEETROOT_SOUP, "soup", "bowled");
        instance.registerTagToMaterial(Material.RABBIT_STEW, "stew", "soup", "bowled");
        instance.registerTagToMaterial(Material.SUSPICIOUS_STEW, "stew", "soup", "bowled");
        //关键词：做饭容器 
        instance.registerTagToMaterial(Material.BOWL, "bowl", "wooden_bowl", "container");
        instance.registerTagToMaterial(Material.GLASS_BOTTLE, "bottle", "glass_bottle", "container");
        instance.registerTagToMaterial(Material.BUCKET, "bucket", "iron_bucket", "container");
        instance.registerTagToMaterial(Material.CAULDRON, "cauldron", "iron_cauldron", "pot","container");
        instance.registerTagToMaterial(Material.BARREL, "barrel", "wooden_barrel", "container");
        //关键词：热源(方块型) 
        instance.registerTagToMaterial(Material.MAGMA_BLOCK, "magma", "heat_source");
        instance.registerTagToMaterial(Material.CAMPFIRE, "fire", "heat_source");
        instance.registerTagToMaterial(Material.SOUL_CAMPFIRE, "fire", "soul", "heat_source");
        //关键词：饮料、液体
        instance.registerTagToMaterial(Material.HONEY_BOTTLE, "honey", "sweet", "drinks", "bottled", "ropy");
        instance.registerTagToMaterial(Material.MILK_BUCKET, "milk", "bucket_milk", "drinks", "bucket_liquid", "liquid");
        instance.registerTagToMaterial(Material.OMINOUS_BOTTLE, "ominous", "ominous_bottle", "drinks", "bottled");
        instance.registerTagToMaterial(Material.EXPERIENCE_BOTTLE, "experience", "experience_bottle", "drinks", "bottled");
        instance.registerTagToMaterial(Material.POTION, "potion", "drink_potion", "drinks", "bottled");
        instance.registerTagToMaterial(Material.SPLASH_POTION, "potion", "splash_potion", "drinks", "bottled");
        instance.registerTagToMaterial(Material.LINGERING_POTION, "potion", "lingering_potion", "drinks", "bottled");
        instance.registerTagToMaterial(Material.DRAGON_BREATH, "potion", "dragon_breath", "drinks", "bottled");
        instance.registerTagToMaterial(Material.WATER_BUCKET, "water", "bucket_water", "bucket_liquid", "liquid");
        instance.registerTagToMaterial(Material.LAVA_BUCKET, "lava", "bucket_lava", "bucket_liquid", "liquid");
        instance.registerTagToMaterial(Material.POWDER_SNOW_BUCKET, "snow", "bucket_powder_snow", "bucket_solid");
        instance.registerTagToMaterial(Material.COD_BUCKET, "cod", "bucket_cod", "bucket_mob");
        instance.registerTagToMaterial(Material.SALMON_BUCKET, "salmon", "bucket_salmon", "bucket_mob");
        instance.registerTagToMaterial(Material.TROPICAL_FISH_BUCKET, "tropical_fish", "bucket_tropical_fish", "bucket_mob");
        instance.registerTagToMaterial(Material.PUFFERFISH_BUCKET, "pufferfish", "bucket_pufferfish", "bucket_mob");
        instance.registerTagToMaterial(Material.AXOLOTL_BUCKET, "axolotl", "bucket_axolotl", "bucket_mob");
        instance.registerTagToMaterial(Material.TADPOLE_BUCKET, "tadpole", "bucket_tadpole", "bucket_mob");
        //关键词：糖类、食物原材料
        instance.registerTagToMaterial(Material.WHEAT, "wheat", "crop", "ingredient");
        instance.registerTagToMaterial(Material.SUGAR, "sugar", "sweet", "ingredient", "powder");
        instance.registerTagToMaterial(Material.HONEYCOMB, "honey", "sweet", "ingredient");
        instance.registerTagToMaterial(Material.HONEY_BLOCK, "honey", "sweet", "honey_block");
        instance.registerTagToMaterial(Material.HONEYCOMB_BLOCK, "honey", "sweet", "honey_block");
        instance.registerTagToMaterial(Material.SUGAR_CANE, "sugar_cane", "sweet", "sugar", "ingredient", "plant");
        instance.registerTagToMaterial(Material.CACTUS, "cactus", "ingredient", "plant");
        instance.registerTagToMaterial(Material.NETHER_WART, "wart", "nether_wart", "ingredient", "plant", "nether_plant", "crop");
        instance.registerTagToMaterial(Material.COCOA_BEANS, "cocoa_beans", "ingredient", "plant", "bean", "chocolate_ingredient");
        //关键词：菌类(蘑菇)
        instance.registerTagToMaterial(Material.BROWN_MUSHROOM, "mushroom", "brown_mushroom", "ingredient", "mushroom_ingredient", "bone_broth_ingredient");
        instance.registerTagToMaterial(Material.RED_MUSHROOM, "mushroom", "red_mushroom", "ingredient", "mushroom_ingredient", "bone_broth_ingredient");
        instance.registerTagToMaterial(Material.MUSHROOM_STEM, "mushroom", "mushroom_block");
        instance.registerTagToMaterial(Material.BROWN_MUSHROOM_BLOCK, "mushroom", "brown_mushroom", "mushroom_block");
        instance.registerTagToMaterial(Material.RED_MUSHROOM_BLOCK, "mushroom", "red_mushroom", "mushroom_block");
        instance.registerTagToMaterial(Material.CRIMSON_FUNGUS, "mushroom", "crimson_fungus", "ingredient", "mushroom_ingredient");
        instance.registerTagToMaterial(Material.WARPED_FUNGUS, "mushroom", "warped_fungus", "ingredient", "mushroom_ingredient");
        instance.registerTagToMaterial(Material.CRIMSON_ROOTS, "mushroom", "crimson_roots", "roots");
        instance.registerTagToMaterial(Material.WARPED_ROOTS, "mushroom", "warped_roots", "roots");
        instance.registerTagToMaterial(Material.SHROOMLIGHT, "mushroom", "mushroom_block");
        instance.registerTagToMaterial(Material.MYCELIUM, "mushroom", "mushroom_block");
        instance.registerTagToMaterial(Material.CRIMSON_NYLIUM, "mushroom", "mushroom_block");
        instance.registerTagToMaterial(Material.WARPED_NYLIUM, "mushroom", "mushroom_block");
        instance.registerTagToMaterial(Material.WARPED_WART_BLOCK, "wart");
        instance.registerTagToMaterial(Material.NETHER_WART_BLOCK, "wart", "nether_wart");
        //关键词：种子
        instance.registerTagToMaterial(Material.WHEAT_SEEDS, "wheat_seeds", "seed");
        instance.registerTagToMaterial(Material.PUMPKIN_SEEDS, "pumkin_seeds", "seed");
        instance.registerTagToMaterial(Material.MELON_SEEDS, "melon_seeds", "seed");
        instance.registerTagToMaterial(Material.BEETROOT_SEEDS, "beetroot_seeds", "seed");
        instance.registerTagToMaterial(Material.TORCHFLOWER_SEEDS, "torchflower_seeds", "seed", "ancient_seeds", "ancient");
        instance.registerTagToMaterial(Material.PITCHER_POD, "pitcher_pod", "seed","ancient_seeds", "ancient");
        //关键词：植物、花
        instance.registerTagToMaterial(Material.NETHER_SPROUTS, "plant", "nether_plant");
        instance.registerTagToMaterial(Material.WEEPING_VINES,  "plant", "vine", "nether_plant");
        instance.registerTagToMaterial(Material.TWISTING_VINES,  "plant", "vine", "nether_plant");
        instance.registerTagToMaterial(Material.AZALEA, "plant");
        instance.registerTagToMaterial(Material.FLOWERING_AZALEA, "plant", "flower");
        instance.registerTagToMaterial(Material.SHORT_GRASS, "plant", "grass");
        instance.registerTagToMaterial(Material.DEAD_BUSH, "plant");
        instance.registerTagToMaterial(Material.DANDELION,  "plant", "flower");
        instance.registerTagToMaterial(Material.POPPY,  "plant", "flower");
        instance.registerTagToMaterial(Material.BLUE_ORCHID,  "plant", "flower");
        instance.registerTagToMaterial(Material.ALLIUM,  "plant", "flower");
        instance.registerTagToMaterial(Material.AZURE_BLUET,  "plant", "flower");
        instance.registerTagToMaterial(Material.RED_TULIP,  "plant", "flower");
        instance.registerTagToMaterial(Material.ORANGE_TULIP,  "plant", "flower");
        instance.registerTagToMaterial(Material.WHITE_TULIP,  "plant", "flower");
        instance.registerTagToMaterial(Material.PINK_TULIP,  "plant", "flower");
        instance.registerTagToMaterial(Material.OXEYE_DAISY,  "plant", "flower");
        instance.registerTagToMaterial(Material.CORNFLOWER,  "plant", "flower");
        instance.registerTagToMaterial(Material.LILY_OF_THE_VALLEY,  "plant", "flower");
        instance.registerTagToMaterial(Material.TORCHFLOWER,  "plant", "flower", "ancient", "peppery_taste");
        instance.registerTagToMaterial(Material.CLOSED_EYEBLOSSOM,  "plant", "flower");
        instance.registerTagToMaterial(Material.OPEN_EYEBLOSSOM,  "plant", "flower");
        instance.registerTagToMaterial(Material.WITHER_ROSE,  "plant", "flower", "poisonous");
        instance.registerTagToMaterial(Material.PINK_PETALS,  "plant", "flower");
        instance.registerTagToMaterial(Material.SPORE_BLOSSOM,  "plant", "flower");
        instance.registerTagToMaterial(Material.BAMBOO,  "plant", "bamboo");
        instance.registerTagToMaterial(Material.VINE,  "plant", "vine");
        instance.registerTagToMaterial(Material.TALL_GRASS,  "plant", "tall_plant", "grass");
        instance.registerTagToMaterial(Material.SUNFLOWER,  "plant", "tall_plant", "flower");
        instance.registerTagToMaterial(Material.LILAC,  "plant", "tall_plant", "flower");
        instance.registerTagToMaterial(Material.ROSE_BUSH,  "plant", "tall_plant", "flower");
        instance.registerTagToMaterial(Material.PEONY,  "plant", "tall_plant", "flower");
        instance.registerTagToMaterial(Material.PITCHER_PLANT,  "plant", "tall_plant", "flower", "ancient");
        instance.registerTagToMaterial(Material.BIG_DRIPLEAF,  "plant", "tall_plant");
        instance.registerTagToMaterial(Material.SMALL_DRIPLEAF,  "plant");
        instance.registerTagToMaterial(Material.SCULK_VEIN,  "plant");
        instance.registerTagToMaterial(Material.CHORUS_PLANT,  "plant", "chorus");
        instance.registerTagToMaterial(Material.CHORUS_FLOWER,  "plant", "chorus", "flower");
        instance.registerTagToMaterial(Material.GLOW_LICHEN,  "plant", "bone_broth_ingredient");
        instance.registerTagToMaterial(Material.HANGING_ROOTS,  "plant", "bone_broth_ingredient", "roots");
        instance.registerTagToMaterial(Material.PALE_HANGING_MOSS,  "plant", "roots");
        instance.registerTagToMaterial(Material.LILY_PAD,  "plant", "water_plant");
        //1.21.5的植物
        //instance.registerTagToMaterial(Material.CACTUS_FLOWER,  "plant", "flower");
        //instance.registerTagToMaterial(Material.WILDFLOWERS,  "plant", "flower");
        //instance.registerTagToMaterial(Material.LEAF_LITTER,  "plant");
        //instance.registerTagToMaterial(Material.FIREFLY_BUSH,  "plant", "firefly", "bush");
        //instance.registerTagToMaterial(Material.BUSH,  "plant", "bush");
        //instance.registerTagToMaterial(Material.SHORT_DRY_GRASS,  "plant", "grass");
        //instance.registerTagToMaterial(Material.TALL_DRY_GRASS,  "plant", "grass", "tall_plant");

        //关键词：冰
        instance.registerTagToMaterial(Material.ICE, "ice", "drinks_ingredient");
        instance.registerTagToMaterial(Material.PACKED_ICE, "ice", "drinks_ingredient");
        instance.registerTagToMaterial(Material.BLUE_ICE, "ice", "drinks_ingredient");
        //关键词：雪
        instance.registerTagToMaterial(Material.SNOW_BLOCK, "snow");
        instance.registerTagToMaterial(Material.SNOW, "snow");
        instance.registerTagToMaterial(Material.SNOWBALL, "snow", "ball", "drinks_ingredient");
        //关键词：球形
        instance.registerTagToMaterial(Material.SLIME_BALL, "slime", "ball", "ropy");
        instance.registerTagToMaterial(Material.CLAY_BALL, "clay", "ball");
        instance.registerTagToMaterial(Material.MAGMA_CREAM, "magma", "ball", "ropy");
        instance.registerTagToMaterial(Material.FIRE_CHARGE, "fire", "ball");
        //关键词：树苗
        instance.registerTagToMaterial(Material.OAK_SAPLING, "sapling");
        instance.registerTagToMaterial(Material.SPRUCE_SAPLING, "sapling");
        instance.registerTagToMaterial(Material.BIRCH_SAPLING, "sapling");
        instance.registerTagToMaterial(Material.JUNGLE_SAPLING, "sapling");
        instance.registerTagToMaterial(Material.ACACIA_SAPLING, "sapling");
        instance.registerTagToMaterial(Material.DARK_OAK_SAPLING, "sapling");
        instance.registerTagToMaterial(Material.MANGROVE_PROPAGULE, "sapling");
        instance.registerTagToMaterial(Material.CHERRY_SAPLING, "sapling");
        instance.registerTagToMaterial(Material.PALE_OAK_SAPLING, "sapling");
        //关键词：动物皮毛
        instance.registerTagToMaterial(Material.LEATHER, "skin", "animal_pelage");
        instance.registerTagToMaterial(Material.RABBIT_HIDE, "skin", "animal_pelage");
        instance.registerTagToMaterial(Material.FEATHER, "feather", "animal_pelage");
        instance.registerTagToMaterial(Material.TURTLE_SCUTE, "scute", "animal_pelage");
        instance.registerTagToMaterial(Material.ARMADILLO_SCUTE, "scute", "animal_pelage");
        instance.registerTagToMaterial(Material.INK_SAC, "sac", "animal_pelage");
        instance.registerTagToMaterial(Material.GLOW_INK_SAC, "sac", "animal_pelage");
        instance.registerTagToMaterial(Material.PRISMARINE_SHARD, "scute", "animal_pelage");
        instance.registerTagToMaterial(Material.PRISMARINE_CRYSTALS, "scute", "animal_pelage");
        instance.registerTagToMaterial(Material.PHANTOM_MEMBRANE, "scute", "animal_pelage");
        instance.registerTagToMaterial(Material.SHULKER_SHELL, "shell", "animal_pelage", "container");
        //关键词：头颅
        instance.registerTagToMaterial(Material.SKELETON_SKULL, "head");
        instance.registerTagToMaterial(Material.WITHER_SKELETON_SKULL, "head");
        instance.registerTagToMaterial(Material.PLAYER_HEAD, "head");
        instance.registerTagToMaterial(Material.ZOMBIE_HEAD, "head");
        instance.registerTagToMaterial(Material.CREEPER_HEAD, "head");
        instance.registerTagToMaterial(Material.PIGLIN_HEAD, "head");
        instance.registerTagToMaterial(Material.DRAGON_HEAD, "head");
        // 杂项
        instance.registerTagToMaterial(Material.BLAZE_POWDER, "peppery_taste", "powder");
        instance.registerTagToMaterial(Material.ECHO_SHARD, "sculk", "scute");
        instance.registerTagToMaterial(Material.GUNPOWDER, "powder");
        instance.registerTagToMaterial(Material.GLOWSTONE_DUST, "powder", "glow");
        instance.registerTagToMaterial(Material.REDSTONE, "powder");
        instance.registerTagToMaterial(Material.HEART_OF_THE_SEA, "heart");
        instance.registerTagToMaterial(Material.CREAKING_HEART, "heart");
        instance.registerTagToMaterial(Material.BONE, "bone");
        instance.registerTagToMaterial(Material.BLAZE_ROD, "rod", "mob_rod");
        instance.registerTagToMaterial(Material.BREEZE_ROD, "rod", "mob_rod");
        instance.registerTagToMaterial(Material.STICK, "rod");
        instance.registerTagToMaterial(Material.RESIN_CLUMP, "ropy", "resin", "clump");
        instance.registerTagToMaterial(Material.GHAST_TEAR, "ghast", "tear");
        instance.registerTagToMaterial(Material.TRIAL_KEY, "key");
        instance.registerTagToMaterial(Material.OMINOUS_TRIAL_KEY, "key", "ominous");
    }

    @Override
    public void registerItem(@NotNull ArtisanItem artisanItem) {
        if (RegisterManager.isOpen()) {
            registry.put(artisanItem.getRegistryId(), artisanItem);
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

    @Override
    @Unmodifiable
    @NotNull
    public Set<NamespacedKey> getAllIds() {
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

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public @NotNull ItemStack getItemStack(NamespacedKey registryId, int count) {
        if (isArtisanItem(registryId)) return getArtisanItem(registryId).getItemStack(count);
        ItemType itemType = Registry.ITEM.get(registryId);
        if (itemType == null) throw new IllegalArgumentException("No registryId as: " + registryId.asString());
        ItemStack itemStack = itemType.createItemStack();
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
    public @NotNull ArtisanItem getArtisanItem(@NotNull ItemStack itemStack) {
        ArtisanItem artisanItem = registry.get(getRegistryId(itemStack));
        if (artisanItem == null) throw new IllegalArgumentException("You should use has method to check before get!");
        return artisanItem;
    }

    @Override
    public @NotNull @Unmodifiable Collection<NamespacedKey> getIdByTag(@NotNull String tag) {
        if (tagToId.containsKey(tag)) {
            return Collections.unmodifiableCollection(tagToId.get(tag));
        }
        return Collections.emptySet();
    }

    @Override
    public @NotNull @Unmodifiable Collection<String> getTagsById(@NotNull NamespacedKey id) {
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
