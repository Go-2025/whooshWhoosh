package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.enchantment.MarkEnchantment;
import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Stored;
import net.gopa.mc.whooshwhoosh.registry.EnchantmentsRegistry;
import net.gopa.mc.whooshwhoosh.toolkit.dataTool.DataSaver;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.hit.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class MarkEnchMixin {

    @Inject(method = "onEntityHit", at = @At(value = "HEAD"), order = 0)
    private void onHit(EntityHitResult entityHitResult, CallbackInfo ci) {
        if ((Object) this instanceof ArrowEntity arrow) {
            if (entityHitResult.getEntity() instanceof LivingEntity target) {
                Stored stored = (Stored) EnchantmentsRegistry.MARK.get();
                DataSaver saver = stored.getDataSaver(target);
                NbtCompound data = saver.getData();

                String key = MarkEnchantment.KEY2;
                if (data.contains(key)) {
                    if (target.hasStatusEffect(StatusEffects.GLOWING)) {
                        int lvl = data.getInt(key);

                        arrow.setDamage(arrow.getDamage() * (lvl + 5) / 4);
                        target.removeStatusEffect(StatusEffects.GLOWING);
                    }
                    saver.remove(key).save();
                }
            }
        }
    }
}
