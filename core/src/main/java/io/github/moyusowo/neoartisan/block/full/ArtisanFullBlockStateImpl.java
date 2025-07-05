package io.github.moyusowo.neoartisan.block.full;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockStateBase;
import io.github.moyusowo.neoartisanapi.api.block.full.ArtisanFullBlockState;
import io.github.moyusowo.neoartisanapi.api.block.full.FullBlockAppearance;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
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
        private static final int actualState = Block.getId(
                Blocks.STONE.defaultBlockState()
        );

        private int generateAppearanceState() {
            BlockState blockState = Blocks.NOTE_BLOCK.defaultBlockState();
            switch (this.fullBlockAppearance.noteBlockAppearance) {
                case HAT -> blockState = blockState.setValue(BlockStateProperties.NOTEBLOCK_INSTRUMENT, NoteBlockInstrument.HAT);
                case BASEDRUM -> blockState = blockState.setValue(BlockStateProperties.NOTEBLOCK_INSTRUMENT, NoteBlockInstrument.BASEDRUM);
                case SNARE -> blockState = blockState.setValue(BlockStateProperties.NOTEBLOCK_INSTRUMENT, NoteBlockInstrument.SNARE);
                case BASS -> blockState = blockState.setValue(BlockStateProperties.NOTEBLOCK_INSTRUMENT, NoteBlockInstrument.BASS);
                case FLUTE -> blockState = blockState.setValue(BlockStateProperties.NOTEBLOCK_INSTRUMENT, NoteBlockInstrument.FLUTE);
                case BELL -> blockState = blockState.setValue(BlockStateProperties.NOTEBLOCK_INSTRUMENT, NoteBlockInstrument.BELL);
                case GUITAR -> blockState = blockState.setValue(BlockStateProperties.NOTEBLOCK_INSTRUMENT, NoteBlockInstrument.GUITAR);
                case CHIME -> blockState = blockState.setValue(BlockStateProperties.NOTEBLOCK_INSTRUMENT, NoteBlockInstrument.CHIME);
                case XYLOPHONE -> blockState = blockState.setValue(BlockStateProperties.NOTEBLOCK_INSTRUMENT, NoteBlockInstrument.XYLOPHONE);
                case IRON_XYLOPHONE -> blockState = blockState.setValue(BlockStateProperties.NOTEBLOCK_INSTRUMENT, NoteBlockInstrument.IRON_XYLOPHONE);
                case COW_BELL -> blockState = blockState.setValue(BlockStateProperties.NOTEBLOCK_INSTRUMENT, NoteBlockInstrument.COW_BELL);
                case DIDGERIDOO -> blockState = blockState.setValue(BlockStateProperties.NOTEBLOCK_INSTRUMENT, NoteBlockInstrument.DIDGERIDOO);
                case BIT -> blockState = blockState.setValue(BlockStateProperties.NOTEBLOCK_INSTRUMENT, NoteBlockInstrument.BIT);
                case BANJO -> blockState = blockState.setValue(BlockStateProperties.NOTEBLOCK_INSTRUMENT, NoteBlockInstrument.BANJO);
                case PLING -> blockState = blockState.setValue(BlockStateProperties.NOTEBLOCK_INSTRUMENT, NoteBlockInstrument.PLING);
                case DRAGON -> blockState = blockState.setValue(BlockStateProperties.NOTEBLOCK_INSTRUMENT, NoteBlockInstrument.DRAGON);
                case PIGLIN -> blockState = blockState.setValue(BlockStateProperties.NOTEBLOCK_INSTRUMENT, NoteBlockInstrument.PIGLIN);
                case ZOMBIE -> blockState = blockState.setValue(BlockStateProperties.NOTEBLOCK_INSTRUMENT, NoteBlockInstrument.ZOMBIE);
                case CREEPER -> blockState = blockState.setValue(BlockStateProperties.NOTEBLOCK_INSTRUMENT, NoteBlockInstrument.CREEPER);
                case SKELETON -> blockState = blockState.setValue(BlockStateProperties.NOTEBLOCK_INSTRUMENT, NoteBlockInstrument.SKELETON);
                case CUSTOM_HEAD -> blockState = blockState.setValue(BlockStateProperties.NOTEBLOCK_INSTRUMENT, NoteBlockInstrument.CUSTOM_HEAD);
                case WITHER_SKELETON -> blockState = blockState.setValue(BlockStateProperties.NOTEBLOCK_INSTRUMENT, NoteBlockInstrument.WITHER_SKELETON);
            }
            return Block.getId(
                    blockState.setValue(BlockStateProperties.NOTE, this.fullBlockAppearance.note)
                            .setValue(BlockStateProperties.POWERED, this.fullBlockAppearance.powered)
            );
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
