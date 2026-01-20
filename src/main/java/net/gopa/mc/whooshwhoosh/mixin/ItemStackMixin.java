package net.gopa.mc.whooshwhoosh.mixin;

import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.gopa.mc.whooshwhoosh.registry.EnchantmentsRegistry;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Unique
    private static final EnchantmentsRegistry OFFERING_ENCH_REG = EnchantmentsRegistry.OFFERING;

    @Unique
    private static boolean hasEnchantment(NbtList enchNbtList, EnchantmentsRegistry targetEnchant) {
        if (enchNbtList == null || enchNbtList.isEmpty()) return false;

        return enchNbtList.stream()
                .anyMatch(nbt -> {
                    if (nbt instanceof NbtCompound compound) {
                        String id = compound.getString("id");
                        return id.equals(WhooshwhooshMod.MOD_ID + ":" + targetEnchant.getId());
                    }
                    return false;
                });
    }

    @Inject(
            method = "damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onDamage(
            int originalDamage, LivingEntity entity, Consumer<LivingEntity> breakCallback, CallbackInfo ci
    ) {
        ItemStack stack = (ItemStack) (Object) this;
        PlayerEntity player = (PlayerEntity) entity;

        if (player.isCreative()) return;
        if (!hasEnchantment(stack.getEnchantments(), OFFERING_ENCH_REG)) return;

        Random random = WhooshwhooshMod.RANDOM;

        int maxDamage = stack.getMaxDamage();
        int currDamage = stack.getDamage();

        Float2IntFunction getIntDamage = (float n) -> (int) Math.max(2f, maxDamage * n);
        int minExtraDamage = getIntDamage.get(0.1f);
        int maxExtraDamage = getIntDamage.get(0.18f);

        int extraDamage = random.nextInt(maxExtraDamage - minExtraDamage) + minExtraDamage;

        stack.setDamage(Math.min(currDamage + extraDamage, maxDamage));

        if (currDamage < maxDamage) ci.cancel();
        else {
            World world = entity.getWorld();
            if (world.isClient) return;

            Vec3d playerPos = player.getPos();
            int n = Math.min(stack.getEnchantments().size(), 200);
            for (int i = 0; i < n; i++)
                world.spawnEntity(new ExperienceOrbEntity(world,
                        playerPos.x, playerPos.y, playerPos.z, random.nextInt(3) + 4));
        }
    }
}
