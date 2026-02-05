package net.gopa.mc.whooshwhoosh.registry;

import net.gopa.mc.whooshwhoosh.event.api.EntityAttackEvent;
import net.gopa.mc.whooshwhoosh.event.api.ItemBreakEvent;
import net.gopa.mc.whooshwhoosh.event.api.ItemDamageEvent;
import net.gopa.mc.whooshwhoosh.event.api.PlayerCriticalHitEvent;
import net.gopa.mc.whooshwhoosh.event.impl.EntityAttackListener;
import net.gopa.mc.whooshwhoosh.event.impl.ItemBreakListener;
import net.gopa.mc.whooshwhoosh.event.impl.ItemDamageListener;
import net.gopa.mc.whooshwhoosh.event.impl.PlayerCriticalHitListener;

public class ListenerRegistry {

    public static void registerAll() {
        // item
        ItemBreakEvent.EVENT.register(ItemBreakListener.INSTANCE);
        ItemDamageEvent.EVENT.register(ItemDamageListener.INSTANCE);
        // entity
        EntityAttackEvent.EVENT.register(EntityAttackListener.INSTANCE);
        PlayerCriticalHitEvent.EVENT.register(PlayerCriticalHitListener.INSTANCE);
    }
}
