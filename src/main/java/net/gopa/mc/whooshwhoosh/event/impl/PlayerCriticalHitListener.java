package net.gopa.mc.whooshwhoosh.event.impl;

import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Triggerable;
import net.gopa.mc.whooshwhoosh.event.api.PlayerCriticalHitEvent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

public class PlayerCriticalHitListener implements PlayerCriticalHitEvent {

    public static final PlayerCriticalHitEvent INSTANCE = new PlayerCriticalHitListener();

    private PlayerCriticalHitListener() {
    }

    @Override
    public ActionResult interact(LivingEntity target, LivingEntity attacker) {
        for (NbtElement value : attacker.getMainHandStack().getEnchantments()) {
            NbtCompound nbt = (NbtCompound) value;
            Enchantment ench = Registries.ENCHANTMENT.get(new Identifier(nbt.getString("id")));

            if (ench instanceof Triggerable triggerableEnch) {
                int level = nbt.getInt("lvl");
                Triggerable proxy = Triggerable.CheckProxyFactory.createProxy(triggerableEnch, Triggerable.class);
                proxy.onCriticalHit(level, target, attacker);
            }
        }
        return ActionResult.SUCCESS;
    }
}
