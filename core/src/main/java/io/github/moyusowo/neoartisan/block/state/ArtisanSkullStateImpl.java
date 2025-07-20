package io.github.moyusowo.neoartisan.block.state;

import com.github.retrooper.packetevents.protocol.world.states.WrappedBlockState;
import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.state.base.ArtisanBaseBlockStateImpl;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.state.ArtisanSkullState;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

final class ArtisanSkullStateImpl extends ArtisanBaseBlockStateImpl implements ArtisanSkullState {
    @InitMethod(priority = InitPriority.REGISTRAR)
    private static void init() {
        Bukkit.getServicesManager().register(
                BuilderFactory.class,
                BuilderImpl::new,
                NeoArtisan.instance(),
                ServicePriority.Normal
        );
    }

    private final String urlBase64;
    private final float hardness;

    private ArtisanSkullStateImpl(ItemGenerator[] generators, String urlBase64, float hardness) {
        super(WrappedBlockState.getByString("minecraft:player_head[powered=false,rotation=0]").getGlobalId(), WrappedBlockState.getByString("minecraft:player_head[powered=false,rotation=0]").getGlobalId(), generators);
        this.urlBase64 = urlBase64;
        this.hardness = hardness;
    }

    @Override
    public @NotNull String getUrlBase64() {
        return urlBase64;
    }

    @Override
    public boolean canSurviveFloating() {
        return true;
    }

    @Override
    @NotNull
    public Float getHardness() {
        return hardness;
    }

    private static final class BuilderImpl implements Builder {
        private String urlBase64;
        private ItemGenerator[] generators;
        private float hardness;

        private BuilderImpl() {
            urlBase64 = null;
            generators = null;
            hardness = Float.MIN_VALUE;
        }

        @Override
        public @NotNull Builder textureUrl(@NotNull String textureUrl, boolean isBase64) {
            if (isBase64) {
                this.urlBase64 = textureUrl;
            } else {
                this.urlBase64 = Base64.getUrlEncoder().encodeToString(textureUrl.getBytes(StandardCharsets.UTF_8));
            }
            return this;
        }

        @Override
        public @NotNull Builder generators(ItemGenerator[] generators) {
            this.generators = generators;
            return this;
        }

        @Override
        public @NotNull Builder hardness(float hardness) {
            this.hardness = hardness;
            return this;
        }

        @Override
        public @NotNull ArtisanSkullState build() {
            if (generators == null || urlBase64 == null || hardness <= 0) throw new IllegalArgumentException("You must fill all the param correctly!");
            return new ArtisanSkullStateImpl(generators, urlBase64, hardness);
        }
    }
}
