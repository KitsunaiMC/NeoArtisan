package io.github.moyusowo.neoartisanapi.api.block.state.base;

import io.github.moyusowo.neoartisanapi.api.block.util.PistonMoveBlockReaction;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Represents a specific state of a custom block, containing visual appearance and actual behavior definitions.
 * <p>
 * Each state corresponds to the block under specific conditions:
 * <ul>
 *   <li><b>Visual appearance</b> - The block appearance seen by clients</li>
 *   <li><b>Actual state</b> - The logical state used by the server</li>
 *   <li><b>Drops</b> - Items produced when broken</li>
 *   <li><b>Some interactions</b> - Piston pushing, flammability, floating, etc.</li>
 * </ul>
 * </p>
 *
 */
public interface ArtisanBaseBlockState {
    /**
     * Gets the Minecraft Internal BlockState ID for client display
     * <p>
     * Corresponds to the {@code BlockState} numeric ID in NMS, used for client rendering.
     * </p>
     *
     * @return NMS block state numeric ID
     */
    int appearanceState();

    /**
     * Gets the actual Minecraft Internal BlockState ID used by the server
     * <p>
     * Used for server-side logic calculations, may differ from visual state.
     * </p>
     *
     * @return NMS block state ID corresponding to actual logic state
     */
    int actualState();

    /**
     * Gets the dropped items when this block state is broken
     * <p>
     * Drops are dynamically generated through {@link ItemGenerator}, supporting conditional drops.
     * The returned array may be empty (indicating no drops), but will not be {@code null}.
     * </p>
     *
     * @return new copies of item stacks (safe to modify)
     */
    @NotNull
    Collection<ItemStack> drops();

    /**
     * Gets the type of interaction between this block state and pistons
     *
     * @return the piston move reaction type for this block state
     */
    @NotNull
    PistonMoveBlockReaction pistonMoveReaction();

    /**
     * Checks if this block state can be broken by fluids
     *
     * @return true if the block can be broken by fluids, false otherwise
     */
    boolean isFlowBreaking();

    /**
     * Checks if this block state can be placed floating in air
     *
     * @return true if the block can survive floating, false otherwise
     */
    boolean canSurviveFloating();

    /**
     * Gets the light emission of this block state
     *
     * @return the brightness level, or {@code null} to use vanilla brightness
     */
    @Nullable
    Integer getBrightness();

    /**
     * Gets the hardness of this block state
     *
     * @return the hardness value, or {@code null} to use vanilla hardness
     */
    @Nullable
    Float getHardness();

    /**
     * Gets the type enum that this block state belongs to
     *
     * @return the enum value representing the block state type, not {@code null}
     */
    @NotNull
    ArtisanBlockStates getType();
}
