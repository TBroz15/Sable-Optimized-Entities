package dev.tuxebro.sable_optimized_entities.mixin;

import dev.ryanhcode.sable.sublevel.SubLevel;
import dev.tuxebro.sable_optimized_entities.EntitySublevelExtension;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.lang.ref.WeakReference;

@Mixin(Entity.class)
public class EntityMixin implements EntitySublevelExtension {
    @Unique
    private WeakReference<SubLevel> SOE$subLevel = null;

    @Override
    @Nullable
    public WeakReference<SubLevel> SOE$getSubLevelRef() {
        return SOE$subLevel;
    }

    @Override
    public void SOE$setSubLevel(@NotNull SubLevel subLevel) {
        SOE$subLevel = new WeakReference<>(subLevel);
    }
}
