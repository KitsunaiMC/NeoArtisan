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

@SuppressWarnings("unused")
public abstract class ArtisanBlockGUI implements BlockInventoryHolder, Listener {

    protected final Inventory inventory;
    protected final Location location;

    protected ArtisanBlockGUI(Plugin plugin, int size, Component title, Location location) {
        this.location = location;
        inventory = plugin.getServer().createInventory(this, size, title);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        setItemInInventory();
    }

    protected ArtisanBlockGUI(Plugin plugin, InventoryType inventoryType, Component title, Location location) {
        this.location = location;
        inventory = plugin.getServer().createInventory(this, inventoryType, title);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        setItemInInventory();
    }

    protected ArtisanBlockGUI(Plugin plugin, int size, String title, Location location) {
        this(plugin, size, MiniMessage.miniMessage().deserialize(title), location);
    }

    protected ArtisanBlockGUI(Plugin plugin, InventoryType inventoryType, String title, Location location) {
        this(plugin, inventoryType, MiniMessage.miniMessage().deserialize(title), location);
    }

    @Override
    @NotNull
    public Inventory getInventory() {
        return this.inventory;
    }

    @NotNull
    @Override
    public Block getBlock() {
        return this.location.getBlock();
    }

    public ArtisanBlockData getArtisanBlockData() {
        return NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlock(this.location.getBlock());
    }

    protected abstract void setItemInInventory();

    @EventHandler
    protected void onClick(InventoryClickEvent event) {
        if (!(event.getClickedInventory().getHolder(false) instanceof ArtisanBlockGUI)) return;
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    protected void onInteract(PlayerInteractEvent event) {
        if (!event.getClickedBlock().getLocation().equals(this.location)) return;
        event.setCancelled(true);
        event.getPlayer().openInventory(this.inventory);
    }

}
