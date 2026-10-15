package dev.tuxebro.sable_optimized_entities.mixin;

import dev.ryanhcode.sable.sublevel.SubLevel;
import dev.tuxebro.sable_optimized_entities.EntitySublevelUUIDExtension;
import net.minecraft.server.level.ServerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.lang.ref.WeakReference;

@Mixin(ServerEntity.class)
public class ServerEntityMixin implements EntitySublevelUUIDExtension {
    @Unique
    private WeakReference<SubLevel> SOE$subLevel = null;

    @Nullable
    public WeakReference<SubLevel> SOE$getSubLevelRef() {
        return SOE$subLevel;
    }

    public void SOE$setSubLevel(@NotNull SubLevel subLevel) {
        SOE$subLevel = new WeakReference<>(subLevel);
    }
}
