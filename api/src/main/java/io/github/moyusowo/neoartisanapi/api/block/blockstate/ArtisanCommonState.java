package io.github.moyusowo.neoartisanapi.api.block.blockstate;

import io.github.moyusowo.neoartisanapi.api.block.blockstate.appearance.CommonAppearance;
import io.github.moyusowo.neoartisanapi.api.block.util.PistonMoveBlockReaction;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.util.BuilderFactoryUtil;
import org.jetbrains.annotations.NotNull;

public interface ArtisanCommonState extends ArtisanBaseBlockState {
    @NotNull
    static Builder builder() {
        return BuilderFactoryUtil.getBuilder(BuilderFactory.class).builder();
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
        ArtisanCommonState build();
    }
}
