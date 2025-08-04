# 更新日志 Change log

> 本文档遵循 [Keep a Changelog](https://keepachangelog.com/) 规范
> This document follows the [Keep a Changelog](https://keepachangelog.com/) specification

## [1.0.0] - 2025-08-04
### 修复 / Fixed
- **增加了命令短别名** / Added short aliases of command

## [1.0.0-rc.5] - 2025-08-04
### 修复 / Fixed
- **修复了依赖打包时没有正确重定位的问题** / Fixed that dependency packaging does not correctly relocate

## [1.0.0-rc.4] - 2025-08-04
### 修复 / Fixed
- **修复了区块包处理过慢阻塞netty线程的问题** / Fixed that the chunk data package processing is too slow and blocks the netty thread

## [1.0.0-rc.3] - 2025-07-29
### 修复 / Fixed
- **修复了 `MultiChoice` 匹配要全部匹配时才返回 `true` 的问题** / Fixed that the `MultiChoice` match method only returns `true` when all choices are matched

## [1.0.0-rc.2] - 2025-07-28
### 新增 / Added
- **补充并更改 javadoc** / Updated javadoc

### 修复 / Fixed
- **修复编译器 NPE 警告（即使实际上不可能）** / Fixed NPE in compiler (actually impossible)
- **修复无法通过右击空气打开配方书 GUI 的问题** / Fixed issue where right-clicking air would open the recipe book GUI

## [1.0.0-rc.1] - 2025-07-27
### 新增 / Added
- **物品注册表新增 `getTagsByItemStack` 方法** / Added `getTagsByItemStack` method to the item registry
- **新增 `EmptyChoice` 类以规范配方选择为空时的默认实现** / Added `EmptyChoice` class to standardize the default implementation when a recipe choice is empty
- **`Choice` 接口新增 `match(NamespacedKey)` 方法** / Added `match(NamespacedKey)` method to the `Choice` interface
- **配方注册表内新增 `getAllRecipesByType` 方法** / Added `getAllRecipesByType` method to the recipe registry
- **新增配方书系统，新增命令 `/neoartisan guide`** / Added recipe book system with new command `/neoartisan guide`
- **可以通过实现 `GuideGUIGenerator` 并用 `RecipeRegistry#setGuide` 方法注册自己的配方显示器** / You can now register your own recipe display by implementing `GuideGUIGenerator` and using the `RecipeRegistry#setGuide` method
- **自定义物品新增配方书分类字段，可以用 `RecipeRegistry#setCategory` 注册自定义的类别名称和展示物品** / Custom items now have a recipe book category field; you can register custom category names and display items using `RecipeRegistry#setCategory`

### 变更 / Changed
- **移除只是对Paper的PDC简单包装的属性系统，保留自定义物品属性，默认写入PDC** / Removed the simple PDC wrapper for Paper's attribute system; retained custom item attributes with default PDC storage
- **规范移动和重命名了部分类和包** / Standardized and renamed some classes and packages
- **版本号更新至 `1.0.0-rc.1`，正式进入预发布阶段** / Updated version number to `1.0.0-rc.1`, officially entering the pre-release phase
- **`Registries` 类内的注册表常量改为隐式懒加载，防止意外触发<clinit>导致崩溃** / Changed the registry constants in the `Registries` class to implicit lazy loading to prevent accidental triggering of <clinit> causing crashes

### 修复 / Fixed
- **修复了有序配方合成时绝对位置必须一样的问题，改为相对位置一样即可合成** / Fixed the issue where absolute positions were required for shaped recipe crafting; now relative positions are sufficient
- **修复了 `BlockMappingsManager` 类内无法正确读取方块ID的问题** / Fixed the issue in the `BlockMappingsManager` class where block IDs were not being read correctly

## [0.10.0 - beta] - 2025-07-25
### 新增 / Added
- 方块创建时增加是否为方块实体的选择，默认无方块实体 / Added optional block-entity flag when creating blocks; defaults to no block entity
- 增加方块硬度API / Added block-hardness API
- 插件startup失败之后自动关闭插件及附属插件 / Plugin now auto-disables itself and its addons if startup fails
- `ItemGenerator` 接口增加默认的 `ChanceGenerator` 实现类 / Added default `ChanceGenerator` implementation to `ItemGenerator` interface
- 为物品合成和物品本身增加标签系统，支持为原版物品注册标签，支持一键获取标签，增加原版物品默认标签 / Introduced tag system for items and recipes; supports registering tags for vanilla items, one-click tag retrieval, and default vanilla tags
- 增加了对食物使用后效果和食物使用时间的支持 / Added support for post-consume effects and consume duration for food items
- `LifecycleTask` 增加区块加载检查，默认情况下不会执行未加载区域的方块绑定的生命周期任务 / `LifecycleTask` now checks chunk load status; lifecycle tasks bound to blocks in unloaded chunks are skipped by default
- 重构 `BlockEntity`，增加索引以提升性能 / Refactored `BlockEntity` with added indexing for better performance

### 变更 / Changed
- 删除 `@NeoArtisanAPI.Register` 注解，改为在附属插件的onEnable内注册 / Removed `@NeoArtisanAPI.Register` annotation; registration now happens in addon plugins' `onEnable`
- 方块储存依赖 `World` 的 UUID 而非 `World` 对象本身 / Block storage now uses `World` UUID instead of the `World` object
- 用命名空间键替换固定的Type，支持其他配方可能，配方增加多物品输出可能 / Replaced fixed Type with namespaced keys to allow other recipe types and multiple outputs
- 增加 Choice API，重构配方系统，Registry 只起到分类储存的作用 / Added Choice API and refactored recipe system; registries now only store and classify recipes
- 部分非必须 Builder 条目改为 `@Nullable` / Marked optional builder fields as `@Nullable`
- 修改命令格式，增加玩家选择器 / Updated command format and added player selectors

### 修复 / Fixed
- 修复压力板类方块能踩下去的问题 / Fixed pressure-plate-like blocks being steppable
- 修复了树叶方块 canBurn 特性没实现的问题 / Fixed missing `canBurn` behavior for leaf blocks
- 修复了自定义方块替换方法内不发包从而玩家获取不到更新的问题 / Fixed missing packets in custom block replacement, preventing client updates
- 修复了玩家踩踏自定义作物导致其掉落之后陷入耕地里面的问题 / Fixed players sinking into farmland after trampling custom crops and causing them to drop
- 修复给自定义作物放骨粉不消耗骨粉的bug / Fixed bone meal not being consumed when used on custom crops

## [0.9.0 - alpha] - 2025-07-18
### 新增 / Added
- 新增营火、高炉、烟熏炉配方支持 / Added support for campfire, blast furnace, and smoker recipes
- 新增自定义方块生命周期管理系统 / Added custom block lifecycle management system
- 新增直接操作ArtisanBlockData的API / Added direct ArtisanBlockData manipulation API

### 变更 / Changed
- 全面优化错误提示与信息反馈系统 / Completely overhauled error messaging and notification system
- 重构并重命名核心实现类提升代码可读性 / Refactored and renamed core implementation classes for better readability
- 解耦自定义方块系统：Block作为状态协调器，BlockState定义行为 / Decoupled custom block system: Block as state coordinator, BlockState defines behavior
- PDC存储迁移至Marker实体，利用原生游戏机制 / Migrated PDC storage to Marker entities using native game mechanics
- 重构方块存储系统：区块级存储 + 脏标记优化 / Refactored block storage: Chunk-level storage with dirty flag optimization
- 作物生长系统转为框架原生实现，支持多状态组合 / Crop growth system converted to native implementation with multi-state support
- 强化插件加载依赖顺序要求 / Enforced stricter plugin loading dependency order

### 修复 / Fixed
- 修复自定义头颅方块放置后状态未更新问题 / Fixed custom skull block state not updating after placement
- 修复附属插件加载时的兼容性问题 / Fixed compatibility issues during addon plugin loading

## [0.8.0 - alpha and 0.7.0 - alpha] - 2025-07-15
### 新增 / Added
- 引入保护插件钩子库，在方块交互前检查权限 / Added protection plugin hook library to check permissions before block interactions
- 物品构造器增加 `internalUse` 方法，标记内部物品不在命令补全显示 / Added `internalUse` method to Item builder for internal items excluded from command completions
- 实现更合理的命令建议系统 / Implemented more intuitive command suggestion system
- 增加 PacketEvents 网络层支持 / Added PacketEvents network layer support
- 引入全局 UUID 存储系统替代 NMS Level 对象 / Introduced global UUID storage system to replace NMS Level objects
### 变更 / Changed
- 使用 PacketEvents 重写区块方块重映射网络包处理 / Rewrote block remapping packet handling using PacketEvents
- 完全移除 NMS 和 OBC 依赖 / Completely removed NMS and OBC dependencies
- 优化方块数据存储结构 / Optimized block data storage structure
- 重构网络包处理线程模型 / Refactored packet handling threading model
- 更新跨版本兼容层实现 / Updated cross-version compatibility layer implementation
### 修复 / Fixed
- 修复保护插件交互时的边缘情况 / Fixed edge cases in protection plugin interactions
- 修正命令建议中的排序问题 / Fixed sorting issues in command suggestions
- 解决全局 UUID 存储的线程竞争问题 / Resolved thread contention in global UUID storage
- 修复 PacketEvents 集成中的包处理异常 / Fixed packet processing exceptions in PacketEvents integration

## [0.6.0 - alpha] - 2025-07-06
### 新增 / Added
- 增加头颅方块ArtisanHeadBlock / Added ArtisanHeadBlock for custom skull blocks
- 增加物品Builder的头颅参数 / Added skull parameters to Item Builder
- 对作物方块外观增加了直接使用原版作物外观的选择，适配不想强制适用资源包的开发者 / Added vanilla crop appearance option for artisan crops, supporting developers who don't want to enforce resource packs
- 阻止自定义物品参与村民交易事件 / Prevent custom items from participating in villager trade events

### 变更 / Changed
- 将方块生命周期事件类独立出GUI包 / Moved block lifecycle event classes out of GUI package
- 将不希望再继承的一些类增加final标记 / Marked certain classes as final to prevent inheritance
- 重构文档 / Refactored documentation

### 修复 / Fixed
- 修复了一些警告信息 / Fixed various warning messages
- 修改了部分包结构与类可见性，确保安全 / Adjusted package structure and class visibility for enhanced security
- 事件监听方法改为规范的public void前缀 / Standardized event listener methods with public void prefix

## [0.5.0 - alpha] - 2025-06-23
### 新增 / Added
- ArtisanItem增加复杂构造器，支持传入ItemStack的Supplier从而动态生成ItemStack / Added complex constructor to ArtisanItem supporting ItemStack Supplier for dynamic generation
- 增加初始化时反射调用异常的原因显示 / Added cause display for reflection invocation exceptions during initialization
- 增加Block内Builder的基接口，用于标记 / Added base Builder interface in Block for type marking
- 重构方块GUI抽象基类，增加了对生命周期任务的管理，增加了注销GUI时关闭玩家库存的逻辑，规范了对子类重写方法的空异常处理 / Refactored block GUI base class with lifecycle task management, inventory closing on unregister, and standardized null exception handling
- 增加ArtisanFullBlock完整方块，物理逻辑为石头，挖掘逻辑为音符盒，但不能被活塞推动和拉动 / Added ArtisanFullBlock with stone physics, note box mining logic, but piston-immune
- 增加了Factory接口，修复了服务管理器单例复用的问题 / Added Factory interface, fixed service manager singleton reuse issue
- 增加默认配置文件，里面有debug选项 / Added default configuration file with debug option

### 变更 / Changed
- ArtisanItem的Builder中几个Property直接传入数值参数，Property类移到core模块用于内部使用 / Simplified ArtisanItem Builder with direct value parameters, moved Property class to core module
- 移动了一些类，优化目录结构 / Moved classes for better package structure
- 优化掉落效果 / Improved drop effects

### 修复 / Fixed
- 修复了注入管道时可能的线程不安全问题 / Fixed potential thread-safety issue in pipeline injection
- 修复了活塞打破下方方块事件时的错误 / Fixed piston breaking block below event error
- 修复了碰撞箱判定，考虑所有实体 / Fixed collision box detection to include all entities
- 修复和完善了命令建议 / Fixed and improved command suggestions
- 修复了一些日志记录的小错误 / Fixed minor logging errors
- 修复定时保存 / Fixed scheduled saving

---

## [0.4.0 - alpha] - 2025-06-03
### 新增 / Added
- 自定义方块事件系统初探，增加自定义方块的放置破坏和失去支撑掉落的事件 / Initial custom block event system with place, break and support-loss events
- 增加方块GUI抽象类和GUICreator接口，增加方块到GUI的绑定 / Added block GUI abstract class and GUICreator interface for block-GUI binding
- 加入了ThinBlock，类似压力板形状的方块，默认映射金制和银质压力板 / Added ThinBlock (pressure plate-like) with gold/silver plate mappings
- 为有碰撞箱块的放置增加碰撞箱检测 / Added collision detection for block placement
- 增加注解驱动的定时注册API / Added annotation-driven scheduled registration API
- 增加自定义方块放置和破坏音效的设置 / Added custom block place/break sound settings
- 新增ArtisanBlockType枚举 / Added ArtisanBlockType enum
- 增加熔炉配方系统 / Added furnace recipe system

### 变更 / Changed
- 重构了自定义方块体系，增加一些基类，抽离Crop实现，利于拓展 / Refactored block system with base classes and decoupled crop implementation
- 自定义方块改为有限适用，分为不同种类的方块，不再尝试全部匹配 / Limited block applicability to specific types instead of universal matching
- 规范初始化顺序，调整目录结构和包结构 / Standardized initialization order and package structure
- 事件系统内部逻辑重构，分离共同逻辑 / Refactored event system with shared logic separation
- 完善了JavaDoc / Improved JavaDoc documentation
- 重构自定义配方系统，更规范，风格与其他更统一 / Refactored recipe system for consistency
- 优化固定字段的反射性能，使用bukkit自带的读取BlockState方法 / Optimized reflection using Bukkit's built-in BlockState methods

---

## [0.3.0 - alpha] - 2025-05-20
### 新增 / Added
- 属性系统增加玩家绑定属性的支持和API / Added player-bound attributes to attribute system
- 内部代码初始化增加注解驱动 / Added annotation-driven initialization
- 增加debugMode / Added debug mode
- 增加对自定义装备对原版装备槽位的继承 / Added vanilla equipment slot inheritance for custom gear
- 对自定义方块附加了PDC / Added PDC support for custom blocks

### 变更 / Changed
- 分离全局属性和物品堆属性的注册 / Separated global and itemstack attribute registration
- 删除读取yml的相关功能，专注核心模块和API设计 / Removed YAML reading to focus on core API
- 规范重命名和移动部分模块 / Standardized naming and module organization
- 创建物品使用新版的数据组件API，修复了一些ItemMeta导致的问题 / Used new data component API for item creation, fixed ItemMeta issues

### 修复 / Fixed
- 更健壮的错误处理 / More robust error handling

---

## [0.2.0 - alpha] - 2025-05-12
### 新增 / Added
- 加入作物系统及持久化 / Added crop system with persistence

### 变更 / Changed
- 调整包结构，符合封装原则 / Adjusted package structure for encapsulation
- 重写配方匹配，改用Bukkit提供的NamespacedKey作为索引键 / Rewrote recipe matching using Bukkit's NamespacedKey
- 调整部分API命名和结构 / Adjusted API naming and structure

---

## [0.1.0 - alpha] - 2025-05-09
### 新增 / Added
- 合成系统、物品系统和属性系统初步完成 / Initial implementation of crafting, item and attribute systems
- 提供独立的基本API框架 / Provided standalone basic API framework