# NeoArtisan - Minecraft 高版本自定义内容微框架

![License](https://img.shields.io/badge/license-GPL3-green)
![Minecraft](https://img.shields.io/badge/Minecraft-1.21.4+-blue)
![API](https://img.shields.io/badge/API-Paper%20Compatible-orange)

[English →](./README_EN.md)


<details>
<summary>⚠️ 开发预览版小贴士（点一下展开）</summary>

- 当前版本 0.x.y，API 可能随小版本变动  
- 锁定版本号后再上生产服
- 1.0.0 起开始语义化版本
</details>

---

### ✅ 「一句话概述」

> NeoArtisan 是一个 **基于 PaperAPI的，专为 Minecraft 插件开发者准备的高版本自定义内容微框架**：  
> 用一行 Builder 就能注册带模型、可交互、可掉落的自定义物品 / 方块 / 配方，  
> 自带生命周期事件与 GUI 绑定，**不写 NMS、不碰资源包、不依赖重型内容平台**。  
> 适合想快速交付玩法、又嫌 Slimefun 太重、从零撸 NMS 太累的开发者。


- **开发者友好**：全部基于 Paper API + PacketEvents
- **零默认内容**：只提供注册器 & 事件，不给任何现成机器或武器，保持最小依赖
- **可插拔逻辑**：作物掉落、机器产出、GUI 按钮仍由你写，框架只做「存-取-事件路由」

---


### ✅ 「30 秒看懂」

| 典型场景                 | 传统手写                             | NeoArtisan 实际写法（取自你的测试代码）                                                                                                                                                                                            |
|----------------------|----------------------------------|----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **加一把带模型、可右键放置方块的剑** | YAML（拓展性差）或手动管理物品                | **1 个 Builder 链**<br>`ArtisanItem.builder()`<br>`.registryId(NamespacedKey.fromString("myplugin:magic_sword"))`<br>`.rawMaterial(Material.IRON_SWORD)`<br>`.itemModel(key)`<br>`.blockId(cooking_pot)`<br>`.build()` |
| **注册一个 3 阶段作物方块**    | 需要自己管理自定义作物各种各样的事件交互，或深入nms，极其复杂 | **1 个 ArtisanCropBuilder**<br>`ArtisanCropBlock.builder()`<br>`.blockId("magic_crop")`<br>`.states(...3 个阶段...)`<br>`.boneMealMinGrowth(0)`<br>`.boneMealMaxGrowth(2)`<br>`.build()`                                 |
| **给厨锅做一个 GUI**       | 需要手动实现方块生命周期管理                   | **1 个 ArtisanBlockGUI 子类**<br>`class CookingPotGUI extends ArtisanBlockGUI {`<br>`    protected void init() { /* 放按钮 */ }`<br>`}`                                                                                    |
| **加一条新配方**           | 原版配方逻辑不能兼容自定义物品                  | **1 个 RecipeBuilder**<br>`ArtisanShapedRecipe.builder()`<br>`.key(NamespacedKey.fromString("myplugin:soup"))`<br>`.set("A B")`<br>`.add('A', choice)`<br>`.resultGenerator(generator)`<br>`.build()`                 |

> 框架只干「注册-保存-事件路由」，业务逻辑仍由你写，**不写 NMS、不碰 YAML、不继承臃肿资源包框架**。

---

## 3 步跑起来

1. **加仓库 & 依赖**  
```gradle
repositories { maven { url 'https://jitpack.io' } }
dependencies { compileOnly 'com.github.MoYuSOwO:NeoArtisan:-SNAPSHOT:api' }
```

2. **启用框架**  
   把 `NeoArtisan.jar` 和前置 `packetevents.jar` 一起扔进 `plugins/` 文件夹。

3. **一行注册**
```java
public final class MyPlugin extends JavaPlugin {
    @Override
    public void onEnable() {
        NeoArtisanAPI.getItemRegistry().register(
            ArtisanItem.builder()
                .registryId(new NamespacedKey(this, "magic_sword"))
                .rawMaterial(Material.IRON_SWORD)
                .displayName("<yellow>魔法剑</yellow>")
                .build()
        );
    }
}
```
完整示例 → [农夫乐事Paper复刻版](https://github.com/KitsunaiMC/FarmersDelightRepaper)

---

## 还能做什么？
<details>
<summary>点开看功能清单</summary>

- **方块** 普通 / 作物 / 透明 / 头颅 / 压力板 / （未来）多方块结构
- **物品** 武器 / 防具 / 食物 / 完全自定义
- **配方** 工作台 / 熔炉 / 后续支持铁砧、酿造
- **GUI** 继承 `ArtisanBlockGUI` 大大简化逻辑
- **物流 & 机器 API**（未来）节点-连线抽象，具体逻辑作者写
</details>

---

## 这不是什么
- ❌ **不是整合包**：0 默认内容，只给 API
- ❌ **不是 IA / Oraxen**：不负责模型、贴图、音效
- ❌ **不是 Slimefun 2.0**：不提供现成机器，只让你更快写自己的机器

---

## 参与 & 吐槽
- 提 Issue / PR
- 邮件：MoYuOwO@outlook.com
- GPL3 开源，随意折腾

---

> 如果你嫌 Slimefun 太重、IA 太美术、从零写 NMS 太累——  
> 用 NeoArtisan，把“注册”这件事一次性搞定。