package io.github.moyusowo.neoartisanapi.api.block.state;

import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBaseBlockState;
import io.github.moyusowo.neoartisanapi.api.block.state.base.ArtisanBlockStates;
import io.github.moyusowo.neoartisanapi.api.block.util.PistonMoveBlockReaction;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.util.BuilderFactoryUtil;
import org.jetbrains.annotations.NotNull;

/**
 * 头颅型方块状态定义。
 */
public interface ArtisanSkullState extends ArtisanBaseBlockState {
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

    @NotNull
    String getUrlBase64();

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

    interface Builder {
        @NotNull
        Builder textureUrl(@NotNull String textureUrl, boolean isBase64);

        @NotNull
        Builder generators(@NotNull ItemGenerator[] generators);

        @NotNull
        Builder hardness(float hardness);

        @NotNull
        ArtisanSkullState build();
    }
}
