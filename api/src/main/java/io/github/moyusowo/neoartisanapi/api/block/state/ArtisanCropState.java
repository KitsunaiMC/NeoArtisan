package io.github.moyusowo.neoartisanapi.api.block.state;

import io.github.moyusowo.neoartisanapi.api.block.state.appearance.CropAppearance;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBlockStates;
import io.github.moyusowo.neoartisanapi.api.block.util.PistonMoveBlockReaction;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.util.ServiceUtil;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Crop block state definition.
 *
 * <p>
 * Crop-type blocks that break when pushed by pistons, cannot float in air,
 * and can be washed away by water.
 * </p>
 *
 */
@ApiStatus.NonExtendable
public interface ArtisanCropState extends ArtisanBaseBlockState {
    /**
     * Creates a new crop block state builder instance
     *
     * @return a crop block state builder instance for creating custom crop block states
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
        return ArtisanBlockStates.CROP;
    }

    interface BuilderFactory {
        @NotNull
        Builder builder();
    }

    /**
     * Builder interface for crop block states, used to gradually build custom crop block state instances
     */
    interface Builder {
        /**
         * Sets the client appearance for the crop block state
         *
         * @param cropAppearance the appearance for the crop block state, cannot be null
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder appearance(@NotNull CropAppearance cropAppearance);

        /**
         * Sets the item generators for the crop block state
         *
         * @param generators the item generators for the crop block state, cannot be null
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder generators(@NotNull ItemGenerator[] generators);

        /**
         * Builds and returns the final crop block state instance
         *
         * @return the completed crop block state instance
         */
        @NotNull
        ArtisanCropState build();
    }
}
