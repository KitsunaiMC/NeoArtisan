package io.github.moyusowo.neoartisanapi.api.blockrefactor.blockstate.appearance.common;

import io.github.moyusowo.neoartisanapi.api.blockrefactor.blockstate.appearance.CommonAppearance;
import org.jetbrains.annotations.NotNull;

public record NoteBlockAppearance(
        @NotNull NoteBlockSoundProperty noteBlockAppearance,
        int note,
        boolean powered
) implements CommonAppearance {

    public enum NoteBlockSoundProperty {
        HAT,
        BASEDRUM,
        SNARE,
        BASS,
        FLUTE,
        BELL,
        GUITAR,
        CHIME,
        XYLOPHONE,
        IRON_XYLOPHONE,
        COW_BELL,
        DIDGERIDOO,
        BIT,
        BANJO,
        PLING,
        ZOMBIE,
        SKELETON,
        CREEPER,
        DRAGON,
        WITHER_SKELETON,
        PIGLIN,
        CUSTOM_HEAD
    }
}
