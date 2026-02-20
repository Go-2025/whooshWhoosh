package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.toolkit.dataTool.DataSaver;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BowItem.class)
public abstract class BowItemMixin {

    private static final String key1 = "bow_pull_start";
    private static final String key2 = "last_bow_pull_progress";

    @Inject(method = "use", at = @At("HEAD"))
    private void onUseStart(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (!world.isClient) {
            ItemStack stack = user.getStackInHand(hand);
            if (!stack.isEmpty() && stack.getItem() instanceof BowItem) {
                DataSaver saver = DataSaver.of(user);
                saver.putLong(key1, world.getTime()).save();
            }
        }
    }

    @Inject(method = "onStoppedUsing", at = @At("HEAD"))
    private void onUseEnd(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo ci) {
        if (stack.getItem() instanceof BowItem) {
            DataSaver saver = DataSaver.of(user);
            NbtCompound data = saver.getData();
            long startTime = data.getLong(key1);
            long durationTicks = world.getTime() - startTime;
            float progress = Math.min(1.0f, durationTicks / 20.0f);
            saver
                    .remove(key1)
                    .putFloat(key2, progress)
                    .save();
        }
    }
}
