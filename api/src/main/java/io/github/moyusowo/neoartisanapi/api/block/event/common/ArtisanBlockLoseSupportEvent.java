package io.github.moyusowo.neoartisanapi.api.block.event.common;

import io.github.moyusowo.neoartisanapi.api.block.block.base.ArtisanBaseBlock;
import org.bukkit.block.Block;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.BlockExpEvent;
import org.jetbrains.annotations.NotNull;

/**
 * 当自定义方块因失去支撑（如下方方块被破坏）可能掉落时触发的事件。
 * <p>
 * 继承自 {@link BlockExpEvent}，事件不可取消。
 * </p>
 *
 * @see ArtisanBaseBlock 自定义方块类型
 * @see BlockExpEvent
 */
public class ArtisanBlockLoseSupportEvent extends BlockExpEvent {

    private static final HandlerList handlers = new HandlerList();
    private final ArtisanBaseBlock artisanBaseBlock;
    private boolean dropItems;

    /**
     * 构造失去支撑事件
     *
     * @param theBlock 即将掉落的方块实例（非null）
     * @param artisanBaseBlock 关联的自定义方块定义（非null）
     */
    public ArtisanBlockLoseSupportEvent(@NotNull final Block theBlock, @NotNull ArtisanBaseBlock artisanBaseBlock) {
        super(theBlock, 0);
        this.dropItems = true;
        this.artisanBaseBlock = artisanBaseBlock;
    }

    /**
     * 设置是否生成掉落物
     *
     * @param dropItems true时生成正常掉落物，false时无掉落
     */
    public void setDropItems(boolean dropItems) {
        this.dropItems = dropItems;
    }

    /**
     * 检查是否会生成掉落物
     *
     * @return 当前掉落物生成状态
     */
    public boolean isDropItems() {
        return this.dropItems;
    }

    /**
     * 获取关联的自定义方块定义
     *
     * @return 自定义方块实例
     */
    public @NotNull ArtisanBaseBlock getArtisanBlock() {
        return this.artisanBaseBlock;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    @NotNull
    public static HandlerList getHandlerList() {
        return handlers;
    }
}
