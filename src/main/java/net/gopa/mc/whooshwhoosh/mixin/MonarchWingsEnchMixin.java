package net.gopa.mc.whooshwhoosh.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;

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
