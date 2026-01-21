package net.gopa.mc.whooshwhoosh.mixin;

import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.gopa.mc.whooshwhoosh.registry.EnchantmentsRegistry;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

import static net.gopa.mc.whooshwhoosh.util.EnchantmentUtil.hasEnchantment;

@Mixin(ItemStack.class)
public class OfferingEnchMixin {

    @Unique
    private static final Enchantment OFFERING_ENCH = EnchantmentsRegistry.OFFERING.get();
    @Unique
    private static final Random RANDOM = WhooshwhooshMod.RANDOM;

    @ModifyVariable(
            method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V",
            at = @At("HEAD"),
            argsOnly = true
    )
    private int addRandToDamageWhenWornOut(int amount) {
        ItemStack stack = (ItemStack) (Object) this;

        if (!hasEnchantment(stack.getEnchantments(), OFFERING_ENCH)) return amount;

        int maxDamage = stack.getMaxDamage();
        Float2IntFunction getIntDamage = (float n) -> (int) Math.max(2f, maxDamage * n);
        int minExtraDamage = getIntDamage.get(0.12f);
        int maxExtraDamage = getIntDamage.get(0.20f);

        return amount * RANDOM.nextInt(maxExtraDamage - minExtraDamage) + minExtraDamage;
    }

    @Inject(
            method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V")
    )
    public void dropExpOrdWhenItemBreak(int amount, LivingEntity entity, Consumer<LivingEntity> breakCallback, CallbackInfo ci) {
        ItemStack stack = (ItemStack) (Object) this;
        PlayerEntity player = (PlayerEntity) entity;
        World world = player.getWorld();

        if (world.isClient) return;

        Vec3d playerPos = player.getPos();
        int n = Math.min(stack.getEnchantments().size(), 200);

        for (int i = 0; i < n; i++)
            world.spawnEntity(new ExperienceOrbEntity(world,
                    playerPos.x, playerPos.y, playerPos.z, RANDOM.nextInt(3) + 4));
    }
}
