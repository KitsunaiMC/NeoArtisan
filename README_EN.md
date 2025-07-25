# NeoArtisan – A lightweight content micro-framework for Minecraft 1.21.4+

![License](https://img.shields.io/badge/license-GPL3-green)  
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.4+-blue)  
![API](https://img.shields.io/badge/API-Paper%20Compatible-orange)

[中文版](./README.md)

<details>
<summary>⚠️ Development preview notes (click to expand)</summary>

- Current version 0.x.y – API may change in minor updates
- Pin the version before using in production
- Semantic versioning starts from 1.0.0
</details>

---

### ✅ One-sentence pitch

> NeoArtisan is a **Paper-API-based micro-framework made for Minecraft plugin developers**:  
> register custom items / blocks / recipes with model, interaction and drops in one fluent Builder,  
> complete with lifecycle events and GUI hooks – **no NMS, no resource-pack hassle, no heavyweight content platform**.  
> Perfect for devs who want to ship features fast but find Slimefun too heavy and raw NMS too painful.

- **Developer-friendly**: built entirely on Paper API + PacketEvents
- **Zero default content**: only registries & events – no premade machines or weapons, minimal footprint
- **Pluggable logic**: crop drops, machine output, GUI buttons remain yours; the framework just handles “store–retrieve–route events”

---

### ✅ 30-second tour

| Typical task                                                                 | Traditional way                                                   | NeoArtisan (from real test code)                                                                                                                                                                                         |
|------------------------------------------------------------------------------|-------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **Add a sword with custom model that can be right-clicked to place a block** | YAML (limited) or manual item management                          | **One Builder chain**<br>`ArtisanItem.builder()`<br>`.registryId(NamespacedKey.fromString("myplugin:magic_sword"))`<br>`.rawMaterial(Material.IRON_SWORD)`<br>`.itemModel(key)`<br>`.blockId(cooking_pot)`<br>`.build()` |
| **Register a 3-stage crop block**                                            | Hand-roll custom crop events or dive into NMS – extremely complex | **One ArtisanCropBuilder**<br>`ArtisanCropBlock.builder()`<br>`.blockId("magic_crop")`<br>`.states(...3 stages...)`<br>`.boneMealMinGrowth(0)`<br>`.boneMealMaxGrowth(2)`<br>`.build()`                                  |
| **Create a cooking-pot GUI**                                                 | Manually implement block lifecycle management                     | **One ArtisanBlockGUI subclass**<br>`class CookingPotGUI extends ArtisanBlockGUI {`<br>`    protected void init() { /* place buttons */ }`<br>`}`                                                                        |
| **Add a new recipe**                                                         | Vanilla recipe logic can’t handle custom items                    | **One RecipeBuilder**<br>`ArtisanShapedRecipe.builder()`<br>`.key(NamespacedKey.fromString("myplugin:soup"))`<br>`.set("A B")`<br>`.add('A', choice)`<br>`.resultGenerator(generator)`<br>`.build()`                     |

> The framework only does “register–save–route events”; your business logic stays yours.  
> **No NMS, no YAML, no bulky resource-pack frameworks.**

---

## Get started in 3 steps

1. **Add repository & dependency**
```gradle
repositories { maven { url 'https://jitpack.io' } }
dependencies { compileOnly 'com.github.MoYuSOwO:NeoArtisan:-SNAPSHOT:api' }
```

2. **Enable the framework**  
   Drop `NeoArtisan.jar` and its dependency `packetevents.jar` into the `plugins/` folder.

3. **One-line registration**
```java
public final class MyPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        NeoArtisanAPI.getItemRegistry().register(
            ArtisanItem.builder()
                .registryId(new NamespacedKey(this, "magic_sword"))
                .rawMaterial(Material.IRON_SWORD)
                .displayName("<yellow>Magic Sword</yellow>")
                .build()
        );
    }
}
```
Full example → [FarmersDelightRepaper](https://github.com/KitsunaiMC/FarmersDelightRepaper)

---

## What else can it do?
<details>
<summary>Expand the feature list</summary>

- **Blocks**: normal / crop / transparent / skull / pressure plate / (future) multiblock
- **Items**: weapons / armor / food / fully custom
- **Recipes**: crafting table / furnace / upcoming anvil & brewing support
- **GUI**: extend `ArtisanBlockGUI` to cut boilerplate to almost zero
- **Logistics & machine API** (planned): node–edge abstraction, logic left to you
</details>

---

## What it is **not**
- ❌ **Not a modpack**: zero default content, pure API only
- ❌ **Not IA / Oraxen**: we don’t handle models, textures or sounds
- ❌ **Not Slimefun 2.0**: no ready-made machines, just a faster way to write your own

---

## Contribute & feedback
- Open an issue / PR
- Email: MoYuOwO@outlook.com
- GPL3 open source – hack away

---

> If Slimefun feels too heavy, IA too artist-centric, and raw NMS too daunting –  
> grab NeoArtisan and get “registration” done once and for all.