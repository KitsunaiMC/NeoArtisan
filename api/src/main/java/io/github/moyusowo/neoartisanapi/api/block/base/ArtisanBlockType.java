package io.github.moyusowo.neoartisanapi.api.block.base;

import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCrop;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCropData;
import io.github.moyusowo.neoartisanapi.api.block.crop.ArtisanCropState;
import io.github.moyusowo.neoartisanapi.api.block.thin.ArtisanThinBlock;
import io.github.moyusowo.neoartisanapi.api.block.thin.ArtisanThinBlockData;
import io.github.moyusowo.neoartisanapi.api.block.thin.ArtisanThinBlockState;
import io.github.moyusowo.neoartisanapi.api.block.transparent.ArtisanTransparentBlock;
import io.github.moyusowo.neoartisanapi.api.block.transparent.ArtisanTransparentBlockData;
import io.github.moyusowo.neoartisanapi.api.block.transparent.ArtisanTransparentBlockState;

/**
 * 自定义方块类型枚举，罗列了所有自定义方块类型。
 * <p>
 * 每个自定义方块所采用的方块类型在注册一刻就已经确定，取决于你使用那种类型的Builder构建方块。
 * 不同类型的自定义方块服务器所用的真实方块类型不一样，很多逻辑也不同。
 * </p>
 *
 * @see ArtisanBlock
 * @since 1.0.1
 */
@SuppressWarnings("unused")
public enum ArtisanBlockType {

    /**
     * 作物方块，服务器内的真实方块是小麦。
     *
     * <p>无碰撞箱、遵循原版的植物生长和随机刻逻辑，其他物理遵循原版小麦逻辑。</p>
     */
    CROP_BLOCK(ArtisanCrop.class, ArtisanCropState.class, ArtisanCropData.class),

    /**
     * 薄型方块，服务器内的真实方块是压力板。
     *
     * <p>需要支撑，但默认没有压力板的发送红石信号行为，其他物理遵循原版压力板逻辑，无碰撞箱。</p>
     */
    THIN_BLOCK(ArtisanThinBlock.class, ArtisanThinBlockState.class, ArtisanThinBlockData.class),

    /**
     * 半透明/透明方块，服务器内的真实方块是不会损毁的树叶。
     *
     * <p>不会消失，不会被烧毁，其他行为遵循原版树叶逻辑。</p>
     */
    TRANSPARENT_BLOCK(ArtisanTransparentBlock.class, ArtisanTransparentBlockState.class, ArtisanTransparentBlockData.class);

//    FULL_BLOCK;

    public final Class<? extends ArtisanBlock> artisanBlockClass;
    public final Class<? extends ArtisanBlockState> artisanBlockStateClass;
    public final Class<? extends ArtisanBlockData> artisanBlockDataClass;

    ArtisanBlockType(Class<? extends ArtisanBlock> artisanBlockClass, Class<? extends ArtisanBlockState> artisanBlockStateClass, Class<? extends ArtisanBlockData> artisanBlockDataClass) {
        this.artisanBlockClass = artisanBlockClass;
        this.artisanBlockStateClass = artisanBlockStateClass;
        this.artisanBlockDataClass = artisanBlockDataClass;
    }
}
