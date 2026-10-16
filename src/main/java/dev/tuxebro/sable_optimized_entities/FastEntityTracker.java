package dev.tuxebro.sable_optimized_entities;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3dc;

import java.util.concurrent.TimeUnit;

// i cant reload code inside of mixins, rare L hotswap
public class FastEntityTracker {
    private static final Cache<Entity, Vec3> entityStaticVecCache = CacheBuilder.newBuilder()
            .weakKeys()
            .maximumSize(2000)
            .expireAfterWrite(10, TimeUnit.SECONDS)
            .build();

    private static final Cache<SubLevel, Boolean> movingSublevels = CacheBuilder.newBuilder()
            .weakKeys()
            .maximumSize(500)
            .expireAfterWrite(3, TimeUnit.SECONDS)
            .build();

    public static Vec3 track(Entity entity) {
        final Vec3 pos = entity.position();

        if (!isEntityValid(entity)) return pos;
        if (!isOnSublevel(entity)) return pos;

        SubLevel subLevel = getSubLevel(entity);
        if (subLevel == null) return pos;

        if (isMovingFast(subLevel)) {
            entityStaticVecCache.invalidate(entity);
            return toGlobalPos(subLevel, pos);
        }

        return getStaticPos(subLevel, entity, pos);
    }



    private static Vec3 toGlobalPos(SubLevel subLevel, Vec3 pos) {
        return subLevel
                .logicalPose()
                .transformPosition(pos);
    }

    private static Vec3 getStaticPos(SubLevel subLevel, Entity entity, Vec3 pos) {
        Vec3 globalPos = entityStaticVecCache.getIfPresent(entity);
        if (globalPos != null)
            return globalPos;

        globalPos = toGlobalPos(subLevel, pos);
        entityStaticVecCache.put(entity, globalPos);

        return globalPos;
    }

    // had to put a cheaper and somewhat inaccurate calc
    private static boolean isMovingFast(SubLevel subLevel) {
        final double SPEED_THRESHOLD = 0.01D;

        Boolean isMovingFastObj = movingSublevels.getIfPresent(subLevel);
        if (isMovingFastObj != null)
            return isMovingFastObj;

        Vector3dc lastPos = subLevel
                .lastPose()
                .position();

        double diffSpeed = subLevel
                .logicalPose()
                .position()
                .distance(lastPos);

        boolean isMovingFast = diffSpeed >= SPEED_THRESHOLD;
        movingSublevels.put(subLevel, isMovingFast);

        return isMovingFast;
    }

    private static boolean isEntityValid(Entity entity) {
        switch (entity) {
            case Mob ignored -> {
                return false;
            }
            case Player ignored -> {
                return false;
            }
            case ItemEntity ignored -> {
                return false;
            }
            default -> {
                return true;
            }
        }
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
