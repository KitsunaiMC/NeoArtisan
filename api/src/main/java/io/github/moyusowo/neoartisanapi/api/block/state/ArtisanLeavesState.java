package io.github.moyusowo.neoartisanapi.api.block.state;

import io.github.moyusowo.neoartisanapi.api.block.state.appearance.LeavesAppearance;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBlockStates;
import io.github.moyusowo.neoartisanapi.api.block.util.PistonMoveBlockReaction;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.util.ServiceUtil;
import org.jetbrains.annotations.NotNull;

/**
 * 树叶型方块状态定义。
 */
public interface ArtisanLeavesState extends ArtisanBaseBlockState {
    @NotNull
    static Builder builder() {
        return ServiceUtil.getService(BuilderFactory.class).builder();
    }

    @Override
    @NotNull
    default PistonMoveBlockReaction pistonMoveReaction() {
        return PistonMoveBlockReaction.BREAK;
    }

    @Override
    default boolean isFlowBreaking() {
        return false;
    }

    boolean canBurn();

    @NotNull
    Float getHardness();

    @Override
    @NotNull
    default ArtisanBlockStates getType() {
        return ArtisanBlockStates.LEAVES;
    }

    interface BuilderFactory {
        @NotNull
        Builder builder();
    }

    interface Builder {
        @NotNull
        Builder appearance(@NotNull LeavesAppearance leavesAppearance);

        @NotNull
        Builder generators(@NotNull ItemGenerator[] generators);

        @NotNull
        Builder hardness(float hardness);

        @NotNull
        Builder burnable();

        @NotNull
        ArtisanLeavesState build();
    }
}
