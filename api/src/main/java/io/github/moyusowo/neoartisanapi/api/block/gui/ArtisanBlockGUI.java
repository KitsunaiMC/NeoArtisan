package io.github.moyusowo.neoartisanapi.api.block.gui;

import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.protection.Protections;
import io.github.moyusowo.neoartisanapi.api.block.storage.Storages;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.*;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * Abstract base class for custom block GUIs, implementing an interface bound to a block location.
 * <p>
 * <b>Core features:</b>
 * <ul>
 *   <li><b>Automatic event management</b> - Automatically registers required event listeners</li>
 *   <li><b>Lifecycle control</b> - Provides initialization (init) and termination (terminate) hooks</li>
 *   <li><b>Delayed initialization</b> - Ensures block data is loaded before initialization</li>
 *   <li><b>Task scheduling</b> - Binds to block task scheduler</li>
 *   <li><b>Location binding</b> - Strictly associated with a specific block location</li>
 * </ul>
 *
 * <b>Inheritance requirements:</b>
 * <ol>
 *   <li>Must implement {@link #init()} method for initialization</li>
 *   <li>Can override {@link #terminate()} to implement cleanup logic</li>
 *   <li><b>Do not call</b> {@link #getArtisanBlockData()} directly in constructor</li>
 * </ol>
 *
 * @see BlockInventoryHolder
 * @see Listener
 */
public abstract class ArtisanBlockGUI implements BlockInventoryHolder, Listener {

    protected final Inventory inventory;
    protected final Location location;

    /**
     * Basic constructor (should not be called outside {@link GUICreator#create(Location)})
     *
     * @param plugin plugin instance (non-null)
     * @param size inventory size (multiple of 9)
     * @param title title
     * @param location framework-provided block location (non-null)
     */
    protected ArtisanBlockGUI(Plugin plugin, int size, Component title, Location location) {
        this.location = location;
        inventory = plugin.getServer().createInventory(this, size, title);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Basic constructor (inventory type version)
     *
     * @param plugin plugin instance
     * @param inventoryType vanilla inventory type
     * @param title title
     * @param location framework-provided block location
     */
    protected ArtisanBlockGUI(Plugin plugin, InventoryType inventoryType, Component title, Location location) {
        this.location = location;
        inventory = plugin.getServer().createInventory(this, inventoryType, title);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Constructor (with size and string title, supports MiniMessage format)
     *
     * @param plugin plugin instance
     * @param size inventory size
     * @param title traditional string title (automatically converted to MiniMessage)
     * @param location framework-provided block location
     */
    protected ArtisanBlockGUI(Plugin plugin, int size, String title, Location location) {
        this(plugin, size, MiniMessage.miniMessage().deserialize(title), location);
    }

    /**
     * Constructor (with inventory type and string title, supports MiniMessage format)
     *
     * @param plugin plugin instance
     * @param inventoryType vanilla inventory type
     * @param title traditional string title (automatically converted to MiniMessage)
     * @param location bound block location
     */
    protected ArtisanBlockGUI(Plugin plugin, InventoryType inventoryType, String title, Location location) {
        this(plugin, inventoryType, MiniMessage.miniMessage().deserialize(title), location);
    }

    /**
     * Gets the bound inventory instance
     * @return the inventory associated with this GUI
     */
    @Override
    @NotNull
    public Inventory getInventory() {
        return this.inventory;
    }

    /**
     * Gets the bound block instance
     * @return the physical block associated with the inventory
     */
    @NotNull
    @Override
    public Block getBlock() {
        return this.location.getBlock();
    }

    /**
     * Gets the associated custom block data
     * <p>
     * <b>Safe access times:</b>
     * <ul>
     *   <li>{@link #init()} method and its call chain</li>
     *   <li>Event handling methods (like {@link #onClick(InventoryClickEvent)})</li>
     *   <li>Lifecycle task execution</li>
     * </ul>
     *
     * @return the custom block data at this location (never null)
     * @throws NullPointerException if block data is not yet loaded
     */
    public @NotNull ArtisanBlockData getArtisanBlockData() {
        return Storages.BLOCK.getArtisanBlockData(this.location.getBlock());
    }

    /**
     * Add GUI lifecycle task
     *
     * <p>
     * Used to register periodic tasks that need to run after GUI initialization.
     * Must be called before {@link #init()} method executes.
     * </p>
     *
     * @see ArtisanBlockData#getLifecycleTaskManager()
     */
    protected void addLifecycleTask(@NotNull Runnable runnable, long delay, long period, boolean isAsynchronous, boolean runInChunkNotLoaded) {
        getArtisanBlockData().getLifecycleTaskManager().addLifecycleTask(runnable, delay, period, isAsynchronous, runInChunkNotLoaded);
    }

    /**
     * Execute GUI initialization process (called internally by framework)
     * <p>
     * This method is automatically triggered after confirming the associated block data is loaded.
     * Execution order:
     * <ol>
     *   <li>Call subclass implementation of {@link #init()} method</li>
     *   <li>Start all registered lifecycle tasks</li>
     *   <li>Mark initialization as complete</li>
     * </ol>
     *
     * <p><b>Important:</b>This method is automatically called by the framework, developers should not trigger it manually.</p>
     *
     * @see #init() abstract initialization method
     */
    public final void onInit() {
        try {
            init();
        } finally {
            getArtisanBlockData().getLifecycleTaskManager().addTerminateRunnable(this::onTerminate);
        }
    }

    /**
     * Execute GUI termination process (called internally by framework)
     * <p>
     * When the associated block is destroyed or chunk is unloaded, the framework automatically
     * calls this method for resource cleanup.
     * </p>
     */
    private void onTerminate() {
        try {
            terminate();
        } finally {
            Bukkit.getOnlinePlayers().stream()
                    .filter(p -> p.getOpenInventory().getTopInventory().getHolder() == this)
                    .forEach(player -> player.closeInventory(InventoryCloseEvent.Reason.PLUGIN));
            HandlerList.unregisterAll(this);
        }
    }

    /**
     * Abstract initialization method (subclass must implement)
     * <p>
     * Called automatically by the framework after confirming associated block data is loaded.
     * In this method you can safely:
     * <ul>
     *   <li>Access {@link #getArtisanBlockData()} to get block data</li>
     *   <li>Initialize inventory item layout</li>
     *   <li>Configure initial GUI state</li>
     *   <li>Register lifecycle events</li>
     * </ul>
     *
     * @throws IllegalStateException if block data is unavailable
     */
    protected abstract void init();

    /**
     * Termination cleanup method (subclass can optionally implement)
     * <p>
     * Called when the GUI-associated block is destroyed, for executing resource cleanup operations.
     * </p>
     */
    protected void terminate() {}

    /**
     * Inventory click event handler (cancels all operations by default)
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (!(event.getClickedInventory().getHolder(false) instanceof ArtisanBlockGUI)) return;
        event.setCancelled(true);
    }

    /**
     * Inventory drag event handler (cancels all operations by default)
     */
    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (!(event.getInventory().getHolder(false) instanceof ArtisanBlockGUI)) return;
        event.setCancelled(true);
    }

    /**
     * Handle block GUI opening
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public final void onInteract(PlayerInteractEvent event) {
        if (event.useInteractedBlock() == Event.Result.DENY) return;
        if (event.useItemInHand() == Event.Result.DENY) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (!event.getClickedBlock().getLocation().equals(this.location)) return;
        if (!Protections.BLOCK.canInteract(event.getPlayer(), event.getClickedBlock().getLocation())) return;
        event.setCancelled(true);
        InventoryView inventoryView = Objects.requireNonNull(event.getPlayer().openInventory(this.inventory));
        InventoryOpenEvent inventoryOpenEvent = new InventoryOpenEvent(inventoryView);
        inventoryOpenEvent.callEvent();
        if (inventoryOpenEvent.titleOverride() != null) throw new IllegalArgumentException("Can not change title of ArtisanBlockGUI!");
        if (inventoryOpenEvent.isCancelled()) inventoryView.close();
    }

}
