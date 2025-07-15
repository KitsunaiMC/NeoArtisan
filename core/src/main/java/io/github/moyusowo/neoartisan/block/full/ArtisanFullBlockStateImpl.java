package io.github.moyusowo.neoartisan.block.full;

import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.protocol.world.states.enums.Instrument;
import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockStateBase;
import io.github.moyusowo.neoartisanapi.api.block.full.ArtisanFullBlockState;
import io.github.moyusowo.neoartisanapi.api.block.full.FullBlockAppearance;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

final class ArtisanFullBlockStateImpl extends ArtisanBlockStateBase implements ArtisanFullBlockState {
    @InitMethod(priority = InitPriority.REGISTRAR)
    private static void init() {
        Bukkit.getServicesManager().register(
                BuilderFactory.class,
                new BuilderFactory() {
                    @Override
                    public @NotNull Builder builder() {
                        return new BuilderImpl();
                    }
                },
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    ArtisanFullBlockStateImpl(int appearanceState, int actualState, ItemGenerator[] generators) {
        super(appearanceState, actualState, generators);
    }

    private static class BuilderImpl implements Builder {
        protected FullBlockAppearance fullBlockAppearance;
        protected ItemGenerator[] generators;
        private static final int actualState = WrappedBlockState.getByString("minecraft:stone").getGlobalId();

        private int generateAppearanceState() {
            WrappedBlockState wrappedBlockState = WrappedBlockState.getByString("minecraft:note_block[instrument=harp,note=0,powered=false]");
            switch (this.fullBlockAppearance.noteBlockAppearance) {
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
            wrappedBlockState.setNote(this.fullBlockAppearance.note);
            wrappedBlockState.setPowered(this.fullBlockAppearance.powered);
            return wrappedBlockState.getGlobalId();
        }

        public BuilderImpl() {
            fullBlockAppearance = null;
            generators = null;
        }


        @Override
        public @NotNull Builder appearanceState(@NotNull FullBlockAppearance fullBlockAppearance) {
            this.fullBlockAppearance = fullBlockAppearance;
            return this;
        }

        @Override
        public @NotNull Builder generators(ItemGenerator[] generators) {
            this.generators = generators;
            return this;
        }

        @Override
        public @NotNull ArtisanFullBlockState build() {
            if (generators == null || fullBlockAppearance == null) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanFullBlockStateImpl(generateAppearanceState(), actualState, generators);
        }
    }
}
