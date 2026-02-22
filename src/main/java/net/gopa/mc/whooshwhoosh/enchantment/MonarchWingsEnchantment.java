package net.gopa.mc.whooshwhoosh.enchantment;

import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Stored;
import net.gopa.mc.whooshwhoosh.toolkit.dataTool.DataSaver;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.Trigger;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.TriggerPoint;
import net.gopa.mc.whooshwhoosh.toolkit.trigger.Triggerable;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ElytraItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;

@Trigger(TriggerPoint.ON_ENTITY_DAMAGE)
public class MonarchWingsEnchantment extends Enchantment implements Stored, Triggerable {

    private static final String KEY = "jump_count";

    public MonarchWingsEnchantment() {
        super(Rarity.COMMON, EnchantmentTarget.WEARABLE, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public ActionResult onTargetDamage(int level, LivingEntity source, Entity attacker, DamageSource damageSource) {
        if (canTrigger(level)) {
            ClientPlayerEntity p = MinecraftClient.getInstance().player;
            if (p != null) {
                PlayerEntity player = p.getWorld().getPlayerByUuid(attacker.getUuid());
                DataSaver saver = getDataSaver(attacker);
                attacker.addVelocity(0, Math.abs(attacker.getVelocity().y) + 0.5, 0);
                saver.putInt(KEY, 0).save();
            }
        }
        return ActionResult.PASS;
    }

    public void onClickJumpKey(int level, PlayerEntity player) {
        if (!canTrigger(level)) return;
        DataSaver saver = getDataSaver(player);
        if (player.isFallFlying() && !player.isOnGround()) {
            if (getData(player).getInt(KEY) > level) return;
            player.addVelocity(0, 0.3, 0);
            saver.putInt(KEY, saver.getData().getInt(KEY) + 1).save();
        } else {
            saver.putInt(KEY, 0).save();
        }
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof ElytraItem;
    }
}
