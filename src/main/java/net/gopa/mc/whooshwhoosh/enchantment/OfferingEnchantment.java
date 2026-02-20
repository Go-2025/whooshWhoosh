package net.gopa.mc.whooshwhoosh.enchantment;

import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.gopa.mc.whooshwhoosh.enchantment.annotation.Trigger;
import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Triggerable;
import net.gopa.mc.whooshwhoosh.enums.TriggerPoint;
import net.minecraft.enchantment.VanishingCurseEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.function.Consumer;

@Trigger(TriggerPoint.ON_ITEM_BREAK)
public class OfferingEnchantment extends VanishingCurseEnchantment implements Triggerable {
    public OfferingEnchantment() {
        super(Rarity.COMMON, EquipmentSlot.values());
    }

    @Override
    public ActionResult onItemBreak(int level, LivingEntity source, ItemStack stack, Consumer<LivingEntity> breakCallback) {
        World world = source.getWorld();
        Vec3d basePos = source.getPos();
        int n = Math.min(stack.getEnchantments().size(), 200);

        for (int i = 0; i < n; i++) {
            int expAmount = WhooshwhooshMod.RANDOM.nextInt(3) + 4;
            world.spawnEntity(new ExperienceOrbEntity(world,
                    basePos.x, basePos.y, basePos.z, expAmount));
        }
        return ActionResult.PASS;
    }
}
