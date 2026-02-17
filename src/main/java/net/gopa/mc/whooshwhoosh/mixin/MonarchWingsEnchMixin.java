package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.enchantment.MonarchWingsEnchantment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.gopa.mc.whooshwhoosh.util.EnchantmentUtil.processEnch;

@Mixin(PlayerEntity.class)
public abstract class MonarchWingsEnchMixin {

//    @Inject(method = "tick", at = @At("HEAD"))
//    private void onTick(CallbackInfo ci) {
//        PlayerEntity self = (PlayerEntity) (Object) this;
//        MinecraftClient client = MinecraftClient.getInstance();
//        if (client.world != null) {
//            if (!(client.world.getEntityById(self.getId()) instanceof ClientPlayerEntity clientP)) return;
//            if (clientP.input.jumping) {
//                processEnch(self.getEquippedStack(EquipmentSlot.CHEST), (ench, lvl) -> {
//                    if (ench instanceof MonarchWingsEnchantment monarchWingsEnchantment) {
//                        monarchWingsEnchantment.onClickJumpKey(lvl, clientP
//                        );
//                    }
//                });
//            }
//        }
//    }
}
