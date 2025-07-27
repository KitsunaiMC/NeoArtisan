package io.github.moyusowo.neoartisanapi.api.block.state;

import io.github.moyusowo.neoartisanapi.api.block.state.appearance.CommonAppearance;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBlockStates;
import io.github.moyusowo.neoartisanapi.api.block.util.PistonMoveBlockReaction;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.util.ServiceUtil;
import org.jetbrains.annotations.NotNull;

/**
 * 通常型方块状态定义。
 *
 * <p>
 * 不可推动、可悬空、不能被水冲走的最常见的方块。
 * </p>
 *
 */
public interface ArtisanCommonState extends ArtisanBaseBlockState {
    @NotNull
    static Builder builder() {
        return ServiceUtil.getService(BuilderFactory.class).builder();
    }

    @Override
    @NotNull
    default PistonMoveBlockReaction pistonMoveReaction() {
        return PistonMoveBlockReaction.RESIST;
    }

    @Override
    default boolean isFlowBreaking() {
        return false;
    }

    @Override
    default boolean canSurviveFloating() {
        return true;
    }

    @NotNull
    Float getHardness();

    @Override
    @NotNull
    default ArtisanBlockStates getType() {
        return ArtisanBlockStates.COMMON;
    }

    interface BuilderFactory {
        @NotNull
        Builder builder();
    }

    interface Builder {
        @NotNull
        Builder appearance(@NotNull CommonAppearance commonAppearance);

        @NotNull
        Builder generators(@NotNull ItemGenerator[] generators);

        @NotNull
        Builder hardness(float hardness);

        @NotNull
        ArtisanCommonState build();
    }
}
