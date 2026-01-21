package net.gopa.mc.whooshwhoosh.mixin;

import it.unimi.dsi.fastutil.floats.Float2IntFunction;
import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.gopa.mc.whooshwhoosh.registry.EnchantmentsRegistry;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Unique
    private static final EnchantmentsRegistry OFFERING_ENCH_REG = EnchantmentsRegistry.OFFERING;
    @Unique
    private static final Random RANDOM = WhooshwhooshMod.RANDOM;

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

    @Unique
    private static void increaseDamageOnOfferingEnchItem(ItemStack stack, int currDamage, int maxDamage) {
        Float2IntFunction getIntDamage = (float n) -> (int) Math.max(2f, maxDamage * n);
        int minExtraDamage = getIntDamage.get(0.1f);
        int maxExtraDamage = getIntDamage.get(0.18f);
        int extraDamage = RANDOM.nextInt(maxExtraDamage - minExtraDamage) + minExtraDamage;

        stack.setDamage(Math.min(currDamage + extraDamage, maxDamage));
    }

    @Unique
    private static void makeOfferingEnchItemDropExpOrd(ItemStack stack, PlayerEntity player) {
        World world = player.getWorld();
        if (world.isClient) return;

        Vec3d playerPos = player.getPos();
        int n = Math.min(stack.getEnchantments().size(), 200);
        for (int i = 0; i < n; i++)
            world.spawnEntity(new ExperienceOrbEntity(world,
                    playerPos.x, playerPos.y, playerPos.z, RANDOM.nextInt(3) + 4));
    }

    @Inject(
            method = "damage(ILnet/minecraft/util/math/random/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void onDamage(
            int amount, Random rand, ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir
    ) {
        ItemStack stack = (ItemStack) (Object) this;

        if (player.isCreative()) return;
        if (!hasEnchantment(stack.getEnchantments(), OFFERING_ENCH_REG)) return;

        int maxDamage = stack.getMaxDamage();
        int currDamage = stack.getDamage();

        increaseDamageOnOfferingEnchItem(stack, currDamage, maxDamage);

        if (currDamage < maxDamage) cir.cancel();
        else makeOfferingEnchItemDropExpOrd(stack, player);
    }
}
