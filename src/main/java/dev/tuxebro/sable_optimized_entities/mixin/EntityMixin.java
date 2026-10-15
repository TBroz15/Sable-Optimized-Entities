package dev.tuxebro.sable_optimized_entities.mixin;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.sublevel.SubLevel;
import dev.tuxebro.sable_optimized_entities.EntitySublevelAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.lang.ref.WeakReference;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntitySublevelAccessor {
    @Shadow
    public abstract Level level();

    @Shadow
    public abstract Vec3 position();

    @Unique
    private WeakReference<SubLevel> SOE$subLevelRef = null;

    @Override
    @Nullable
    public SubLevel SOE$getSubLevel() {
        WeakReference<SubLevel> subLevelRef = SOE$subLevelRef;

        SubLevel subLevel = (subLevelRef != null)
                ? subLevelRef.get()
                : null;

        if (subLevel == null)
            subLevel = Sable.HELPER.getContaining(this.level(), this.position());

        boolean sublevelExists
                = subLevel != null && !subLevel.isRemoved();
        if (!sublevelExists) return null;

        if (subLevelRef == null)
            this.SOE$setSubLevel(subLevel);

        return subLevel;
    }

    @Override
    public void SOE$setSubLevel(@NotNull SubLevel subLevel) {
        SOE$subLevelRef = new WeakReference<>(subLevel);
    }
}
