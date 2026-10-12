package dev.tuxebro.sable_optimized_entities;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import com.google.common.collect.MapMaker;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

// i cant reload code inside of mixins, rare L hotswap
@EventBusSubscriber
public class FastSublevelTracker {
    public static final int SUBLEVEL_AREA_CHUNK_DIST = 1_250_000;
    public static final int MAX_CYCLE_CACHE_POS_CLEAR = 100;

    private static int cyclesCachePosClear = 0;
    private static final ConcurrentMap<UUID, Vec3> cachedPos = new MapMaker()
            .weakValues()
            .makeMap();

    public static Vec3 trackEntity(Entity entity) {
        final Vec3 pos = entity.position();

        switch (entity) {
            case Mob ignored -> {
                return pos;
            }
            case Player ignored -> {
                return pos;
            }
            case ItemEntity ignored -> {
                return pos;
            }
            default -> {
            }
        }

        boolean isOnSublevel = fastCheckEntityIsOnSublevelArea(entity);
        if (!isOnSublevel) return pos;

        Vec3 transformedPos = cachedPos.get(entity.getUUID());

        boolean canReturnCachedPos = transformedPos != null
                && cachedPos.containsKey(entity.getUUID());
        if (canReturnCachedPos) return transformedPos;

        final Level level = entity.level();
        final SubLevel subLevel = Sable.HELPER.getContaining(level, pos);

        if (subLevel == null) return pos;
        if (subLevel.isRemoved()) return pos;

        transformedPos = subLevel
                .logicalPose()
                .transformPosition(pos);

        cachedPos.put(entity.getUUID(), transformedPos);
        return transformedPos;
    }

    @SubscribeEvent
    public static void onTick(ServerTickEvent.Pre event) {
        cyclesCachePosClear++;
        if (cyclesCachePosClear < MAX_CYCLE_CACHE_POS_CLEAR) return;

        cyclesCachePosClear = 0;
        cachedPos.clear();
    }

    public static boolean fastCheckEntityIsOnSublevelArea(Entity instance) {
        var chunkPos = instance.chunkPosition();
        return Mth.abs(chunkPos.x) >= SUBLEVEL_AREA_CHUNK_DIST ||
                Mth.abs(chunkPos.z) >= SUBLEVEL_AREA_CHUNK_DIST;
    }
}
