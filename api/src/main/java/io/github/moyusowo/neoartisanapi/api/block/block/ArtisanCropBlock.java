package io.github.moyusowo.neoartisanapi.api.block.block;

import io.github.moyusowo.neoartisanapi.api.block.block.base.ArtisanBaseBlock;
import io.github.moyusowo.neoartisanapi.api.block.block.base.ArtisanBlocks;
import io.github.moyusowo.neoartisanapi.api.block.data.ArtisanBlockData;
import io.github.moyusowo.neoartisanapi.api.block.util.SoundProperty;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.util.ServiceUtil;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Interface representing a custom crop block, extending the base block interface {@link ArtisanBaseBlock}
 * and providing crop-specific functionalities and behavior definitions.
 *
 * <p>
 * This interface is specifically designed for handling custom crop-type blocks, including growth control,
 * bone meal acceleration, and other crop-specific behaviors.
 * </p>
 *
 * @see ArtisanBaseBlock base block interface that defines general block behaviors
 */
@ApiStatus.NonExtendable
public interface ArtisanCropBlock extends ArtisanBaseBlock {
    /**
     * Creates a new crop block builder instance
     *
     * @return a crop block builder instance for creating custom crop blocks
     */
    @NotNull
    static Builder builder() {
        return ServiceUtil.getService(BuilderFactory.class).builder();
    }

    interface BuilderFactory {
        @NotNull
        Builder builder();
    }

    /**
     * Gets the minimum growth increment when bone meal is applied
     *
     * @return the minimum number of growth stages advanced per bone meal use (≥0)
     */
    int getBoneMealMinGrowth();

    /**
     * Gets the maximum growth increment when bone meal is applied
     *
     * @return the maximum number of growth stages the crop can advance when bone meal is used,
     *         greater than or equal to the minimum growth increment
     */
    int getBoneMealMaxGrowth();

    /**
     * Generates a random growth increment when bone meal is applied
     *
     * @return a random value between the minimum and maximum growth increments
     */
    int generateBoneMealGrowth();

    /**
     * Handles the random tick event for the crop, used to implement natural crop growth logic
     *
     * @param cropData the current crop block data instance containing state and other related information
     */
    void onRandomTick(ArtisanBlockData cropData);

    @Override
    default boolean hasBlockEntity() {
        return false;
    }

    @Override
    @NotNull
    default ArtisanBlocks getType() {
        return ArtisanBlocks.CROP;
    }

    /**
     * Builder interface for crop blocks, used to gradually build custom crop block instances
     */
    interface Builder {
        /**
         * Sets the unique identifier for the crop block
         *
         * @param blockId the namespaced key identifier for the crop block, cannot be null
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder blockId(@NotNull NamespacedKey blockId);

        /**
         * Sets the list of all state instances for the crop block
         *
         * @param states the list of crop block states, each state corresponds to a growth stage
         *               or other state of the crop, cannot be null
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder states(@NotNull List<ArtisanBaseBlockState> states);

        /**
         * Sets the placement sound property for the crop block
         *
         * @param placeSoundProperty the placement sound property for the crop block, cannot be null
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder placeSound(@NotNull SoundProperty placeSoundProperty);

        /**
         * Sets the minimum growth increment when bone meal is applied
         *
         * @param boneMealMinGrowth the minimum bone meal growth increment, should be greater than or equal to 0
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder boneMealMinGrowth(int boneMealMinGrowth);

        /**
         * Sets the maximum growth increment when bone meal is applied
         *
         * @param boneMealMaxGrowth the maximum bone meal growth increment, should be greater than or equal
         *                          to the minimum growth increment
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder boneMealMaxGrowth(int boneMealMaxGrowth);

        /**
         * Builds and returns the final crop block instance
         *
         * @return the completed crop block instance
         * @throws IllegalArgumentException thrown if required parameters are not set or parameters are invalid
         */
        @NotNull
        ArtisanCropBlock build();
    }
}
