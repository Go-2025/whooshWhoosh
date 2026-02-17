package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.event.api.EntityDamageEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LivingEntity.class, PlayerEntity.class})
public abstract class EntityDamageMixin {

    @Inject(method = "damage", at = @At("TAIL"), cancellable = true)
    private void onAttackStart(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity entity = source.getSource();
        LivingEntity attacker =
                entity instanceof LivingEntity
                ? (LivingEntity) entity
                : entity instanceof ArrowEntity arrowEntity
                ? (LivingEntity) arrowEntity.getOwner()
                : null;
        if (attacker == null || attacker.getWorld().isClient()) return;
        ActionResult result = EntityDamageEvent.EVENT.invoker().interact((LivingEntity) (Object) this, attacker, source);
        if (result != ActionResult.PASS) cir.setReturnValue(result.isAccepted());
    }
}
