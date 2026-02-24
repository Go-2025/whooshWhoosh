package net.gopa.mc.whooshwhoosh.enchantment;

import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Stored;
import net.gopa.mc.whooshwhoosh.toolkit.dataTool.DataSaver;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.Trigger;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.TriggerPoint;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.Triggerable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;

@Trigger({TriggerPoint.ON_ATTACK})
public class BashEnchantment extends Enchantment implements Triggerable, Stored {

    private static final String KEY = "lvl";
    private static final String KEY2 = "time_stamp";

    public BashEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public ActionResult onAttack(int level, LivingEntity source, LivingEntity target) {
        if (canTrigger(level) && !source.isOnGround() && !target.isOnGround()) {
            target.addVelocity(0, -5f, 0);

            getDataSaver(target)
                    .putInt(KEY, level)
                    .putLong(KEY2, source.getWorld().getTime())
                    .save();
        }
        return ActionResult.PASS;
    }

    public ActionResult onLand(LivingEntity source, float fallDistance, DamageSource damageSource) {
        DataSaver dataSaver = getDataSaver(source);
        NbtCompound data = dataSaver.getData();

        if (data.contains(KEY)) {
            int level = data.getInt(KEY);
            source.damage(damageSource, getDamage(data.getLong(KEY2), source.getWorld().getTime(), fallDistance, level));

            dataSaver.remove(KEY).remove(KEY2).save();
        }
        return ActionResult.PASS;
    }

    private int getDamage(long startTime, long nowTime, float fallDistance, int level) {
        return ((int) (fallDistance / (nowTime - startTime)) + level * 2);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof AxeItem;
    }
}
