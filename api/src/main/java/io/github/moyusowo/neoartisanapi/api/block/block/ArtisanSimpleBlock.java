package io.github.moyusowo.neoartisanapi.api.block.block;

import io.github.moyusowo.neoartisanapi.api.block.block.base.ArtisanBaseBlock;
import io.github.moyusowo.neoartisanapi.api.block.block.base.ArtisanBlocks;
import io.github.moyusowo.neoartisanapi.api.block.util.SoundProperty;
import io.github.moyusowo.neoartisanapi.api.block.gui.GUICreator;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.util.ServiceUtil;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * Interface representing a simple block.
 *
 * <p>
 * This simple block type has only one block state, suitable for decorative blocks or GUI-type blocks.
 * </p>
 *
 * @see ArtisanBaseBlock base block interface
 */
@ApiStatus.NonExtendable
public interface ArtisanSimpleBlock extends ArtisanBaseBlock {
    /**
     * Creates a new simple block builder instance
     *
     * @return a simple block builder instance for creating custom simple blocks
     */
    @NotNull
    static Builder builder() {
        return ServiceUtil.getService(BuilderFactory.class).builder();
    }

    @Override
    @NotNull
    default ArtisanBlocks getType() {
        return ArtisanBlocks.SIMPLE;
    }

    interface BuilderFactory {
        @NotNull
        Builder builder();
    }

    /**
     * Builder interface for simple blocks, used to gradually build custom simple block instances
     */
    interface Builder {
        /**
         * Sets the unique identifier for the simple block
         *
         * @param blockId the namespaced key identifier for the simple block, cannot be null
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder blockId(@NotNull NamespacedKey blockId);

        /**
         * Sets the single state instance for the simple block
         *
         * @param stage the block state for the simple block, cannot be null
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder state(@NotNull ArtisanBaseBlockState stage);

        /**
         * Sets the placement sound property for the simple block
         *
         * @param placeSoundProperty the placement sound property for the simple block, cannot be null
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder placeSound(@NotNull SoundProperty placeSoundProperty);

        /**
         * Sets the GUI creator for the simple block
         *
         * @param guiCreator the GUI creator for the simple block, cannot be null
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder guiCreator(@NotNull GUICreator guiCreator);

        /**
         * Enables block entity functionality for the simple block
         *
         * @return the builder instance, supporting method chaining
         */
        @NotNull
        Builder blockEntity();

        /**
         * Builds and returns the final simple block instance
         *
         * @return the completed simple block instance
         */
        @NotNull
        ArtisanSimpleBlock build();
    }
}
