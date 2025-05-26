package io.github.moyusowo.neoartisanapi.api.persistence;

import org.bukkit.persistence.PersistentDataContainer;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface EmptyPersistentDataContainer {

    PersistentDataContainer emptyPersistentDataContainer();

}
