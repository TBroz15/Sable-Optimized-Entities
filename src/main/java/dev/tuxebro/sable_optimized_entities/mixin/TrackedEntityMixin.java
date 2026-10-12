package dev.tuxebro.sable_optimized_entities.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.tuxebro.sable_optimized_entities.FastSublevelTracker;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// completely override sable's redirect mixin
@Mixin(targets = "net.minecraft.server.level.ChunkMap$TrackedEntity", priority = 500)
public class TrackedEntityMixin {
    @WrapOperation(method = "updatePlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;position()Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 trackSubLevelEntities(Entity instance, Operation<Vec3> original) {
        return FastSublevelTracker.trackEntity(instance);
    }
}
