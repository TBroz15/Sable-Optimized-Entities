package dev.tuxebro.sable_optimized_entities;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(SableOptimizedEntities.MOD_ID)
public class SableOptimizedEntities {
    public static final String MOD_ID = "sable_optimized_entities";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SableOptimizedEntities(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("wassup dude, im fixing a part of sable's unoptimized code lol");
    }
}
