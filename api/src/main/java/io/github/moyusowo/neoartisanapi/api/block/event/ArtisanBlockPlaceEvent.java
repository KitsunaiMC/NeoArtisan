package io.github.moyusowo.neoartisanapi.api.block.event;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlock;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockData;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * 当玩家放置自定义方块时触发的事件。
 * <p>
 * 继承自 {@link BlockPlaceEvent}，包含被放置的自定义方块实例。
 * 可用于取消放置行为或修改放置后的方块状态。
 * </p>
 *
 * @see ArtisanBlock 自定义方块类型
 * @since 1.0.0
 */
public class ArtisanBlockPlaceEvent extends BlockPlaceEvent {

    private static final HandlerList handlers = new HandlerList();

    protected ArtisanBlock artisanBlock;
    protected ArtisanBlockData placedArtisanBlockData;

    /**
     * 构造自定义方块放置事件
     *
     * @param placedBlock 被放置的方块实例（非null）
     * @param replacedBlockState 被替换的原有方块状态（非null）
     * @param placedAgainst 所依附的相邻方块（非null）
     * @param itemInHand 玩家手持的物品（非null）
     * @param thePlayer 放置方块的玩家（非null）
     * @param canBuild 是否有建造权限
     * @param hand 使用的手持装备槽（非null）
     * @param artisanBlock 关联的自定义方块定义（非null）
     */
    public ArtisanBlockPlaceEvent(@NotNull Block placedBlock, @NotNull BlockState replacedBlockState, @NotNull Block placedAgainst, @NotNull ItemStack itemInHand, @NotNull Player thePlayer, boolean canBuild, @NotNull EquipmentSlot hand, @NotNull ArtisanBlock artisanBlock, @NotNull ArtisanBlockData placedArtisanBlockData) {
        super(placedBlock, replacedBlockState, placedAgainst, itemInHand, thePlayer, canBuild, hand);
        this.artisanBlock = artisanBlock;
        this.placedArtisanBlockData = placedArtisanBlockData;
    }

    /**
     * 获取被放置的自定义方块定义
     *
     * @return 自定义方块实例，包含方块类型和状态信息
     */
    public @NotNull ArtisanBlock getArtisanBlock() {
        return this.artisanBlock;
    }

    /**
     * 获取即将被储存的自定义方块BlockData
     *
     * @return 即将被储存的自定义方块BlockData
     * @see ArtisanBlockData
     * @since 1.0.1
     */
    public ArtisanBlockData getPlacedArtisanBlockData() { return this.placedArtisanBlockData; }

    /**
     * 设置即将被储存的自定义方块BlockData
     *
     * @see ArtisanBlockData
     * @since 1.0.1
     */
    public void setPlacedArtisanBlockData(ArtisanBlockData placedArtisanBlockData) { this.placedArtisanBlockData = placedArtisanBlockData; }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
