package net.gopa.mc.whooshwhoosh.registry;

import net.gopa.mc.whooshwhoosh.Handler.EnchTriggerHandler;
import net.gopa.mc.whooshwhoosh.event.api.*;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.TriggerPoint;

import java.util.Collections;

public class ListenerRegistry {

    public static void registerAll() {
        // item
        ItemBreakEvent.EVENT.register((stack, source, breakCallback) ->
                new EnchTriggerHandler(TriggerPoint.ON_ITEM_BREAK)
                        .handleEntity(source, (t, l) -> t.onItemBreak(l, source, stack, breakCallback))
                        .result());
        ItemDamageEvent.EVENT.register((stack, amount, source, breakCallback) ->
                new EnchTriggerHandler(TriggerPoint.ON_ITEM_DAMAGE)
                        .handleEntity(source, (t, l) -> t.onItemDamage(l, source, stack, amount, breakCallback))
                        .result());
        ItemEnchantEvent.EVENT.register((stack, enchantments) ->
                new EnchTriggerHandler(TriggerPoint.ON_ITEM_ENCHANT)
                        .handleStack(Collections.singletonList(stack), (t, nbt) -> t.onItemEnch(nbt.getInt("lvl"), stack, nbt, enchantments))
                        .result());

        // entity or living entity
        EntityAttackEvent.EVENT.register((target, source) ->
                new EnchTriggerHandler(TriggerPoint.ON_ATTACK)
                        .handleEntity(source, (t, l) -> t.onAttack(l, source, target))
                        .result());
        EntityDamageEvent.EVENT.register((source, attacker, damageSource) ->
                new EnchTriggerHandler(TriggerPoint.ON_ENTITY_DAMAGE)
                        .handleEntity(source, (t, l) -> t.onTargetDamage(l, source, attacker, damageSource))
                        .result());
        EntityJumpEvent.EVENT.register((source) ->
                new EnchTriggerHandler(TriggerPoint.ON_JUMP)
                        .handleEntity(source, (t, l) -> t.onEntityJump(l, source))
                        .result());

        // player
        PlayerCriticalHitEvent.EVENT.register((target, source) ->
                new EnchTriggerHandler(TriggerPoint.ON_CRITICAL_HIT)
                        .handleEntity(source, (t, l) -> t.onCriticalHit(l, source, target))
                        .result());

        // projectile
        ArrowHitEvent.EVENT.register((arrow, target) ->
                new EnchTriggerHandler(TriggerPoint.ON_ARROW_HIT)
                        .handleEntity(arrow.getOwner(), (t, l) -> t.onArrowHit(l, target, arrow))
                        .result());
    }
}
