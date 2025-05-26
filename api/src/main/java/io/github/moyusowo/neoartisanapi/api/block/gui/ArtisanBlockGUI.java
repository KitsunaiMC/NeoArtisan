package io.github.moyusowo.neoartisanapi.api.block.gui;

import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * 自定义方块GUI的抽象基类，实现与方块位置绑定的交互界面。
 * <p>
 * <b>特性：</b>
 * <ul>
 *   <li>自动注册所需事件监听器</li>
 *   <li>支持多种库存类型和标题格式</li>
 *   <li>与方块位置严格绑定</li>
 * </ul>
 *
 * <b>继承要求：</b>
 * <ol>
 *   <li>必须实现 {@link #setItemInInventory()} 设置初始物品</li>
 *   <li>可通过重写和增加事件方法扩展交互逻辑</li>
 * </ol>
 *
 * @see BlockInventoryHolder Bukkit库存持有者接口
 * @see Listener 事件监听标记
 * @since 1.0.0
 */
@SuppressWarnings("unused")
public abstract class ArtisanBlockGUI implements BlockInventoryHolder, Listener {

    protected final Inventory inventory;
    protected final Location location;

    /**
     * 构造器（指定大小和MiniMessage标题）
     * @param plugin 宿主插件实例（用于事件注册）
     * @param size 库存大小（必须是9的倍数，1-54之间）
     * @param title 使用MiniMessage格式的库存标题
     * @param location 绑定的方块位置
     */
    protected ArtisanBlockGUI(Plugin plugin, int size, Component title, Location location) {
        this.location = location;
        inventory = plugin.getServer().createInventory(this, size, title);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        setItemInInventory();
    }

    /**
     * 构造器（指定库存类型和MiniMessage标题）
     * @param plugin 宿主插件实例
     * @param inventoryType 原版库存类型（如CHEST、HOPPER等）
     * @param title 使用MiniMessage格式的库存标题
     * @param location 绑定的方块位置
     */
    protected ArtisanBlockGUI(Plugin plugin, InventoryType inventoryType, Component title, Location location) {
        this.location = location;
        inventory = plugin.getServer().createInventory(this, inventoryType, title);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        setItemInInventory();
    }

    /**
     * 构造器（指定大小和字符串标题，但支持MiniMessage格式）
     * @param plugin 宿主插件实例
     * @param size 库存大小
     * @param title 传统字符串标题（自动转换为MiniMessage）
     * @param location 绑定的方块位置
     */
    protected ArtisanBlockGUI(Plugin plugin, int size, String title, Location location) {
        this(plugin, size, MiniMessage.miniMessage().deserialize(title), location);
    }

    /**
     * 构造器（指定大小和字符串标题，但支持MiniMessage格式）
     * @param plugin 宿主插件实例
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
     *
     * @return 该位置的自定义方块数据
     */
    public ArtisanBlockData getArtisanBlockData() {
        return NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlock(this.location.getBlock());
    }

    /**
     * 抽象方法：初始化库存物品
     * <p>
     * 子类必须实现此方法，在构造时设置库存的初始物品布局。
     * </p>
     */
    protected abstract void setItemInInventory();

    /**
     * 库存点击事件处理（默认取消所有操作）
     */
    @EventHandler
    protected void onClick(InventoryClickEvent event) {
        if (!(event.getClickedInventory().getHolder(false) instanceof ArtisanBlockGUI)) return;
        event.setCancelled(true);
    }

    /**
     * 方块交互事件处理（默认打开GUI）
     */
    @EventHandler(priority = EventPriority.LOWEST)
    protected void onInteract(PlayerInteractEvent event) {
        if (!event.getClickedBlock().getLocation().equals(this.location)) return;
        event.setCancelled(true);
        event.getPlayer().openInventory(this.inventory);
    }

}
