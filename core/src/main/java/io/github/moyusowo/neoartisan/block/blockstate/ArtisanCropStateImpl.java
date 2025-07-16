package io.github.moyusowo.neoartisan.block.blockstate;

import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import com.github.retrooper.packetevents.protocol.world.states.enums.East;
import com.github.retrooper.packetevents.protocol.world.states.enums.North;
import com.github.retrooper.packetevents.protocol.world.states.enums.South;
import com.github.retrooper.packetevents.protocol.world.states.enums.West;
import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.blockstate.ArtisanCropState;
import io.github.moyusowo.neoartisanapi.api.block.blockstate.appearance.CropAppearance;
import io.github.moyusowo.neoartisanapi.api.block.blockstate.appearance.crop.OriginalCropAppearance;
import io.github.moyusowo.neoartisanapi.api.block.blockstate.appearance.crop.SugarCaneAppearance;
import io.github.moyusowo.neoartisanapi.api.block.blockstate.appearance.crop.TripwireAppearance;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

public class ArtisanCropStateImpl extends ArtisanBaseBlockStateImpl implements ArtisanCropState {
    @InitMethod(priority = InitPriority.REGISTRAR)
    private static void init() {
        Bukkit.getServicesManager().register(
                BuilderFactory.class,
                BuilderImpl::new,
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    protected ArtisanCropStateImpl(int appearanceState, int actualState, ItemGenerator[] generators) {
        super(appearanceState, actualState, generators);
    }

    private static final class BuilderImpl implements Builder {
        private CropAppearance cropAppearance;
        private ItemGenerator[] generators;
        private static final int actualState = WrappedBlockState.getByString("minecraft:wheat[age=1]").getGlobalId();

        private int generateAppearanceState() {
            if (this.cropAppearance instanceof TripwireAppearance tripwireAppearance) {
                WrappedBlockState wrappedBlockState = WrappedBlockState.getByString("minecraft:tripwire[attached=false,disarmed=false,east=false,north=false,powered=false,south=false,west=false]");
                wrappedBlockState.setAttached(tripwireAppearance.get(TripwireAppearance.BlockStateProperty.ATTACHED));
                wrappedBlockState.setDisarmed(tripwireAppearance.get(TripwireAppearance.BlockStateProperty.DISARMED));
                if (tripwireAppearance.get(TripwireAppearance.BlockStateProperty.EAST)) {
                    wrappedBlockState.setEast(East.TRUE);
                } else {
                    wrappedBlockState.setEast(East.FALSE);
                }
                if (tripwireAppearance.get(TripwireAppearance.BlockStateProperty.NORTH)) {
                    wrappedBlockState.setNorth(North.TRUE);
                } else {
                    wrappedBlockState.setNorth(North.FALSE);
                }
                if (tripwireAppearance.get(TripwireAppearance.BlockStateProperty.WEST)) {
                    wrappedBlockState.setWest(West.TRUE);
                } else {
                    wrappedBlockState.setWest(West.FALSE);
                }
                if (tripwireAppearance.get(TripwireAppearance.BlockStateProperty.SOUTH)) {
                    wrappedBlockState.setSouth(South.TRUE);
                } else {
                    wrappedBlockState.setSouth(South.FALSE);
                }
                wrappedBlockState.setPowered(tripwireAppearance.get(TripwireAppearance.BlockStateProperty.POWERED));
                return wrappedBlockState.getGlobalId();
            } else if (this.cropAppearance instanceof SugarCaneAppearance sugarCaneAppearance) {
                WrappedBlockState wrappedBlockState = WrappedBlockState.getByString("minecraft:sugar_cane[age=0]");
                wrappedBlockState.setAge(sugarCaneAppearance.getAge());
                return wrappedBlockState.getGlobalId();
            } else {
                OriginalCropAppearance originalCropAppearance = (OriginalCropAppearance) this.cropAppearance;
                final WrappedBlockState wrappedBlockState;
                switch (originalCropAppearance.originalCropMaterial()) {
                    case WHEAT -> {
                        wrappedBlockState = WrappedBlockState.getByString("minecraft:wheat[age=1]");
                        wrappedBlockState.setAge(originalCropAppearance.age());
                    }
                    case CARROT -> {
                        wrappedBlockState = WrappedBlockState.getByString("minecraft:carrots[age=1]");
                        wrappedBlockState.setAge(originalCropAppearance.age());
                    }
                    case POTATO -> {
                        wrappedBlockState = WrappedBlockState.getByString("minecraft:potatoes[age=1]");
                        wrappedBlockState.setAge(originalCropAppearance.age());
                    }
                    case BEETROOT -> {
                        wrappedBlockState = WrappedBlockState.getByString("minecraft:beetroot[age=1]");
                        wrappedBlockState.setAge(originalCropAppearance.age());
                    }
                    case TORCH_FLOWER -> {
                        wrappedBlockState = WrappedBlockState.getByString("minecraft:torchflower_crop[age=1]");
                        wrappedBlockState.setAge(originalCropAppearance.age());
                    }
                    case PITCHER_PLANT -> {
                        wrappedBlockState = WrappedBlockState.getByString("minecraft:pitcher_crop[age=1]");
                        wrappedBlockState.setAge(originalCropAppearance.age());
                    }
                    default -> wrappedBlockState = WrappedBlockState.getByGlobalId(0);
                }
                return wrappedBlockState.getGlobalId();
            }
        }

        private BuilderImpl() {
            cropAppearance = null;
            generators = null;
        }

        @Override
        public @NotNull Builder appearance(@NotNull CropAppearance cropAppearanceBlock) {
            this.cropAppearance = cropAppearanceBlock;
            return this;
        }

        @Override
        public @NotNull Builder generators(ItemGenerator[] generators) {
            this.generators = generators;
            return this;
        }

        @Override
        public @NotNull ArtisanCropState build() {
            if (generators == null || cropAppearance == null) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanCropStateImpl(generateAppearanceState(), actualState, generators);
        }
    }
}
