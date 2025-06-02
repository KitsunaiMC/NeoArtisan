# NeoArtisan - High-Version Minecraft Custom Content Framework

![License](https://img.shields.io/badge/license-GPL3-green)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.4-blue)
![API](https://img.shields.io/badge/API-Paper%20Compatible-orange)

[中文文档](README.md)

## Project Overview

NeoArtisan is a custom content framework for Minecraft 1.21.4+, licensed under GPL3, focused on providing flexible content creation capabilities for plugin developers while maintaining good compatibility with PaperAPI.

## Core Design Philosophy

### 🧩 Pure Foundation Framework
- **No Default Content**: Only provides framework and tools
- **Non-Comprehensive API**: Focused on core functionality
- **PaperAPI Implementation**: Good compatibility, minimal NMS usage
- **Extension-Friendly**: Well-designed extension points

### 🧱 Custom Block System
- Multiple block types supported
- State management with multi-state transitions
- NMS-optimized packet handling
- Complete event system

### 🛡️ Custom Item System
- Three-tier attribute system
- Full category support (weapons, armor, food, etc.)
- NBT integration

### 🔮 Custom Recipe System
- Extensible design (currently only crafting table)
- Independent architecture

### 🛠️ Developer Experience
- Builder pattern
- Comprehensive documentation
- Lifecycle event system

## Quick Start

### Add Dependency

```gradle
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.MoYuSOwO:NeoArtisan:1.0:api'
}
```

### Example Usage

```java
// register to server
@NeoArtisanAPI.Register
public void registerContent() {
    NeoArtisanAPI.getBlockRegistry().register(
            ArtisanTransparentBlock.builder()
                    .blockId(ItemTest.cooking_pot)
                    .canBurn(false)
                    .states(
                            List.of(
                                    ArtisanTransparentBlockState.builder()
                                            .appearanceState(
                                                    new TransparentAppearance(
                                                            TransparentAppearance.LeavesAppearance.OAK_LEAVES,
                                                            1,
                                                            false,
                                                            false
                                                    )
                                            )
                                            .generators(
                                                    new ItemGenerator[]{
                                                            ItemGenerator.simpleGenerator(
                                                                    ItemTest.cooking_pot,
                                                                    1
                                                            )
                                                    }
                                            )
                                            .build()
                            )
                    )
                    .build()
    );
    NeoArtisanAPI.getItemRegistry().registerItem(
            ArtisanItem.builder()
                    .registryId(magic_helmet)
                    .rawMaterial(Material.IRON_HELMET)
                    .displayName("<aqua>魔法头盔~")
                    .lore(
                            List.of(
                                    "魔法头盔一顶",
                                    "可以帮助你挡住下落的蜘（ji）蛛（ju）"
                            )
                    )
                    .armorProperty(
                            new ArmorProperty(
                                    5,
                                    1,
                                    null
                            )
                    )
                    .maxDurability(2500)
                    .build()
    );
    NeoArtisanAPI.getItemRegistry().registerItem(
            ArtisanItem.builder()
                    .registryId(magic_sword)
                    .rawMaterial(Material.IRON_SWORD)
                    .displayName("<yellow>魔法剑~")
                    .lore(
                            List.of(
                                    "魔法剑一把",
                                    "可以帮助你更快地杀怪"
                            )
                    )
                    .weaponProperty(
                            new WeaponProperty(
                                    1.0f,
                                    1.5f,
                                    11.0f
                            )
                    )
                    .maxDurability(5000)
                    .build()
    );
    NeoArtisanAPI.getItemRegistry().registerItem(
            ArtisanItem.builder()
                    .registryId(cooking_pot)
                    .rawMaterial(Material.PAPER)
                    .displayName("烹饪锅")
                    .blockId(cooking_pot)
                    .itemModel(cooking_pot)
                    .build()
    );
}
```

## Architectural Advantages

1. Clear extension points
2. Paper-friendly integration
3. Collaborative development support

## Development Recommendations

1. Develop content packs as separate plugins
2. Extend `ArtisanBlockGUI` for custom GUIs
3. Currently only guarantees 1.21.4 compatibility

## Future Plans

- Develop more block types
- Add support for non-crafting table recipes (furnace, anvil, etc.)
- Implement API extensions for technical content (logistics, electricity systems)
- More features under consideration

## Contribution

We welcome:
- Content pack development
- Core improvements
- Documentation enhancements

Requirements:
1. Follow GPL3 license
2. Maintain API simplicity
3. Provide proper documentation

## License

GNU General Public License v3.0 - See LICENSE file

## Contact

MoYuOwO@outlook.com
