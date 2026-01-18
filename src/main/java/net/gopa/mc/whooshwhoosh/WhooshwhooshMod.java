package net.gopa.mc.whooshwhoosh;

import net.fabricmc.api.ModInitializer;
import net.gopa.mc.whooshwhoosh.registry.EnchantmentsRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhooshwhooshMod implements ModInitializer {

    public static final String MOD_ID = "whooshwhoosh";

    public static final Logger LOGGER = LoggerFactory.getLogger("Whoosh Whoosh");

    @Override
    public void onInitialize() {
        EnchantmentsRegistry.registerAll();
    }
}
