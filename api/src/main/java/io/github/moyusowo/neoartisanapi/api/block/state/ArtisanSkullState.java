package io.github.moyusowo.neoartisanapi.api.block.state;

import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBlockStates;
import io.github.moyusowo.neoartisanapi.api.block.util.PistonMoveBlockReaction;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.util.ServiceUtil;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Skull block state definition.
 */
@ApiStatus.NonExtendable
public interface ArtisanSkullState extends ArtisanBaseBlockState {
    /**
     * Creates a new skull block state builder instance
     *
     * @return a skull block state builder instance for creating custom skull block states
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

    /**
     * Gets the texture URL in base64 format for this skull block state
     *
     * @return the base64 encoded texture URL (not null)
     */
    @NotNull
    String getUrlBase64();

    /**
     * Gets the hardness of this skull block state
     *
     * @return the hardness value (not null)
     */
    @NotNull
    Float getHardness();

    @Override
    @NotNull
    default ArtisanBlockStates getType() {
        return ArtisanBlockStates.SKULL;
    }

    interface BuilderFactory {
        @NotNull
        Builder builder();
    }

    /**
     * Builder interface for skull block states, used to gradually build custom skull block state instances
     */
    interface Builder {
        /**
         * Sets the texture for the skull block state
         *
         * @param textureUrl the texture URL for the skull block state, cannot be null
         * @param isBase64 whether the texture URL is already base64 encoded
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder textureUrl(@NotNull String textureUrl, boolean isBase64);

        /**
         * Sets the item generators for the skull block state
         *
         * @param generators the item generators for the skull block state, cannot be null
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder generators(@NotNull ItemGenerator[] generators);

        /**
         * Sets the hardness for the skull block state
         *
         * @param hardness the hardness value for the skull block state
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder hardness(float hardness);

        /**
         * Builds and returns the final skull block state instance
         *
         * @return the completed skull block state instance
         */
        @NotNull
        ArtisanSkullState build();
    }
}
