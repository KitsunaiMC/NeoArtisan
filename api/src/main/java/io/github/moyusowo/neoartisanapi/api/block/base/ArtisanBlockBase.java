package io.github.moyusowo.neoartisanapi.api.block.base;

import io.github.moyusowo.neoartisanapi.api.block.base.sound.SoundProperty;
import io.github.moyusowo.neoartisanapi.api.block.gui.ArtisanBlockGUI;
import io.github.moyusowo.neoartisanapi.api.block.gui.GUICreator;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义方块的抽象基类，提供 {@link ArtisanBlock} 接口的通用实现。
 * <p>
 * <b>重要设计规范：</b>
 * <ol>
 *   <li>所有具体自定义方块实现 <strong>必须</strong> 继承此类，而非直接实现 {@link ArtisanBlock} 接口</li>
 *   <li>子类应通过构造函数注入所有必要组件，确保实例不可变</li>
 *   <li>状态列表 ({@code stages}) 应在构造时完全初始化，后续不可修改，最好是不可变列表</li>
 * </ol>
 *
 * @see ArtisanBlock 基础接口定义
 * @see #ArtisanBlockBase(NamespacedKey, List, GUICreator, SoundProperty, SoundProperty) 唯一允许的构造方式
 * @since 1.0.0
 */
public abstract class ArtisanBlockBase implements ArtisanBlock {

    private final NamespacedKey blockId;
    private final List<ArtisanBlockState> stages;
    private final GUICreator creator;
    private final SoundProperty placeSound;
    private final SoundProperty breakSound;

    protected ArtisanBlockBase(@NotNull NamespacedKey blockId, @NotNull List<? extends ArtisanBlockState> stages, @Nullable GUICreator creator, SoundProperty placeSound, SoundProperty breakSound) {
        this.blockId = blockId;
        this.creator = creator;
        this.placeSound = placeSound;
        this.breakSound = breakSound;
        this.stages = new ArrayList<>();
        this.stages.addAll(stages);
    }

    @Override
    @Nullable
    public ArtisanBlockGUI createGUI(Location location) {
        if (this.creator == null) return null;
        return this.creator.create(location);
    }

    @Override
    @NotNull
    public NamespacedKey getBlockId() {
        return this.blockId;
    }

    @Override
    @NotNull
    public ArtisanBlockState getState(int n) {
        if (n > getTotalStates()) return this.stages.getLast();
        else return this.stages.get(n);
    }

    @Override
    public int getTotalStates() {
        return this.stages.size() - 1;
    }

    @Override
    public SoundProperty getPlaceSoundProperty() { return this.placeSound; }

    @Override
    public SoundProperty getBreakSoundProperty() { return this.breakSound; }

}
