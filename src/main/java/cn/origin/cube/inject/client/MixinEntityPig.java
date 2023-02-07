package cn.origin.cube.inject.client;

import cn.origin.cube.Cube;
import cn.origin.cube.core.events.world.EntityControlEvent;
import net.minecraft.entity.passive.EntityPig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPig.class)
public class MixinEntityPig {
    @Inject(method = "canBeSteered", at = @At("HEAD"), cancellable = true)
    public void canBeSteeredHook(CallbackInfoReturnable<Boolean> cir) {
        EntityControlEvent event = new EntityControlEvent();
        Cube.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "getSaddled", at = @At("HEAD"), cancellable = true)
    public void getSaddledHook(CallbackInfoReturnable<Boolean> cir) {
        EntityControlEvent event = new EntityControlEvent();
        Cube.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            cir.setReturnValue(true);
        }
    }
}
