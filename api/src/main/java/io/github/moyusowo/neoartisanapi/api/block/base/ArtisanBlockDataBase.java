package io.github.moyusowo.neoartisanapi.api.block.base;

import io.github.moyusowo.neoartisanapi.api.NeoArtisanAPI;
import io.github.moyusowo.neoartisanapi.api.block.gui.ArtisanBlockGUI;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * 自定义方块数据的基础抽象实现，提供 {@link ArtisanBlockData} 的核心功能。
 * <p>
 * <b>重要设计规范：</b>
 * <ol>
 *   <li>所有具体方块数据实现 <strong>必须</strong> 继承此类，而非直接实现 {@link ArtisanBlockData} 接口</li>
 *   <li>子类应通过构造函数注入必需参数（blockId/stage/location）</li>
 *   <li>持久化数据容器({@link #persistentDataContainer})由基类自动初始化</li>
 *   <li>GUI实例({@link #artisanBlockGUI})在构造时创建</li>
 * </ol>
 *
 * @see ArtisanBlockData 基础数据接口
 * @see #ArtisanBlockDataBase(NamespacedKey, int, Location) 必须使用的构造方法
 * @since 1.0.0
 */
public abstract class ArtisanBlockDataBase implements ArtisanBlockData {

    private final Location location;
    private final NamespacedKey blockId;
    private final int stage;
    private final PersistentDataContainer persistentDataContainer;
    private final ArtisanBlockGUI artisanBlockGUI;

    protected ArtisanBlockDataBase(@NotNull NamespacedKey blockId, int stage, @NotNull Location location) {
        this.blockId = blockId;
        this.stage = stage;
        this.location = location;
        this.persistentDataContainer = NeoArtisanAPI.emptyPersistentDataContainer().emptyPersistentDataContainer();
        this.artisanBlockGUI = this.getArtisanBlock().createGUI(location);
    }

    @Override
    @Nullable
    public ArtisanBlockGUI getGUI() {
        return this.artisanBlockGUI;
    }

    @Override
    public @NotNull ArtisanBlock getArtisanBlock() {
        return NeoArtisanAPI.getBlockRegistry().getArtisanBlock(blockId);
    }

    @Override
    public @NotNull ArtisanBlockState getArtisanBlockState() {
        return NeoArtisanAPI.getBlockRegistry().getArtisanBlock(blockId).getState(stage);
    }

    @Override
    public @NotNull PersistentDataContainer getPersistentDataContainer() {
        return this.persistentDataContainer;
    }

    @ApiStatus.Internal
    protected void setPersistentDataContainer(PersistentDataContainer persistentDataContainer) {
        persistentDataContainer.copyTo(this.persistentDataContainer, true);
    }

    @Override
    public @NotNull NamespacedKey blockId() {
        return this.blockId;
    }

    @Override
    public int stage() {
        return this.stage;
    }

    @Override
    public @NotNull Location getLocation() {
        return this.location;
    }

}
