package net.gopa.mc.whooshwhoosh;

import net.fabricmc.api.ModInitializer;
import net.gopa.mc.whooshwhoosh.registry.EnchantmentsRegistry;
import net.gopa.mc.whooshwhoosh.registry.ListenerRegistry;
import net.minecraft.util.math.random.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhooshwhooshMod implements ModInitializer {

    public static final String MOD_ID = "whooshwhoosh";

    public static final Logger LOGGER = LoggerFactory.getLogger("Whoosh Whoosh");

    public static final Random RANDOM = Random.create();

    @Override
    public void onInitialize() {
        long startTime = System.nanoTime();
        EnchantmentsRegistry.registerAll();
        ListenerRegistry.registerAll();
        WhooshwhooshMod.LOGGER.info("Whoosh whoosh mod initialized");
        long duration = (System.nanoTime() - startTime) / 1_000_000;
        LOGGER.info("[{}] 初始化完成! 耗时: {}ms", MOD_ID, duration);
    }
}
