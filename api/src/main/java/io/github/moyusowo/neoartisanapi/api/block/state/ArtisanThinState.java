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
 * Thin block state definition.
 */
public interface ArtisanThinState extends ArtisanBaseBlockState {
    /**
     * Creates a new thin block state builder instance
     *
     * @return a thin block state builder instance for creating custom thin block states
     */
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

    /**
     * Builder interface for thin block states, used to gradually build custom thin block state instances
     */
    interface Builder {
        /**
         * Sets the client appearance for the thin block state
         *
         * @param thinAppearance the appearance for the thin block state, cannot be null
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder appearance(@NotNull ThinAppearance thinAppearance);

        /**
         * Sets the item generators for the thin block state
         *
         * @param generators the item generators for the thin block state, cannot be null
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder generators(@NotNull ItemGenerator[] generators);

        /**
         * Builds and returns the final thin block state instance
         *
         * @return the completed thin block state instance
         */
        @NotNull
        ArtisanThinState build();
    }
}
