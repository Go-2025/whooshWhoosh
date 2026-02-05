package net.gopa.mc.whooshwhoosh.event.impl;

import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Triggerable;
import net.gopa.mc.whooshwhoosh.event.api.EntityAttackEvent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;

public class EntityAttackListener implements EntityAttackEvent {

    public static final EntityAttackListener INSTANCE = new EntityAttackListener();

    private EntityAttackListener() {
    }

    @Override
    public ActionResult interact(LivingEntity target, LivingEntity attacker, DamageSource damageSource) {
        ItemStack stack = attacker.getMainHandStack();
        for (NbtElement value : stack.getEnchantments()) {
            NbtCompound nbt = (NbtCompound) value;
            Enchantment ench = Registries.ENCHANTMENT.get(new Identifier(nbt.getString("id")));
            if (ench instanceof Triggerable triggerableEnch) {
                int level = nbt.getInt("lvl");
                Triggerable proxy = Triggerable.CheckProxyFactory.createProxy(triggerableEnch, Triggerable.class);
                proxy.onTargetDamage(level, target, attacker, damageSource);
            }
        }
        return ActionResult.SUCCESS;
    }
}
