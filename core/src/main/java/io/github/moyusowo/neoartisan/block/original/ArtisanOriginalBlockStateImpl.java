package io.github.moyusowo.neoartisan.block.original;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockStateBase;
import io.github.moyusowo.neoartisanapi.api.block.original.ArtisanOriginalBlockState;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.craftbukkit.block.CraftBlockState;
import org.bukkit.plugin.ServicePriority;

public class ArtisanOriginalBlockStateImpl extends ArtisanBlockStateBase implements ArtisanOriginalBlockState {

    @InitMethod(priority = InitPriority.REGISTRAR)
    private static void init() {
        Bukkit.getServicesManager().register(
                Builder.class,
                new BuilderImpl(),
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    protected ArtisanOriginalBlockStateImpl(int state, ItemGenerator[] generators) {
        super(state, state, generators);
    }

    public static class BuilderImpl implements Builder {
        protected ItemGenerator[] generators;
        protected Material material;

        private int generateState() {
            if (!material.isBlock()) throw new IllegalArgumentException("You must provide a block material!");
            CraftBlockState craftBlockState = (CraftBlockState) material.createBlockData().createBlockState();
            BlockState blockState = craftBlockState.getHandle();
            return Block.getId(blockState);
        }

        public BuilderImpl() {
            material = null;
            generators = null;
        }


        @Override
        public Builder blockType(Material material) {
            this.material = material;
            return this;
        }

        @Override
        public Builder generators(ItemGenerator[] generators) {
            this.generators = generators;
            return this;
        }

        @Override
        public ArtisanOriginalBlockState build() {
            if (generators == null || material == null) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanOriginalBlockStateImpl(generateState(), generators);
        }
    }
}
