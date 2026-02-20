package net.gopa.mc.whooshwhoosh.mixin.accessors;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Enchantment.class)
public interface EnchantmentAccessorMixin {
    @Accessor
    EquipmentSlot[] getSlotTypes();
}
