# NeoArtisan - High-Version Minecraft Custom Content Framework

![License](https://img.shields.io/badge/license-GPL3-green)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.4-blue)
![API](https://img.shields.io/badge/API-Paper%20Compatible-orange)

[中文文档](README.md)

<div class="warning" style="
    background: #fff8e6;
    border-left: 4px solid #ffc107;
    padding: 1rem;
    margin: 1.5rem 0;
    border-radius: 0 4px 4px 0;
">
  <div style="
      display: flex;
      align-items: center;
      margin-bottom: 0.5rem;
      color: #d84315;
      font-weight: bold;
  ">
    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" style="margin-right: 8px">
      <path d="M12 2C6.48 2 2 6.48 2 12C2 17.52 6.48 22 12 22C17.52 22 22 17.52 22 12C22 6.48 17.52 2 12 2ZM13 17H11V15H13V17ZM13 13H11V7H13V13Z" fill="currentColor"/>
    </svg>
    <span>DEVELOPMENT STAGE WARNING</span>
  </div>

  <p style="margin: 0.5rem 0">
    <strong>Current version: 0.x.y (Development Preview)</strong>
  </p>

  <ul style="
      margin: 0.5rem 0;
      padding-left: 1.5rem;
  ">
    <li>🚨 <strong>No Compatibility Guarantee</strong>: Does not follow Semantic Versioning spec</li>
    <li>⚡ <strong>Change Risks</strong>: Any update may contain breaking API changes</li>
    <li>💥 <strong>Versioning Rules</strong>:
      <ul style="padding-left: 1.5rem; margin: 0.25rem 0">
        <li>Patch updates (0.x.<strong>y</strong>): <em>May</em> contain breaking changes</li>
        <li>Minor updates (0.<strong>x</strong>.y): <em>Will</em> contain breaking changes</li>
      </ul>
    </li>
  </ul>

  <p style="
      margin: 0.75rem 0 0.25rem;
      font-style: italic;
  ">
    <strong>Stability Plan</strong>: Semantic Versioning compliance begins at v1.0.0 (ETA TBD)
  </p>
</div>

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
- Extensible design (currently only crafting table and furnace)
- Independent architecture

### 🛠️ Developer Experience
- Builder pattern
- Comprehensive documentation
- Lifecycle event system
- **Efficiency-First Philosophy**: We believe in the value of development efficiency, meeting most needs with concise APIs


## Quick Start

### Add Dependency

```gradle
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    compileOnly 'com.github.MoYuSOwO:NeoArtisan:<api_version>:api'
}
```

### Example Usage

```java
// register to server
@NeoArtisanAPI.Register
public void registerContent() {
    NeoArtisanAPI.getBlockRegistry().register(
            ArtisanTransparentBlock.factory().builder()
                    .blockId(ItemTest.cooking_pot)
                    .canBurn(false)
                    .states(
                            List.of(
                                    ArtisanTransparentBlockState.factory().builder()
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
            ArtisanItem.factory().builder()
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
                            5,
                            1,
                            null
                    )
                    .maxDurability(2500)
                    .build()
    );
    NeoArtisanAPI.getItemRegistry().registerItem(
            ArtisanItem.factory().builder()
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
                            1.0f,
                            1.5f,
                            11.0f
                    )
                    .maxDurability(5000)
                    .build()
    );
    NeoArtisanAPI.getItemRegistry().registerItem(
            ArtisanItem.factory().complexBuilder()
                    .registryId(cooking_pot)
                    .itemStack(() -> {
                        ItemStack itemStack = ItemStack.of(Material.PAPER);
                        itemStack.setData(DataComponentTypes.ITEM_NAME, Component.text("<green>烹饪锅"));
                        itemStack.setData(DataComponentTypes.ITEM_MODEL, cooking_pot);
                        return itemStack;
                    })
                    .blockId(cooking_pot)
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
- Add support for more recipe types
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
