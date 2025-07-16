package io.github.moyusowo.neoartisanapi.api.block.blockstate;

import io.github.moyusowo.neoartisanapi.api.block.blockstate.appearance.ThinAppearance;
import io.github.moyusowo.neoartisanapi.api.block.util.PistonMoveBlockReaction;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.util.BuilderFactoryUtil;
import org.jetbrains.annotations.NotNull;

public interface ArtisanThinState extends ArtisanBaseBlockState {
    @NotNull
    static Builder builder() {
        return BuilderFactoryUtil.getBuilder(BuilderFactory.class).builder();
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
