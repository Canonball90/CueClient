package cn.origin.cube.inject.client;

import cn.origin.cube.Cube;
import cn.origin.cube.core.events.world.EntityControlEvent;
import net.minecraft.entity.passive.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorse.class)
public class MixinAbstractHorse {
    @Inject(method = "canBeSteered", at = @At("HEAD"), cancellable = true)
    public void canBeSteeredHook(CallbackInfoReturnable<Boolean> cir) {
        EntityControlEvent event = new EntityControlEvent();
        Cube.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "isHorseSaddled", at = @At("HEAD"), cancellable = true)
    public void isHorseSaddledHook(CallbackInfoReturnable<Boolean> cir) {
        EntityControlEvent event = new EntityControlEvent();
        Cube.EVENT_BUS.post(event);
        if (event.isCanceled()) {
            cir.setReturnValue(true);
        }
    }
}
