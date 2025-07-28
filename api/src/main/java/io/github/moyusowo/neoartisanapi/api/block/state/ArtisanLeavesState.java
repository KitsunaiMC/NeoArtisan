package io.github.moyusowo.neoartisanapi.api.block.state;

import io.github.moyusowo.neoartisanapi.api.block.state.appearance.LeavesAppearance;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBlockStates;
import io.github.moyusowo.neoartisanapi.api.block.util.PistonMoveBlockReaction;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.util.ServiceUtil;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Leaves block state definition.
 */
@ApiStatus.NonExtendable
public interface ArtisanLeavesState extends ArtisanBaseBlockState {
    /**
     * Creates a new leaves block state builder instance
     *
     * @return a leaves block state builder instance for creating custom leaves block states
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
        return false;
    }

    /**
     * Checks if this leaves block state can burn
     *
     * @return true if the leaves can burn, false otherwise
     */
    boolean canBurn();

    /**
     * Gets the hardness of this leaves block state
     *
     * @return the hardness value (not null)
     */
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

    /**
     * Builder interface for leaves block states, used to gradually build custom leaves block state instances
     */
    interface Builder {
        /**
         * Sets the client appearance for the leaves block state
         *
         * @param leavesAppearance the appearance for the leaves block state, cannot be null
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder appearance(@NotNull LeavesAppearance leavesAppearance);

        /**
         * Sets the item generators for the leaves block state
         *
         * @param generators the item generators for the leaves block state, cannot be null
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder generators(@NotNull ItemGenerator[] generators);

        /**
         * Sets the hardness for the leaves block state
         *
         * @param hardness the hardness value for the leaves block state
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder hardness(float hardness);

        /**
         * Makes the leaves block state burnable
         *
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder burnable();

        /**
         * Builds and returns the final leaves block state instance
         *
         * @return the completed leaves block state instance
         */
        @NotNull
        ArtisanLeavesState build();
    }
}
