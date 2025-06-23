package io.github.moyusowo.neoartisanapi.api.block.gui;

import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义方块GUI的抽象基类，实现与方块位置绑定的交互界面。
 * <p>
 * <b>核心特性：</b>
 * <ul>
 *   <li><b>自动事件管理</b> - 自动注册所需事件监听器</li>
 *   <li><b>生命周期控制</b> - 提供初始化(init)和终止(terminate)钩子</li>
 *   <li><b>延迟初始化</b> - 确保方块数据加载完成后再初始化</li>
 *   <li><b>任务调度</b> - 支持注册GUI生命周期任务</li>
 *   <li><b>位置绑定</b> - 与特定方块位置严格关联</li>
 * </ul>
 *
 * <b>继承要求：</b>
 * <ol>
 *   <li>必须实现 {@link #init()} 方法进行初始化</li>
 *   <li>可通过重写 {@link #terminate()} 实现清理逻辑</li>
 *   <li>在构造函数中<b>禁止直接调用</b> {@link #getArtisanBlockData()}，必须通过重写 {@link #init()} 方法以使用内置的延迟初始化机制</li>
 *   <li>如需与GUI生命周期相关的周期性任务，使用 {@link #addLifecycleTask(GUILifecycleTask)} 注册</li>
 * </ol>
 *
 * <p><b>生命周期流程图：</b></p>
 * <pre>
 * 构造实例 → 等待数据加载 → init() → 运行生命周期任务 → [运行中]
 *      ↓
 * 方块销毁 → onTerminate() → terminate() → 注销监听器 → 取消所有任务
 * </pre>
 *
 * @see BlockInventoryHolder Bukkit库存持有者接口
 * @see Listener 事件监听标记
 * @see GUILifecycleTask
 * @since 2.0.0
 */
public abstract class ArtisanBlockGUI implements BlockInventoryHolder, Listener {

    protected final Inventory inventory;
    protected final Location location;
    protected final Plugin plugin;
    private final List<GUILifecycleTask> lifecycleTasks;
    private boolean isInit = false;

    /**
     * 基础构造器（不应该在 {@link GUICreator#create(Location)} 之外的地方调用）
     *
     * @param plugin 插件实例（非null）
     * @param size 库存大小（9的倍数）
     * @param title 标题
     * @param location 框架提供的方块位置（非null）
     *
     * @implNote 构造流程：
     * 1. 附属插件在GUICreator中提供plugin/size/title
     * 2. 框架在方块放置时提供location
     * 3. 构造器组合这些参数初始化GUI
     */
    protected ArtisanBlockGUI(Plugin plugin, int size, Component title, Location location) {
        this.location = location;
        this.plugin = plugin;
        inventory = plugin.getServer().createInventory(this, size, title);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(location.getBlock())) {
                    onInit();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
        this.lifecycleTasks = new ArrayList<>();
    }

    /**
     * 基础构造器（库存类型版本）
     *
     * @param plugin 插件实例
     * @param inventoryType 原版库存类型
     * @param title 标题
     * @param location 框架提供的方块位置
     */
    protected ArtisanBlockGUI(Plugin plugin, InventoryType inventoryType, Component title, Location location) {
        this.location = location;
        this.plugin = plugin;
        inventory = plugin.getServer().createInventory(this, inventoryType, title);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(location.getBlock())) {
                    onInit();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 0L, 1L);
        this.lifecycleTasks = new ArrayList<>();
    }

    /**
     * 构造器（指定大小和字符串标题，但支持MiniMessage格式）
     * @param plugin 插件实例
     * @param size 库存大小
     * @param title 传统字符串标题（自动转换为MiniMessage）
     * @param location 框架提供的方块位置
     */
    protected ArtisanBlockGUI(Plugin plugin, int size, String title, Location location) {
        this(plugin, size, MiniMessage.miniMessage().deserialize(title), location);
    }

    /**
     * 构造器（指定大小和字符串标题，但支持MiniMessage格式）
     * @param plugin 插件实例
     * @param inventoryType 原版库存类型
     * @param title 传统字符串标题（自动转换为MiniMessage）
     * @param location 绑定的方块位置
     */
    protected ArtisanBlockGUI(Plugin plugin, InventoryType inventoryType, String title, Location location) {
        this(plugin, inventoryType, MiniMessage.miniMessage().deserialize(title), location);
    }

    /**
     * 获取绑定的库存实例
     * @return 与此GUI关联的库存
     */
    @Override
    @NotNull
    public Inventory getInventory() {
        return this.inventory;
    }

    /**
     * 获取绑定的方块实例
     * @return 库存关联的物理方块
     */
    @NotNull
    @Override
    public Block getBlock() {
        return this.location.getBlock();
    }

    /**
     * 获取关联的自定义方块数据
     * <p>
     * <b>安全访问时机：</b>
     * <ul>
     *   <li>{@link #init()} 方法及其调用链</li>
     *   <li>事件处理方法（如 {@link #onClick(InventoryClickEvent)}）</li>
     *   <li>生命周期任务执行时</li>
     * </ul>
     *
     * @return 该位置的自定义方块数据（不会为null）
     * @throws IllegalStateException 如果方块数据尚未加载
     */
    public @NotNull ArtisanBlockData getArtisanBlockData() {
        ArtisanBlockData artisanBlockData = NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlockData(this.location.getBlock());
        if (artisanBlockData == null) {
            throw new IllegalStateException("ArtisanBlockData not yet loaded!");
        }
        return artisanBlockData;
    }

    /**
     * 添加GUI生命周期任务
     * <p>
     * 用于注册需要在GUI初始化后运行的周期性任务（如库存刷新）。
     * 必须在 {@link #init()} 方法执行前调用，可在构造函数或 {@link #init()} 方法中注册。
     * </p>
     *
     * @param lifecycleTask 要添加的任务（非null）
     * @throws IllegalStateException 如果初始化已完成
     * @see GUILifecycleTask 生命周期任务接口
     */
    @ApiStatus.Internal
    protected void addLifecycleTask(@NotNull GUILifecycleTask lifecycleTask) {
        if (isInit) throw new IllegalStateException("Cannot add tasks after initialization");
        lifecycleTasks.add(lifecycleTask);
    }

    /**
     * 添加GUI生命周期任务
     * <p>
     * 用于注册需要在GUI初始化后运行的周期性任务（如库存刷新）。
     * 必须在 {@link #init()} 方法执行前调用，可在构造函数或 {@link #init()} 方法中注册。
     * </p>
     *
     * @param bukkitRunnable 要添加的任务（非null）
     * @param delay 任务开始时的延迟（Tick为单位）
     * @param period 任务执行的周期（Tick为单位）
     * @throws IllegalStateException 如果初始化已完成
     */
    protected void addLifecycleTask(@NotNull BukkitRunnable bukkitRunnable, long delay, long period) {
        addLifecycleTask(new GUILifecycleTask(plugin, bukkitRunnable, delay, period));
    }

    /**
     * 添加GUI生命周期任务
     * <p>
     * 用于注册需要在GUI初始化后运行的周期性任务（如库存刷新）。
     * 必须在 {@link #init()} 方法执行前调用，可在构造函数或 {@link #init()} 方法中注册。
     * </p>
     *
     * @param bukkitRunnable 要添加的任务（非null）
     * @param delay 任务开始时的延迟（Tick为单位）
     * @param period 任务执行的周期（Tick为单位）
     * @param isAsynchronous 是否异步执行
     * @throws IllegalStateException 如果初始化已完成
     */
    protected void addLifecycleTask(@NotNull BukkitRunnable bukkitRunnable, long delay, long period, boolean isAsynchronous) {
        addLifecycleTask(new GUILifecycleTask(plugin, bukkitRunnable, delay, period, isAsynchronous));
    }

    /**
     * 执行GUI初始化流程（框架内部调用）
     * <p>
     * 此方法在确认关联方块数据已加载后自动触发，执行顺序：
     * <ol>
     *   <li>调用子类实现的 {@link #init()} 方法</li>
     *   <li>启动所有注册的生命周期任务</li>
     *   <li>标记初始化完成状态</li>
     * </ol>
     *
     * <p><b>重要：</b>此方法由框架自动调用，开发者不应手动触发。</p>
     *
     * @implNote 保证在主线程同步执行
     * @see #init() 抽象初始化方法
     * @since 2.0.0
     */
    private void onInit() {
        try {
            init();
        } finally {
            lifecycleTasks.forEach(GUILifecycleTask::run);
            isInit = true;
        }
    }

    /**
     * 执行GUI终止流程（框架内部调用）
     * <p>
     * 当关联方块被破坏或区块卸载时，框架自动调用此方法进行资源清理，执行顺序：
     * <ol>
     *   <li>调用子类实现的 {@link #terminate()} 清理逻辑（异常安全）</li>
     *   <li>强制关闭所有查看此GUI的玩家库存</li>
     *   <li>注销所有事件监听器</li>
     *   <li>取消所有生命周期任务</li>
     * </ol>
     *
     * <p><b>资源清理保证：</b></p>
     * <ul>
     *   <li><b>玩家库存安全</b> - 自动关闭所有关联的玩家界面</li>
     *   <li><b>异常隔离</b> - terminate() 异常不影响后续清理</li>
     *   <li><b>事件安全</b> - 确保后续不会触发无效事件</li>
     *   <li><b>任务清理</b> - 所有周期任务立即终止</li>
     * </ul>
     *
     * <p><b>执行示例：</b></p>
     * <pre>
     * // 方块被破坏时
     * artisanBlockStorage.remove(location);
     * → 触发 onTerminate()
     *   → 关闭玩家库存
     *   → 清理任务
     *   → 注销事件
     * </pre>
     *
     * @apiNote 此方法仅由NeoArtisan框架调用
     * @implSpec 实现保证线程安全（主线程执行）
     * @since 2.0.0
     */
    @ApiStatus.Internal
    public final void onTerminate() {
        try {
            terminate();
        } finally {
            Bukkit.getOnlinePlayers().stream()
                    .filter(p -> p.getOpenInventory().getTopInventory().getHolder() == this)
                    .forEach(player -> {
                        player.closeInventory(InventoryCloseEvent.Reason.PLUGIN);
                    });
            HandlerList.unregisterAll(this);
            lifecycleTasks.forEach(GUILifecycleTask::cancel);
        }
    }

    /**
     * 抽象初始化方法（子类必须实现）
     * <p>
     * 当GUI确认关联方块数据已加载后，框架会自动调用此方法。
     * 此方法中可安全执行以下操作：
     * <ul>
     *   <li>访问 {@link #getArtisanBlockData()} 获取方块数据</li>
     *   <li>初始化库存物品布局</li>
     *   <li>配置GUI初始状态</li>
     *   <li>注册生命周期事件/li>
     * </ul>
     *
     * <p><b>最佳实践：</b></p>
     * <pre>
     * protected void init() {
     *   ArtisanBlockData data = getArtisanBlockData();
     *   getInventory().setItem(0, createDisplayItem(data));
     *   // 注册自定义处理
     *   registerCustomTask();
     * }
     * </pre>
     *
     * @throws IllegalStateException 如果方块数据不可用
     * @implSpec 避免执行耗时操作
     */
    protected abstract void init();

    /**
     * 终止清理方法（子类可选实现）
     * <p>
     * 当GUI关联的方块被破坏时调用，用于执行资源清理操作。
     * </p>
     *
     *
     * @since 2.0.0
     */
    protected void terminate() {}

    /**
     * 库存点击事件处理（默认取消所有操作）
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (!(event.getClickedInventory().getHolder(false) instanceof ArtisanBlockGUI)) return;
        event.setCancelled(true);
    }

    /**
     * 库存点击事件处理（默认取消所有操作）
     */
    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof ArtisanBlockGUI)) return;
        event.setCancelled(true);
    }

    /**
     * 方块交互事件处理（默认打开GUI）
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onInteract(PlayerInteractEvent event) {
        if (event.useInteractedBlock() == Event.Result.DENY) return;
        if (event.useItemInHand() == Event.Result.DENY) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        if (!event.getClickedBlock().getLocation().equals(this.location)) return;
        event.setCancelled(true);
        event.getPlayer().openInventory(this.inventory);
    }

}
