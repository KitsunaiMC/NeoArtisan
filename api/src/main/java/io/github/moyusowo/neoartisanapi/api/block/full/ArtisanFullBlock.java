package io.github.moyusowo.neoartisanapi.api.block.full;

import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlock;
import io.github.moyusowo.neoartisanapi.api.block.base.sound.SoundProperty;
import io.github.moyusowo.neoartisanapi.api.block.gui.GUICreator;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 表示完整自定义方块的基础接口。
 * <p>
 * 所有实例应通过 {@link ArtisanFullBlock.Builder} 构建。
 * </p>
 *
 * @see ArtisanBlock 基础方块接口
 * @since 2.0.0
 */
public interface ArtisanFullBlock extends ArtisanBlock {
    /**
     * 获取工厂服务实例
     * <p>
     * 此工厂用于创建全新的 {@link Builder} 实例，确保每次构建过程独立且线程安全。
     * </p>
     *
     * @return 作物建造器工厂实例（非null）
     * @throws IllegalStateException 如果工厂服务未注册
     * @see Builder 构建器接口
     * @since 2.0.0
     */
    static BuilderFactory factory() {
        return Bukkit.getServicesManager().load(BuilderFactory.class);
    }

    /**
     * 建造器工厂接口
     * <p>
     * 负责创建全新的 {@link Builder} 实例，确保每次构建过程独立。
     * 实现必须保证每次调用 {@link #builder()} 都返回<strong>未使用过</strong>的构建器。
     * </p>
     *
     * <p><b>实现要求：</b></p>
     * <ol>
     *   <li>禁止返回单例或共享实例</li>
     *   <li>构建器初始状态必须完全重置</li>
     * </ol>
     */
    interface BuilderFactory {
        @NotNull
        Builder builder();
    }

    @NotNull
    ArtisanFullBlockState getState(int n);

    /**
     * 完整方块建造器接口
     */
    interface Builder extends BaseBuilder {

        @NotNull Builder blockId(@NotNull NamespacedKey blockId);

        @NotNull Builder states(@NotNull List<ArtisanFullBlockState> states);

        @NotNull Builder placeSound(@NotNull SoundProperty placeSoundProperty);

        @NotNull Builder breakSound(@NotNull SoundProperty breakSoundProperty);

        @NotNull Builder guiCreator(@NotNull GUICreator creator);

        ArtisanFullBlock build();
    }
}
