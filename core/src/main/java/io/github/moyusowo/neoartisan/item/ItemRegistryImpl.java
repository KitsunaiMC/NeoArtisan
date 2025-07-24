package io.github.moyusowo.neoartisan.item;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.RegisterManager;
import io.github.moyusowo.neoartisan.util.data.NamespacedKeyDataType;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
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
        instance.originalItemTag.forEach(
                (material, tag) -> instance.tagToId.put(tag, material.getKey())
        );
    }

    // 我的世界原版矿辞
    @InitMethod
    public static void initOriginalItemTag() {
        // 关键词：蔬菜
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.BEETROOT, "beetroot", "crop", "vegetable", "sweet");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.CARROT, "carrot", "crop", "vegetable");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.GOLDEN_CARROT, "carrot", "vegetable", "golden_food");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.POTATO, "potato", "crop", "vegetable", "raw");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.BAKED_POTATO, "potato", "vegetable", "cooked", "baked_potato", "baking");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.POISONOUS_POTATO, "potato", "crop", "vegetable", "poisonous");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.DRIED_KELP, "kelp", "cooked", "vegetable", "sushi_ingredient");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.KELP, "kelp", "crop", "vegetable", "sushi_ingredient", "raw", "water_plant", "plant");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SEAGRASS, "seagrass", "vegetable", "sushi_ingredient", "water_plant", "plant");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SEA_PICKLE, "sea_pickle", "crop", "vegetable", "sushi_ingredient", "pickle", "water_plant", "plant");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.PUMPKIN, "pumpkin", "crop", "vegetable");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.CARVED_PUMPKIN, "pumpkin", "vegetable");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.JACK_O_LANTERN, "pumpkin", "vegetable");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.FERN, "fern", "plant", "vegetable");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.LARGE_FERN, "fern", "plant", "vegetable", "tall_plant");
        // 关键词：水果
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.APPLE, "apple", "fruit");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.GOLDEN_APPLE, "apple", "golden_food", "fruit");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.ENCHANTED_GOLDEN_APPLE, "apple", "golden_food", "fruit");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.MELON, "melon", "crop", "fruit");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.MELON_SLICE, "melon", "fruit", "slice");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.GLISTERING_MELON_SLICE, "melon", "golden_food", "fruit", "slice");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SWEET_BERRIES, "berry", "crop", "fruit");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.GLOW_BERRIES, "berry", "crop", "fruit", "bone_broth_ingredient", "glow");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.CHORUS_FRUIT, "chorus", "crop", "fruit", "raw");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.POPPED_CHORUS_FRUIT, "chorus", "cooked", "fruit");
        //关键词：肉类
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.BEEF, "beef", "meat", "raw", "raw_beef");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.COOKED_BEEF, "beef", "meat", "cooked", "cooked_beef");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.PORKCHOP, "porkchop", "meat", "raw", "raw_porkchop");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.COOKED_PORKCHOP, "porkchop", "meat", "cooked", "cooked_porkchop");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.MUTTON, "mutton", "meat", "raw", "raw_mutton");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.COOKED_MUTTON, "mutton", "meat", "cooked", "cooked_mutton");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.CHICKEN, "chicken", "meat", "raw", "raw_chicken");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.COOKED_CHICKEN, "chicken", "meat", "cooked", "cooked_chicken");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.RABBIT, "rabbit", "rabbit_meat", "meat", "raw", "raw_rabbit");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.COOKED_RABBIT, "rabbit", "rabbit_meat", "meat", "cooked", "cooked_rabbit");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.RABBIT_FOOT, "rabbit", "meat", "foot");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.ROTTEN_FLESH, "rotten_flash", "meat", "poisonous");
        //关键词：眼睛
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SPIDER_EYE, "spider_eye", "eye", "poisonous");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.FERMENTED_SPIDER_EYE, "spider_eye", "eye", "poisonous", "fermented");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.ENDER_PEARL, "ender", "eye", "pearl");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.ENDER_EYE, "ender", "eye");
        //关键词：海鲜
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.COD, "cod", "fish", "raw", "raw_cod", "seafood");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.COOKED_COD, "cod", "fish", "cooked", "cooked_cod", "seafood");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SALMON, "salmon", "fish", "raw", "raw_salmon", "seafood");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.COOKED_SALMON, "salmon", "fish", "cooked", "cooked_salmon", "seafood");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.TROPICAL_FISH, "tropical_fish", "fish", "raw", "raw_tropical_fish", "seafood");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.PUFFERFISH, "pufferfish", "fish", "raw", "raw_pufferfish", "seafood", "poisonous");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.NAUTILUS_SHELL, "nautilus_shell", "shell", "seafood");
        //关键词：蛋
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.EGG, "chicken_egg", "egg");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.TURTLE_EGG, "turtle_egg", "egg");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SNIFFER_EGG, "sniffer_egg", "egg");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.DRAGON_EGG, "dragon_egg", "egg");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.FROGSPAWN, "frogspawn", "egg");
        //关键词：蛋(1.21.5)
        //NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.BLUE_EGG, "chicken_egg", "egg");
        //NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.BROWN_EGG, "chicken_egg", "egg");

        //关键词：烘焙
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.BREAD, "bread", "wheat_bread", "baking");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.COOKIE, "cookie", "cocoa_cookie", "baking");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.CAKE, "cake", "egg_and_milk_cake", "baking");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.PUMPKIN_PIE, "pie", "pumpkin_pie", "baking");
        //关键词：汤
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.MUSHROOM_STEW, "stew", "soup", "bowled");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.BEETROOT_SOUP, "soup", "bowled");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.RABBIT_STEW, "stew", "soup", "bowled");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SUSPICIOUS_STEW, "stew", "soup", "bowled");
        //关键词：做饭容器 
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.BOWL, "bowl", "wooden_bowl", "container");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.GLASS_BOTTLE, "bottle", "glass_bottle", "container");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.BUCKET, "bucket", "iron_bucket", "container");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.CAULDRON, "cauldron", "iron_cauldron", "pot","container");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.BARREL, "barrel", "wooden_barrel", "container");
        //关键词：热源(方块型) 
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.MAGMA_BLOCK, "magma", "heat_source");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.CAMPFIRE, "fire", "heat_source");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SOUL_CAMPFIRE, "fire", "soul", "heat_source");
        //关键词：饮料、液体
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.HONEY_BOTTLE, "honey", "sweet", "drinks", "bottled", "ropy");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.MILK_BUCKET, "milk", "bucket_milk", "drinks", "bucket_liquid", "liquid");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.OMINOUS_BOTTLE, "ominous", "ominous_bottle", "drinks", "bottled");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.EXPERIENCE_BOTTLE, "experience", "experience_bottle", "drinks", "bottled");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.POTION, "potion", "drink_potion", "drinks", "bottled");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SPLASH_POTION, "potion", "splash_potion", "drinks", "bottled");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.LINGERING_POTION, "potion", "lingering_potion", "drinks", "bottled");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.DRAGON_BREATH, "potion", "dragon_breath", "drinks", "bottled");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.WATER_BUCKET, "water", "bucket_water", "bucket_liquid", "liquid");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.LAVA_BUCKET, "lava", "bucket_lava", "bucket_liquid", "liquid");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.POWDER_SNOW_BUCKET, "snow", "bucket_powder_snow", "bucket_solid");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.COD_BUCKET, "cod", "bucket_cod", "bucket_mob");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SALMON_BUCKET, "salmon", "bucket_salmon", "bucket_mob");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.TROPICAL_FISH_BUCKET, "tropical_fish", "bucket_tropical_fish", "bucket_mob");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.PUFFERFISH_BUCKET, "pufferfish", "bucket_pufferfish", "bucket_mob");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.AXOLOTL_BUCKET, "axolotl", "bucket_axolotl", "bucket_mob");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.TADPOLE_BUCKET, "tadpole", "bucket_tadpole", "bucket_mob");
        //关键词：糖类、食物原材料
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.WHEAT, "wheat", "crop", "ingredient");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SUGAR, "sugar", "sweet", "ingredient", "powder");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.HONEYCOMB, "honey", "sweet", "ingredient");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.HONEY_BLOCK, "honey", "sweet", "honey_block");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.HONEYCOMB_BLOCK, "honey", "sweet", "honey_block");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SUGAR_CANE, "sugar_cane", "sweet", "sugar", "ingredient", "plant");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.CACTUS, "cactus", "ingredient", "plant");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.NETHER_WART, "wart", "nether_wart", "ingredient", "plant", "nether_plant", "crop");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.COCOA_BEANS, "cocoa_beans", "ingredient", "plant", "bean", "chocolate_ingredient");
        //关键词：菌类(蘑菇)
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.BROWN_MUSHROOM, "mushroom", "brown_mushroom", "ingredient", "mushroom_ingredient", "bone_broth_ingredient");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.RED_MUSHROOM, "mushroom", "red_mushroom", "ingredient", "mushroom_ingredient", "bone_broth_ingredient");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.MUSHROOM_STEM, "mushroom", "mushroom_block");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.BROWN_MUSHROOM_BLOCK, "mushroom", "brown_mushroom", "mushroom_block");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.RED_MUSHROOM_BLOCK, "mushroom", "red_mushroom", "mushroom_block");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.CRIMSON_FUNGUS, "mushroom", "crimson_fungus", "ingredient", "mushroom_ingredient");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.WARPED_FUNGUS, "mushroom", "warped_fungus", "ingredient", "mushroom_ingredient");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.CRIMSON_ROOTS, "mushroom", "crimson_roots", "roots");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.WARPED_ROOTS, "mushroom", "warped_roots", "roots");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SHROOMLIGHT, "mushroom", "mushroom_block");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.MYCELIUM, "mushroom", "mushroom_block");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.CRIMSON_NYLIUM, "mushroom", "mushroom_block");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.WARPED_NYLIUM, "mushroom", "mushroom_block");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.WARPED_WART_BLOCK, "wart");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.NETHER_WART_BLOCK, "wart", "nether_wart");
        //关键词：种子
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.WHEAT_SEEDS, "wheat_seeds", "seed");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.PUMPKIN_SEEDS, "pumkin_seeds", "seed");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.MELON_SEEDS, "melon_seeds", "seed");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.BEETROOT_SEEDS, "beetroot_seeds", "seed");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.TORCHFLOWER_SEEDS, "torchflower_seeds", "seed", "ancient_seeds", "ancient");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.PITCHER_POD, "pitcher_pod", "seed","ancient_seeds", "ancient");
        //关键词：植物、花
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.NETHER_SPROUTS, "plant", "nether_plant");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.WEEPING_VINES,  "plant", "vine", "nether_plant");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.TWISTING_VINES,  "plant", "vine", "nether_plant");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.AZALEA, "plant");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.FLOWERING_AZALEA, "plant", "flower");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SHORT_GRASS, "plant", "grass");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.DEAD_BUSH, "plant");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.DANDELION,  "plant", "flower");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.POPPY,  "plant", "flower");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.BLUE_ORCHID,  "plant", "flower");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.ALLIUM,  "plant", "flower");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.AZURE_BLUET,  "plant", "flower");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.RED_TULIP,  "plant", "flower");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.ORANGE_TULIP,  "plant", "flower");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.WHITE_TULIP,  "plant", "flower");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.PINK_TULIP,  "plant", "flower");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.OXEYE_DAISY,  "plant", "flower");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.CORNFLOWER,  "plant", "flower");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.LILY_OF_THE_VALLEY,  "plant", "flower");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.TORCHFLOWER,  "plant", "flower", "ancient", "peppery_taste");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.CLOSED_EYEBLOSSOM,  "plant", "flower");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.OPEN_EYEBLOSSOM,  "plant", "flower");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.WITHER_ROSE,  "plant", "flower", "poisonous");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.PINK_PETALS,  "plant", "flower");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SPORE_BLOSSOM,  "plant", "flower");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.BAMBOO,  "plant", "bamboo");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.VINE,  "plant", "vine");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.TALL_GRASS,  "plant", "tall_plant", "grass");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SUNFLOWER,  "plant", "tall_plant", "flower");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.LILAC,  "plant", "tall_plant", "flower");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.ROSE_BUSH,  "plant", "tall_plant", "flower");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.PEONY,  "plant", "tall_plant", "flower");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.PITCHER_PLANT,  "plant", "tall_plant", "flower", "ancient");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.BIG_DRIPLEAF,  "plant", "tall_plant");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SMALL_DRIPLEAF,  "plant");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SCULK_VEIN,  "plant");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.CHORUS_PLANT,  "plant", "chorus");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.CHORUS_FLOWER,  "plant", "chorus", "flower");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.GLOW_LICHEN,  "plant", "bone_broth_ingredient");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.HANGING_ROOTS,  "plant", "bone_broth_ingredient", "roots");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.PALE_HANGING_MOSS,  "plant", "roots");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.LILY_PAD,  "plant", "water_plant");
        //1.21.5的植物
        //NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.CACTUS_FLOWER,  "plant", "flower");
        //NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.WILDFLOWERS,  "plant", "flower");
        //NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.LEAF_LITTER,  "plant");
        //NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.FIREFLY_BUSH,  "plant", "firefly", "bush");
        //NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.BUSH,  "plant", "bush");
        //NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SHORT_DRY_GRASS,  "plant", "grass");
        //NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.TALL_DRY_GRASS,  "plant", "grass", "tall_plant");

        //关键词：冰
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.ICE, "ice", "drinks_ingredient");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.PACKED_ICE, "ice", "drinks_ingredient");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.BLUE_ICE, "ice", "drinks_ingredient");
        //关键词：雪
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SNOW_BLOCK, "snow");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SNOW, "snow");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SNOWBALL, "snow", "ball", "drinks_ingredient");
        //关键词：球形
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SLIME_BALL, "slime", "ball", "ropy");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.CLAY_BALL, "clay", "ball");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.MAGMA_CREAM, "magma", "ball", "ropy");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.FIRE_CHARGE, "fire", "ball");
        //关键词：树苗
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.OAK_SAPLING, "sapling");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SPRUCE_SAPLING, "sapling");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.BIRCH_SAPLING, "sapling");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.JUNGLE_SAPLING, "sapling");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.ACACIA_SAPLING, "sapling");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.DARK_OAK_SAPLING, "sapling");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.MANGROVE_PROPAGULE, "sapling");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.CHERRY_SAPLING, "sapling");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.PALE_OAK_SAPLING, "sapling");
        //关键词：动物皮毛
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.LEATHER, "skin", "animal_pelage");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.RABBIT_HIDE, "skin", "animal_pelage");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.FEATHER, "feather", "animal_pelage");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.TURTLE_SCUTE, "scute", "animal_pelage");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.ARMADILLO_SCUTE, "scute", "animal_pelage");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.INK_SAC, "sac", "animal_pelage");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.GLOW_INK_SAC, "sac", "animal_pelage");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.PRISMARINE_SHARD, "scute", "animal_pelage");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.PRISMARINE_CRYSTALS, "scute", "animal_pelage");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.PHANTOM_MEMBRANE, "scute", "animal_pelage");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SHULKER_SHELL, "shell", "animal_pelage", "container");
        //关键词：头颅
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.SKELETON_SKULL, "head");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.WITHER_SKELETON_SKULL, "head");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.PLAYER_HEAD, "head");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.ZOMBIE_HEAD, "head");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.CREEPER_HEAD, "head");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.PIGLIN_HEAD, "head");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.DRAGON_HEAD, "head");
        // 杂项
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.BLAZE_POWDER, "peppery_taste", "powder");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.ECHO_SHARD, "sculk", "scute");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.GUNPOWDER, "powder");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.GLOWSTONE_DUST, "powder", "glow");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.REDSTONE, "powder");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.HEART_OF_THE_SEA, "heart");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.CREAKING_HEART, "heart");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.BONE, "bone");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.BLAZE_ROD, "rod", "mob_rod");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.BREEZE_ROD, "rod", "mob_rod");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.STICK, "rod");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.RESIN_CLUMP, "ropy", "resin", "clump");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.GHAST_TEAR, "ghast", "tear");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.TRIAL_KEY, "key");
        NeoArtisanAPI.getItemRegistry().registerTagToMaterial(Material.OMINOUS_TRIAL_KEY, "key", "ominous");
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
