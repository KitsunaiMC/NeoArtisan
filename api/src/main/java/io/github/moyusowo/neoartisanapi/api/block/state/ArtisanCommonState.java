package io.github.moyusowo.neoartisanapi.api.block.state;

import io.github.moyusowo.neoartisanapi.api.block.state.appearance.CommonAppearance;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBlockStates;
import io.github.moyusowo.neoartisanapi.api.block.util.PistonMoveBlockReaction;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.util.ServiceUtil;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Common block state definition.
 *
 * <p>
 * The most common type of block that cannot be pushed by pistons, can float in air,
 * and cannot be washed away by water.
 * </p>
 *
 */
@ApiStatus.NonExtendable
public interface ArtisanCommonState extends ArtisanBaseBlockState {
    /**
     * Creates a new common block state builder instance
     *
     * @return a common block state builder instance for creating custom common block states
     */
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

    /**
     * Gets the hardness of this common block state
     *
     * @return the hardness value (not null)
     */
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

    /**
     * Builder interface for common block states, used to gradually build custom common block state instances
     */
    interface Builder {
        /**
         * Sets the client appearance for the common block state
         *
         * @param commonAppearance the appearance for the common block state, cannot be null
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder appearance(@NotNull CommonAppearance commonAppearance);

        /**
         * Sets the item generators for the common block state
         *
         * @param generators the item generators for the common block state, cannot be null
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder generators(@NotNull ItemGenerator[] generators);

        /**
         * Sets the hardness for the common block state
         *
         * @param hardness the hardness value for the common block state
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder hardness(float hardness);

        /**
         * Builds and returns the final common block state instance
         *
         * @return the completed common block state instance
         */
        @NotNull
        ArtisanCommonState build();
    }
}
