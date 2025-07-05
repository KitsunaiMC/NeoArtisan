package io.github.moyusowo.neoartisan.block.head;

import io.github.moyusowo.neoartisan.NeoArtisan;
import io.github.moyusowo.neoartisan.block.util.BlockStateUtil;
import io.github.moyusowo.neoartisan.util.init.InitMethod;
import io.github.moyusowo.neoartisan.util.init.InitPriority;
import io.github.moyusowo.neoartisanapi.api.block.base.ArtisanBlockStateBase;
import io.github.moyusowo.neoartisanapi.api.block.head.ArtisanHeadBlockState;
import io.github.moyusowo.neoartisanapi.api.item.ItemGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

class ArtisanHeadBlockStateImpl extends ArtisanBlockStateBase implements ArtisanHeadBlockState {
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

    private final String urlBase64;

    protected ArtisanHeadBlockStateImpl(ItemGenerator[] generators, String urlBase64) {
        super(Block.getId(Blocks.PLAYER_HEAD.defaultBlockState()), Block.getId(Blocks.PLAYER_HEAD.defaultBlockState()), generators);
        this.urlBase64 = urlBase64;
    }

    @Override
    public @NotNull String getUrlBase64() {
        return urlBase64;
    }

    private static class BuilderImpl implements Builder {
        protected String urlBase64;
        protected ItemGenerator[] generators;

        public BuilderImpl() {
            urlBase64 = null;
            generators = null;
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
        public @NotNull ArtisanHeadBlockState build() {
            if (generators == null || urlBase64 == null) throw new IllegalArgumentException("You must fill all the param!");
            return new ArtisanHeadBlockStateImpl(generators, urlBase64);
        }
    }
}
