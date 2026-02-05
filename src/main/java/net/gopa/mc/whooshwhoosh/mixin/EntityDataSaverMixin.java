package net.gopa.mc.whooshwhoosh.mixin;

import net.gopa.mc.whooshwhoosh.enchantment.interfaces.Persistent;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.gopa.mc.whooshwhoosh.util.Const.getRootDataKey;

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
        this.data = nbt;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    private void onWriteNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if (data == null) return;
        nbt.put(getRootDataKey(), data);
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    private void onReadNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains(getRootDataKey())) {
            data = nbt.getCompound(getRootDataKey());
        }
    }
}
