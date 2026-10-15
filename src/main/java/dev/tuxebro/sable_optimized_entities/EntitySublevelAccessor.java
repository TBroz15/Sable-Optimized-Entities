package dev.tuxebro.sable_optimized_entities;

import dev.ryanhcode.sable.sublevel.SubLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface EntitySublevelAccessor {
    @Nullable
    SubLevel SOE$getSubLevel();

    void SOE$setSubLevel(@NotNull SubLevel subLevel);
}
