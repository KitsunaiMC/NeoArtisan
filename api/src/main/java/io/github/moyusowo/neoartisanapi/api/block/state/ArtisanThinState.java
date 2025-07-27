package io.github.moyusowo.neoartisanapi.api.block.state;

import io.github.moyusowo.neoartisanapi.api.block.state.appearance.ThinAppearance;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBlockStates;
import io.github.moyusowo.neoartisanapi.api.block.util.PistonMoveBlockReaction;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.util.ServiceUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 薄型方块状态定义。
 */
public interface ArtisanThinState extends ArtisanBaseBlockState {
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
        return true;
    }

    @Override
    default boolean canSurviveFloating() {
        return false;
    }

    @Override
    @Nullable
    default Float getHardness() {
        return null;
    }

    @Override
    @NotNull
    default ArtisanBlockStates getType() {
        return ArtisanBlockStates.THIN;
    }

    interface BuilderFactory {
        @NotNull
        Builder builder();
    }

    interface Builder {
        @NotNull
        Builder appearance(@NotNull ThinAppearance thinAppearance);

        @NotNull
        Builder generators(@NotNull ItemGenerator[] generators);

        @NotNull
        ArtisanThinState build();
    }
}
