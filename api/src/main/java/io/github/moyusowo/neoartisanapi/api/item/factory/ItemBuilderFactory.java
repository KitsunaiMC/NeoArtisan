package io.github.moyusowo.neoartisanapi.api.item.factory;

import io.github.moyusowo.neoartisanapi.api.item.ArtisanItem;
import org.jetbrains.annotations.NotNull;

public interface ItemBuilderFactory {
    @NotNull
    ArtisanItem.ComplexBuilder complexBuilder();

    @NotNull
    ArtisanItem.Builder builder();
}
