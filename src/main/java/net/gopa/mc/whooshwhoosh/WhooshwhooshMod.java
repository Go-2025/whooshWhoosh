package net.gopa.mc.whooshwhoosh;

import net.fabricmc.api.ModInitializer;
import net.gopa.mc.whooshwhoosh.Handler.TriggerHandler;
import net.gopa.mc.whooshwhoosh.enums.TriggerPoint;
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
        EnchantmentsRegistry.registerAll();
        ListenerRegistry.registerAll();
        WhooshwhooshMod.LOGGER.info("Whoosh whoosh mod initialized");
    }
}
