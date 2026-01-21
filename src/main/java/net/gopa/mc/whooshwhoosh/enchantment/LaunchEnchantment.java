package net.gopa.mc.whooshwhoosh.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.math.Vec3d;

public class LaunchEnchantment extends Enchantment {
    public LaunchEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentTarget.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (!(target instanceof LivingEntity attacker)) return;

        Vec3d direction = attacker.getVelocity();

        float strength = 1f + level * 0.2f;
//        WhooshwhooshMod.LOGGER.info("{}/{}", (direction.y), strength);

        attacker.setVelocity(
                direction.x,
                Math.min(Math.abs(direction.y), 0.4f + level * 0.1f) * strength,
                direction.z
        );
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof SwordItem;
    }

    @Override
    public int getMinPower(int level) {
        return 4 + level * 12;
    }

    @Override
    public int getMaxPower(int level) {
        return getMinPower(level) + 36;
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }
}
