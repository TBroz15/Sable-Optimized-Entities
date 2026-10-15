package dev.tuxebro.sable_optimized_entities;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

// i cant reload code inside of mixins, rare L hotswap
public class FastSublevelTracker {
    public static final int SUBLEVEL_AREA_CHUNK_DIST = 1_250_000;

    public static Vec3 trackEntity(Entity entity) {
        final Vec3 pos = entity.position();

        boolean isValid = checkEntityValidType(entity);
        if (!isValid) return pos;

        boolean isOnSublevel = fastCheckEntityIsOnSublevelArea(entity);
        if (!isOnSublevel) return pos;

        SubLevel subLevel = getSubLevel(entity, pos);
        if (subLevel == null) return pos;

        // TODO: dear self, make a temporary pos cache for sublevels
        // TODO: that stays to still. invalidate it when it moves too fast.
        // TODO: check aero's code (velocity sensor) to see how it gets sublevels' delta movement idk

        return subLevel
                .logicalPose()
                .transformPosition(pos);
    }

    public static boolean checkEntityValidType(Entity entity) {
        switch (entity) {
            case Mob ignored -> {
                return true;
            }
            case Player ignored -> {
                return true;
            }
            case ItemEntity ignored -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    // https://tenor.com/view/ishowspeed-translate-gif-12052961640477791272
    @Nullable
    public static SubLevel getSubLevel(Entity entity, Vec3 pos) {
        Level level = entity.level();
        SubLevelContainer container = SubLevelContainer.getContainer(level);
        if (container == null) return null;

        if (!(entity instanceof EntitySublevelUUIDExtension ext)) return null;

        SubLevel subLevel;
        UUID sublevelUUID = ext.SOE$getSubLevelUUID();

        if (sublevelUUID == null)
            subLevel = Sable.HELPER.getContaining(level, pos);
        else
            subLevel = container.getSubLevel(sublevelUUID);

        if (subLevel == null) return null;
        if (subLevel.isRemoved()) return null;

        if (sublevelUUID == null) {
            sublevelUUID = subLevel.getUniqueId();
            ext.SOE$setSubLevelUUID(sublevelUUID);
        }

        return subLevel;
    }

    public static boolean fastCheckEntityIsOnSublevelArea(Entity instance) {
        var chunkPos = instance.chunkPosition();
        return Mth.abs(chunkPos.x) >= SUBLEVEL_AREA_CHUNK_DIST ||
                Mth.abs(chunkPos.z) >= SUBLEVEL_AREA_CHUNK_DIST;
    }
}
