package io.github.moyusowo.neoartisan.block.state.listener;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.attribute.Attributes;
import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerUpdateAttributes;
import com.sun.jdi.InternalException;
import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.state.ArtisanCommonState;
import io.github.moyusowo.neoartisanapi.api.block.state.ArtisanLeavesState;
import io.github.moyusowo.neoartisanapi.api.block.state.ArtisanSkullState;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageAbortEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BlockBreakSynchronize implements Listener {
    private BlockBreakSynchronize() {}

    public static final NamespacedKey TEMPLATE_HIDDEN_MULTIPLY = new NamespacedKey(NeoArtisan.instance(), "template_multiply");
    private static final NamespacedKey TEMPLATE_MULTIPLY = new NamespacedKey(NeoArtisan.instance(), "template_packet_multiply");

    @InitMethod(priority = InitPriority.LISTENER)
    public static void init() {
        NeoArtisan.registerListener(new BlockBreakSynchronize());
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBreakStart(BlockDamageEvent event) {
        if (event.getInstaBreak()) return;
        if (!NeoArtisanAPI.getArtisanBlockStorage().isArtisanBlock(event.getBlock())) return;
        ArtisanBlockData blockData = NeoArtisanAPI.getArtisanBlockStorage().getArtisanBlockData(event.getBlock());
        switch (blockData.getArtisanBlockState().getType()) {
            case COMMON -> onCommonStateBreakStart(event, blockData);
            case LEAVES -> onLeavesStateBreakStart(event, blockData);
            default -> {}
        }
    }

    @NotNull
    private static WrapperPlayServerUpdateAttributes.PropertyModifier.Operation getOperation(AttributeModifier attributeModifier) {
        final WrapperPlayServerUpdateAttributes.PropertyModifier.Operation operation;
        switch (attributeModifier.getOperation()) {
            case ADD_NUMBER -> operation = WrapperPlayServerUpdateAttributes.PropertyModifier.Operation.ADDITION;
            case ADD_SCALAR -> operation = WrapperPlayServerUpdateAttributes.PropertyModifier.Operation.MULTIPLY_BASE;
            case MULTIPLY_SCALAR_1 -> operation = WrapperPlayServerUpdateAttributes.PropertyModifier.Operation.MULTIPLY_TOTAL;
            default -> throw new InternalException("Why the hell other situation?????");
        }
        return operation;
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBreakAbort(BlockDamageAbortEvent event) {
        final Player player = event.getPlayer();
        final AttributeInstance attributeInstance = player.getAttribute(Attribute.BLOCK_BREAK_SPEED);
        if (attributeInstance == null) throw new InternalException("why the fuck is null????? IT SHOULD NOT BE!!!!");
        attributeInstance.removeModifier(TEMPLATE_HIDDEN_MULTIPLY);
        attributeInstance.removeModifier(TEMPLATE_MULTIPLY);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final AttributeInstance attributeInstance = player.getAttribute(Attribute.BLOCK_BREAK_SPEED);
        if (attributeInstance == null) throw new InternalException("why the fuck is null????? IT SHOULD NOT BE!!!!");
        attributeInstance.removeModifier(TEMPLATE_HIDDEN_MULTIPLY);
        attributeInstance.removeModifier(TEMPLATE_MULTIPLY);
    }

    private void onLeavesStateBreakStart(BlockDamageEvent event, ArtisanBlockData blockData) {
        final Player player = event.getPlayer();
        final AttributeInstance attributeInstance = player.getAttribute(Attribute.BLOCK_BREAK_SPEED);
        if (attributeInstance == null) throw new InternalException("why the fuck is null????? IT SHOULD NOT BE!!!!");
        final float realHardness = event.getBlock().getType().getHardness();
        final float hardness = ((ArtisanLeavesState) blockData.getArtisanBlockState()).getHardness();
        double tool = 1;
        switch (event.getItemInHand().getType()) {
            case WOODEN_SWORD, WOODEN_HOE -> tool /= 2;
            case STONE_SWORD, STONE_HOE -> tool /= 4;
            case IRON_SWORD, IRON_HOE -> tool /= 6;
            case DIAMOND_SWORD, DIAMOND_HOE -> tool /= 8;
            case NETHERITE_SWORD, NETHERITE_HOE -> tool /= 9;
            case GOLDEN_SWORD, GOLDEN_HOE -> tool /= 12;
        }
        attributeInstance.addTransientModifier(
                new AttributeModifier(
                        TEMPLATE_MULTIPLY,
                        (realHardness / hardness * tool) - 1,
                        AttributeModifier.Operation.MULTIPLY_SCALAR_1
                )
        );
    }

    private void onCommonStateBreakStart(BlockDamageEvent event, ArtisanBlockData blockData) {
        final Player player = event.getPlayer();
        final AttributeInstance attributeInstance = player.getAttribute(Attribute.BLOCK_BREAK_SPEED);
        if (attributeInstance == null) throw new InternalException("why the fuck is null????? IT SHOULD NOT BE!!!!");
        final float serverHardness = event.getBlock().getType().getHardness();
        final float clientHardness = Bukkit.createBlockData(WrappedBlockState.getByGlobalId(blockData.getArtisanBlockState().appearanceState()).toString()).getMaterial().getHardness();
        final float hardness = ((ArtisanCommonState) blockData.getArtisanBlockState()).getHardness();
        double serverTool = 1, clientTool = 1;
        if (!event.getItemInHand().getType().name().endsWith("PICKAXE")) {
            serverTool = serverTool * 10 / 3;
        } else {
            switch (event.getItemInHand().getType()) {
                case WOODEN_PICKAXE -> serverTool /= 2;
                case STONE_PICKAXE -> serverTool /= 4;
                case IRON_PICKAXE -> serverTool /= 6;
                case DIAMOND_PICKAXE -> serverTool /= 8;
                case NETHERITE_PICKAXE -> serverTool /= 9;
                case GOLDEN_PICKAXE -> serverTool /= 12;
            }
        }
        if (event.getItemInHand().getType().name().endsWith("AXE")) {
            switch (event.getItemInHand().getType()) {
                case WOODEN_AXE -> clientTool /= 2;
                case STONE_AXE -> clientTool /= 4;
                case IRON_AXE -> clientTool /= 6;
                case DIAMOND_AXE -> clientTool /= 8;
                case NETHERITE_AXE -> clientTool /= 9;
                case GOLDEN_AXE -> clientTool /= 12;
            }
        }
        List<WrapperPlayServerUpdateAttributes.PropertyModifier> properties = new ArrayList<>();
        for (AttributeModifier attributeModifier : attributeInstance.getModifiers()) {
            final WrapperPlayServerUpdateAttributes.PropertyModifier.Operation operation = getOperation(attributeModifier);
            properties.add(
                    new WrapperPlayServerUpdateAttributes.PropertyModifier(
                            new ResourceLocation(attributeModifier.getKey()),
                            attributeModifier.getAmount(),
                            operation
                    )
            );
        }
        properties.add(
                new WrapperPlayServerUpdateAttributes.PropertyModifier(
                        new ResourceLocation(TEMPLATE_MULTIPLY.getKey()),
                        (clientHardness / hardness * clientTool) - 1,
                        WrapperPlayServerUpdateAttributes.PropertyModifier.Operation.MULTIPLY_TOTAL
                )
        );
        final WrapperPlayServerUpdateAttributes wrapperPlayServerUpdateAttributes = new WrapperPlayServerUpdateAttributes(
                player.getEntityId(),
                List.of(
                        new WrapperPlayServerUpdateAttributes.Property(
                                Attributes.BLOCK_BREAK_SPEED,
                                attributeInstance.getBaseValue(),
                                properties
                        )
                )
        );
        attributeInstance.addTransientModifier(
                new AttributeModifier(
                        TEMPLATE_HIDDEN_MULTIPLY,
                        (serverHardness / hardness * serverTool) - 1,
                        AttributeModifier.Operation.MULTIPLY_SCALAR_1
                )
        );
        PacketEvents.getAPI().getPlayerManager().sendPacket(player, wrapperPlayServerUpdateAttributes);
    }

    private void onSkullStateBreakStart(BlockDamageEvent event, ArtisanBlockData blockData) {
        final Player player = event.getPlayer();
        final AttributeInstance attributeInstance = player.getAttribute(Attribute.BLOCK_BREAK_SPEED);
        if (attributeInstance == null) throw new InternalException("why the fuck is null????? IT SHOULD NOT BE!!!!");
        final float realHardness = event.getBlock().getType().getHardness();
        final float hardness = ((ArtisanSkullState) blockData.getArtisanBlockState()).getHardness();
        attributeInstance.addTransientModifier(
                new AttributeModifier(
                        TEMPLATE_MULTIPLY,
                        (realHardness / hardness) - 1,
                        AttributeModifier.Operation.MULTIPLY_SCALAR_1
                )
        );
    }
}
