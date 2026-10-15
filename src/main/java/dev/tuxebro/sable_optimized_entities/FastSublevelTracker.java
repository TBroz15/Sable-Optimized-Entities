package dev.tuxebro.sable_optimized_entities;

import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

// i cant reload code inside of mixins, rare L hotswap
public class FastSublevelTracker {
    public static final int SUBLEVEL_AREA_CHUNK_DIST = 1_250_000;

    public static Vec3 trackEntity(Entity entity) {
        final Vec3 pos = entity.position();

        boolean isValid = checkEntityIsValidType(entity);
        if (!isValid) return pos;

        boolean isOnSublevel = fastCheckEntityIsOnSublevelArea(entity);
        if (!isOnSublevel) return pos;

        SubLevel subLevel = getSubLevel(entity);
        if (subLevel == null) return pos;

        // TODO: dear self, make a temporary pos cache for sublevels
        // TODO: that stays to still. invalidate it when it moves too fast.
        // TODO: check aero's code (velocity sensor) to see how it gets sublevels' delta movement idk

        return subLevel
                .logicalPose()
                .transformPosition(pos);
    }

    public static boolean checkEntityIsValidType(Entity entity) {
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
    public static SubLevel getSubLevel(Entity entity) {
        if (!(entity instanceof EntitySublevelAccessor accessor)) return null;
        return accessor.SOE$getSubLevel();
    }

    public static boolean fastCheckEntityIsOnSublevelArea(Entity instance) {
        var chunkPos = instance.chunkPosition();
        return Mth.abs(chunkPos.x) >= SUBLEVEL_AREA_CHUNK_DIST ||
                Mth.abs(chunkPos.z) >= SUBLEVEL_AREA_CHUNK_DIST;
    }
}
