package dev.tuxebro.sable_optimized_entities;

import dev.ryanhcode.sable.sublevel.SubLevel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;


public interface EntitySublevelUUIDExtension {
    @Nullable
    WeakReference<SubLevel> SOE$getSubLevelRef();

    void SOE$setSubLevel(@NotNull SubLevel subLevel);
}
