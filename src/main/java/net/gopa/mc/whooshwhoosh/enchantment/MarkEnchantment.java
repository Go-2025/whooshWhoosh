package net.gopa.mc.whooshwhoosh.enchantment;

import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Stored;
import net.gopa.mc.whooshwhoosh.toolkit.dataTool.DataSaver;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.Trigger;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.TriggerPoint;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.Triggerable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

@Trigger(TriggerPoint.ON_ARROW_HIT)
public class MarkEnchantment extends Enchantment implements Stored, Triggerable {

    public static final String KEY1 = "mark_id";     // 存储于攻击者实体中的上次标记的实体id
    public static final String KEY2 = "mark_level";  // 存储于被标记者的等级

    public MarkEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.BOW, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public ActionResult onArrowHit(int level, LivingEntity target, ArrowEntity arrow) {
        if (canTrigger(level)) {
            Entity attacker = arrow.getOwner();
            if (attacker != null) {
                DataSaver attSaver = getDataSaver(attacker);
                NbtCompound attData = attSaver.getData();
                DataSaver tarSaver = getDataSaver(target);
                NbtCompound tarData = tarSaver.getData();

                World world = attacker.getWorld();
                int lstId = attData.getInt(KEY1);

                if (!tarData.contains(KEY2)) {
                    if (world.getEntityById(lstId) instanceof LivingEntity lstTarget) {
                        DataSaver lstSaver = getDataSaver(lstTarget);
//                        WhooshwhooshMod.LOGGER.info("MarkEnchMixin.onArrowHit: {}/{}", attData, tarSaver.getData());
                        lstTarget.removeStatusEffect(StatusEffects.GLOWING);
                        lstSaver.remove(KEY2).save();
                    }
                    target.addStatusEffect(new StatusEffectInstance(
                            StatusEffects.GLOWING, 30 * 20, level - 1));
                    tarSaver.putInt(KEY2, level).save();
                    attSaver.putInt(KEY1, target.getId()).save();
                }
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof CrossbowItem;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
}
