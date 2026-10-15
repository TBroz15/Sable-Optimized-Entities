package dev.tuxebro.sable_optimized_entities;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface EntitySublevelUUIDExtension {
    @Nullable
    UUID SOE$getSubLevelUUID();

    void SOE$setSubLevelUUID(@NotNull UUID uuid);
}
