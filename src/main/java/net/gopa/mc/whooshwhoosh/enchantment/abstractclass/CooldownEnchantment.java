package net.gopa.mc.whooshwhoosh.enchantment.abstractclass;

import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Persistent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;

import static net.gopa.mc.whooshwhoosh.util.Const.getDataKey;
import static net.gopa.mc.whooshwhoosh.util.Const.getRootDataKey;

public abstract class CooldownEnchantment extends Enchantment {

    private final String COOLDOWN_KEY = getDataKey("cooldown", this);
    private static final String ROOT_KEY = getRootDataKey();

    protected CooldownEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot[] slotTypes) {
        super(weight, target, slotTypes);
    }

    abstract public int getMaxCooldown(int level);

    abstract public void executeCooldownEffect(LivingEntity user, LivingEntity target, int level);

    @Override
    public final void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (!(target instanceof LivingEntity livingTarget)) return;
        if (!canTrigger(user.getWorld(), livingTarget, getMaxCooldown(level))) return;

        executeCooldownEffect(user, livingTarget, level);
        persistCooldownData(user, livingTarget);
    }

    private boolean canTrigger(World world, LivingEntity target, int cooldown) {
        return hasDataCapability(target)
                && isCooldownExpired(world, target, cooldown);
    }

    private boolean hasDataCapability(Entity entity) {
        return entity instanceof Persistent;
    }

    private boolean isCooldownExpired(World world, LivingEntity target, int cooldown) {
        NbtCompound data = getDataSaver(target).getData();
        long lastTriggerTime = data.getLong(COOLDOWN_KEY);
        return world.getTime() - lastTriggerTime >= cooldown;
    }

    private void persistCooldownData(LivingEntity user, LivingEntity target) {
        Persistent dataSaver = getDataSaver(target);
        NbtCompound root = dataSaver.getData().getCompound(ROOT_KEY);
        root.putLong(COOLDOWN_KEY, user.getWorld().getTime());
        dataSaver.setData(root);
    }

    private Persistent getDataSaver(Entity entity) {
        return (Persistent) entity;
    }
}
