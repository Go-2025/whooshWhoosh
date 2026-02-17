package net.gopa.mc.whooshwhoosh.registry;

import net.gopa.mc.whooshwhoosh.event.api.*;
import net.gopa.mc.whooshwhoosh.event.impl.*;

public class ListenerRegistry {

    public static void registerAll() {
        // item
        ItemBreakEvent.EVENT.register(ItemBreakListener.INSTANCE);
        ItemDamageEvent.EVENT.register(ItemDamageListener.INSTANCE);
        // entity
        EntityDamageEvent.EVENT.register(EntityDamageListener.INSTANCE);
        PlayerCriticalHitEvent.EVENT.register(PlayerCriticalHitListener.INSTANCE);
        ArrowHitEvent.EVENT.register(ArrowHitListener.INSTANCE);
        EntityJumpEvent.EVENT.register(EntityJumpListener.INSTANCE);
    }
}
