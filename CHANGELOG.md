# 更新日志 Change log

> 本文档遵循 [Keep a Changelog](https://keepachangelog.com/) 规范
> This document follows the [Keep a Changelog](https://keepachangelog.com/) specification

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
    <span>开发阶段警告 / Development Phase Warning</span>
  </div>

  <p style="margin: 0.5rem 0">
    <strong>当前版本：0.x.y（开发预览版） / Current Version: 0.x.y (Development Preview)</strong>
  </p>

  <ul style="
      margin: 0.5rem 0;
      padding-left: 1.5rem;
  ">
    <li>🚨 <strong>无兼容性承诺</strong>：不遵循语义化版本控制规范 / <strong>No Compatibility Guarantee</strong>: Does not follow semantic versioning specifications</li>
    <li>⚡ <strong>变更风险</strong>：任何更新都可能包含API破坏性变更 / <strong>Change Risk</strong>: Any update may contain breaking API changes</li>
    <li>💥 <strong>版本升级规则</strong>：
      <ul style="padding-left: 1.5rem; margin: 0.25rem 0">
        <li>修订号更新（0.x.<strong>y</strong>）：<em>可能</em>包含破坏性变更 / Patch updates (0.x.<strong>y</strong>): <em>May</em> contain breaking changes</li>
        <li>次版本更新（0.<strong>x</strong>.y）：<em>必定</em>包含破坏性变更 / Minor updates (0.<strong>x</strong>.y): <em>Will always</em> contain breaking changes</li>
      </ul>
    </li>
  </ul>

  <p style="
      margin: 0.75rem 0 0.25rem;
      font-style: italic;
  ">
    <strong>稳定版计划</strong>：1.0.0版本发布时开始遵循语义化版本控制（时间待定）<br>
    <strong>Stable Release Plan</strong>: Semantic versioning compliance begins at version 1.0.0 (date TBD)
  </p>
</div>

## [0.8.0 and 0.7.0] - 2025-07-15
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

## [0.6.0] - 2025-07-06
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

## [0.5.0] - 2025-06-23
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

## [0.4.0] - 2025-06-03
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

## [0.3.0] - 2025-05-20
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

## [0.2.0] - 2025-05-12
### 新增 / Added
- 加入作物系统及持久化 / Added crop system with persistence

### 变更 / Changed
- 调整包结构，符合封装原则 / Adjusted package structure for encapsulation
- 重写配方匹配，改用Bukkit提供的NamespacedKey作为索引键 / Rewrote recipe matching using Bukkit's NamespacedKey
- 调整部分API命名和结构 / Adjusted API naming and structure

---

## [0.1.0] - 2025-05-09
### 新增 / Added
- 合成系统、物品系统和属性系统初步完成 / Initial implementation of crafting, item and attribute systems
- 提供独立的基本API框架 / Provided standalone basic API framework