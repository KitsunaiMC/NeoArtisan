package io.github.moyusowo.neoartisan.block.blockstate;

import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.protocol.world.states.enums.Instrument;
import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.blockstate.ArtisanCommonState;
import io.github.moyusowo.neoartisanapi.api.block.blockstate.appearance.CommonAppearance;
import io.github.moyusowo.neoartisanapi.api.block.blockstate.appearance.common.NoteBlockAppearance;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

public class ArtisanCommonStateImpl extends ArtisanBaseBlockStateImpl implements ArtisanCommonState {
    @InitMethod(priority = InitPriority.REGISTRAR)
    private static void init() {
        Bukkit.getServicesManager().register(
                BuilderFactory.class,
                BuilderImpl::new,
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    protected ArtisanCommonStateImpl(int appearanceState, int actualState, ItemGenerator[] generators) {
        super(appearanceState, actualState, generators);
    }

    private static final class BuilderImpl implements Builder {
        private CommonAppearance commonAppearance;
        private ItemGenerator[] generators;
        private static final int actualState = WrappedBlockState.getByString("minecraft:stone").getGlobalId();

        private int generateAppearanceState() {
            WrappedBlockState wrappedBlockState = WrappedBlockState.getByString("minecraft:note_block[instrument=harp,note=0,powered=false]");
            final NoteBlockAppearance noteBlockAppearance = (NoteBlockAppearance) commonAppearance;
            switch (noteBlockAppearance.noteBlockAppearance()) {
                case HAT -> wrappedBlockState.setInstrument(Instrument.HAT);
                case BASEDRUM -> wrappedBlockState.setInstrument(Instrument.BASEDRUM);
                case SNARE -> wrappedBlockState.setInstrument(Instrument.SNARE);
                case BASS -> wrappedBlockState.setInstrument(Instrument.BASS);
                case FLUTE -> wrappedBlockState.setInstrument(Instrument.FLUTE);
                case BELL -> wrappedBlockState.setInstrument(Instrument.BELL);
                case GUITAR -> wrappedBlockState.setInstrument(Instrument.GUITAR);
                case CHIME -> wrappedBlockState.setInstrument(Instrument.CHIME);
                case XYLOPHONE -> wrappedBlockState.setInstrument(Instrument.XYLOPHONE);
                case IRON_XYLOPHONE -> wrappedBlockState.setInstrument(Instrument.IRON_XYLOPHONE);
                case COW_BELL -> wrappedBlockState.setInstrument(Instrument.COW_BELL);
                case DIDGERIDOO -> wrappedBlockState.setInstrument(Instrument.DIDGERIDOO);
                case BIT -> wrappedBlockState.setInstrument(Instrument.BIT);
                case BANJO -> wrappedBlockState.setInstrument(Instrument.BANJO);
                case PLING -> wrappedBlockState.setInstrument(Instrument.PLING);
                case DRAGON -> wrappedBlockState.setInstrument(Instrument.DRAGON);
                case PIGLIN -> wrappedBlockState.setInstrument(Instrument.PIGLIN);
                case ZOMBIE -> wrappedBlockState.setInstrument(Instrument.ZOMBIE);
                case CREEPER -> wrappedBlockState.setInstrument(Instrument.CREEPER);
                case SKELETON -> wrappedBlockState.setInstrument(Instrument.SKELETON);
                case CUSTOM_HEAD -> wrappedBlockState.setInstrument(Instrument.CUSTOM_HEAD);
                case WITHER_SKELETON -> wrappedBlockState.setInstrument(Instrument.WITHER_SKELETON);
            }
            wrappedBlockState.setNote(noteBlockAppearance.note());
            wrappedBlockState.setPowered(noteBlockAppearance.powered());
            return wrappedBlockState.getGlobalId();

        }

        private BuilderImpl() {
            commonAppearance = null;
            generators = null;
        }


        @Override
        public @NotNull Builder appearance(@NotNull CommonAppearance commonAppearance) {
            this.commonAppearance = commonAppearance;
            return this;
        }

        @Override
        public @NotNull Builder generators(ItemGenerator[] generators) {
            this.generators = generators;
            return this;
        }

        @Override
        public @NotNull ArtisanCommonState build() {
            if (generators == null || commonAppearance == null) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanCommonStateImpl(generateAppearanceState(), actualState, generators);
        }
    }
}
