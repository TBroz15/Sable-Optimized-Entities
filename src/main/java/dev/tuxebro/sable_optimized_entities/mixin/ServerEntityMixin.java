package dev.tuxebro.sable_optimized_entities.mixin;

import dev.tuxebro.sable_optimized_entities.EntitySublevelUUIDExtension;
import net.minecraft.server.level.ServerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.UUID;

@Mixin(ServerEntity.class)
public class ServerEntityMixin implements EntitySublevelUUIDExtension {
    @Unique
    private UUID SOE$subLevelUUID = null;

    @Override
    @Nullable
    public UUID SOE$getSubLevelUUID() {
        return SOE$subLevelUUID;
    }

    @Override
    public void SOE$setSubLevelUUID(@NotNull UUID uuid) {
        SOE$subLevelUUID = uuid;
    }
}
