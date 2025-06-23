# NeoArtisan - Minecraft 高版本自定义内容框架

![License](https://img.shields.io/badge/license-GPL3-green)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.4-blue)
![API](https://img.shields.io/badge/API-Paper%20Compatible-orange)

[English Version Documentation](README_EN.md)

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
    <span>开发阶段警告</span>
  </div>

  <p style="margin: 0.5rem 0">
    <strong>当前版本：0.x.y（开发预览版）</strong>
  </p>

  <ul style="
      margin: 0.5rem 0;
      padding-left: 1.5rem;
  ">
    <li>🚨 <strong>无兼容性承诺</strong>：不遵循语义化版本控制规范</li>
    <li>⚡ <strong>变更风险</strong>：任何更新都可能包含API破坏性变更</li>
    <li>💥 <strong>版本升级规则</strong>：
      <ul style="padding-left: 1.5rem; margin: 0.25rem 0">
        <li>修订号更新（0.x.<strong>y</strong>）：<em>可能</em>包含破坏性变更</li>
        <li>次版本更新（0.<strong>x</strong>.y）：<em>必定</em>包含破坏性变更</li>
      </ul>
    </li>
  </ul>

  <p style="
      margin: 0.75rem 0 0.25rem;
      font-style: italic;
  ">
    <strong>稳定版计划</strong>：1.0.0版本发布时开始遵循语义化版本控制（时间待定）
  </p>
</div>

## 项目概述

NeoArtisan 是一个面向 Minecraft 1.21.4+ 的自定义内容框架，基于 GPL3 协议开源，专注于为插件开发者提供灵活的内容创建能力，同时保持与 PaperAPI 的良好兼容性。

## 核心设计理念

### 🧩 纯粹的基础框架
- **不包含默认内容**：仅提供框架和工具，不实现任何具体物品/方块
- **非全功能API**：专注于核心功能，保持精简
- **PaperAPI实现**：兼容性较好，不过度使用NMS
- **扩展友好**：完善的扩展点设计，支持开发内容包插件

### 🧱 自定义方块系统
- **多类型支持**：普通方块、作物、薄型方块、（半）透明方块
- **状态管理**：每个方块支持多状态切换(如作物生长阶段)
- **NMS优化**：基于NMSBlockState重映射技术，直接内部修改发包
- **事件体系**：放置/破坏/交互事件支持

### 🛡️ 自定义物品系统
- **属性系统**：全局属性、物品堆属性和玩家属性三级架构
- **类型支持**：武器、防具、食物等全品类物品
- **NBT集成**：完善的持久化数据容器支持

### 🔮 自定义配方系统
- **扩展性强**：目前支持合成台，以后可支持熔炉、铁砧、锻造台等
- **独立架构**：不依赖原版配方系统，避免冲突

### 🛠️ 开发者体验
- **建造者模式**：简化内容对象创建
- **清晰文档**：完整的Javadoc和示例
- **事件系统**：覆盖方块生命周期关键事件
- **效率优先哲学**：我们坚信开发效率的价值，用简洁API满足绝大多数需求

## 快速开始

### 添加依赖

```Gradle
repositories {
    mavenCentral()
    maven { url 'https://jitpack.io' }
}

dependencies {
    CompileOnly 'com.github.MoYuSOwO:NeoArtisan:-SNAPSHOT:api'
}
```

### 创建自定义物品和方块示例

```java
// 注册到服务器
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

## 架构优势

1. **清晰的扩展点**
    - 内容注册接口标准化
    - 存在扩展性强的事件监听机制

2. **Paper友好集成**
    - 基于Paper事件系统构建
    - 使用ServicesManager实现服务发现

3. **协作开发支持**
    - 模块化内容包设计
    - 避免ID冲突的命名空间管理
    - 建造者模式简化对象创建
    - 完善的Javadoc文档

## 开发建议

1. **内容包开发**
    - 建议每个内容包作为独立插件
    - 通过服务接口与其他插件交互

2. **GUI实现**
    - 继承`ArtisanBlockGUI`简化开发
    - 自动处理库存事件

3. **版本兼容**
    - 目前项目处于起步阶段，仅保证1.21.4兼容性，可能随版本更新而更新

## 一些计划

- 开发更多的方块类型
- 加入除工作台配方外的合成管理
- 加入物流、电力等科技相关内容的API扩展
- 还有更多还在想

## 贡献与协作

我们欢迎各种形式的贡献：
- 内容包插件开发
- 核心框架改进
- 文档和示例补充
- ~~作者作者你写的代码就是一坨，看我出招！~~

请遵循：
1. GPL3协议要求
2. 保持API精简性
3. 完善的文档说明

## 许可证

GNU General Public License v3.0 - 详情见LICENSE文件

## 联系

MoYuOwO@outlook.com
