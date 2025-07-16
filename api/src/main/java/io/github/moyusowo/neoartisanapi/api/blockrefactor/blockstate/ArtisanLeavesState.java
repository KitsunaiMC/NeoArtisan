package io.github.moyusowo.neoartisanapi.api.blockrefactor.blockstate;

import io.github.moyusowo.neoartisanapi.api.blockrefactor.blockstate.appearance.LeavesAppearance;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import io.github.moyusowo.neoartisanapi.api.util.BuilderFactoryUtil;
import org.bukkit.block.PistonMoveReaction;
import org.jetbrains.annotations.NotNull;

public interface ArtisanLeavesState extends ArtisanBaseBlockState {
    /**
     * 获取工厂服务实例
     * <p>
     * 此工厂用于创建全新的 {@link Builder} 实例，确保每次构建过程独立且线程安全。
     * </p>
     *
     * @return 建造器工厂实例（非null）
     * @throws IllegalStateException 如果工厂服务未注册
     * @see Builder 构建器接口
     */
    @NotNull
    static Builder builder() {
        return BuilderFactoryUtil.getBuilder(BuilderFactory.class).builder();
    }

    @Override
    @NotNull
    default PistonMoveReaction isPistonPushable() {
        return PistonMoveReaction.BREAK;
    }

    @Override
    default boolean isFlowBreaking() {
        return false;
    }

    interface BuilderFactory {
        @NotNull
        Builder builder();
    }

    interface Builder {
        @NotNull
        Builder appearance(@NotNull LeavesAppearance leavesAppearance);

        @NotNull
        Builder generators(@NotNull ItemGenerator[] generators);

        @NotNull
        ArtisanLeavesState build();
    }
}
