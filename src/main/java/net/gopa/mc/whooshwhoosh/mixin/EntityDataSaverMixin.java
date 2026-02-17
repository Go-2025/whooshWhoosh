package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.WhooshwhooshMod;
import net.gopa.mc.whooshwhoosh.interfaces.Persistent;
import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Stored;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityDataSaverMixin implements Persistent {

    @Unique
    private NbtCompound data;

    @Override
    public NbtCompound getData() {
        if (data == null) {
            data = new NbtCompound();
        }
        return data;
    }

    @Override
    public void setData(NbtCompound nbt) {
        data = nbt;
        WhooshwhooshMod.LOGGER.info("Marked: {}", data);
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    private void onWriteNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if (data == null) return;
        nbt.put(Stored.ROOT_KEY, data);
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    private void onReadNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains(Stored.ROOT_KEY)) {
            data = nbt.getCompound(Stored.ROOT_KEY);
        }
    }
}
