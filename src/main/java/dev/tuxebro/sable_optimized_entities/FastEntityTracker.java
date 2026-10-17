package dev.tuxebro.sable_optimized_entities;

import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class FastEntityTracker {
    public static Vec3 track(Entity entity) {
        final Vec3 pos = entity.position();

        if (!isOnSublevel(entity)) return pos;

        SubLevel subLevel = getSubLevel(entity);
        if (subLevel == null) return pos;

        return subLevel
                .logicalPose()
                .transformPosition(pos);
    }

    @Nullable
    private static SubLevel getSubLevel(Entity entity) {
        if (!(entity instanceof EntitySublevelAccessor accessor)) return null;
        return accessor.SOE$getSubLevel();
    }

    private static boolean isOnSublevel(Entity instance) {
        final int SUBLEVEL_AREA_CHUNK_DIST = 1_250_000;

        var chunkPos = instance.chunkPosition();
        return Mth.abs(chunkPos.x) >= SUBLEVEL_AREA_CHUNK_DIST ||
                Mth.abs(chunkPos.z) >= SUBLEVEL_AREA_CHUNK_DIST;
    }
}
