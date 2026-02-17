package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.gopa.mc.whooshwhoosh.enchantment.VaultEnchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.gopa.mc.whooshwhoosh.util.EnchantmentUtil.processEnch;

@Mixin({ServerPlayerEntity.class})
public abstract class VaultEnchMixin {

    @Unique
    private boolean lastSneakState = false;

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        boolean currentSneak = self.isSneaking();

        if (!currentSneak && lastSneakState) {
            WhooshwhooshMod.LOGGER.info("VaultEnchMixin: onTick");
            ActionResult result = processEnch(self.getEquippedStack(EquipmentSlot.FEET), (ench, lvl) -> {
                if (ench instanceof VaultEnchantment vaultEnch) {
                     vaultEnch.onOffSneak(lvl, self);
                }
                return ActionResult.PASS;
            });
        }
        lastSneakState = currentSneak;
    }
}
