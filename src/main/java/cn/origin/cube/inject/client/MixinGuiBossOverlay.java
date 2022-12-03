package cn.origin.cube.inject.client;

import cn.origin.cube.event.events.render.BossOverlayEvent;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("unused")
@Mixin(GuiBossOverlay.class)
public class MixinGuiBossOverlay {

    @Inject(method = "renderBossHealth", at = @At("HEAD"), cancellable = true)
    private void renderBossHealth(CallbackInfo info) {
        BossOverlayEvent bossOverlayEvent = new BossOverlayEvent();
        MinecraftForge.EVENT_BUS.post(bossOverlayEvent);

        if (bossOverlayEvent.isCanceled()) {
            info.cancel();
        }
    }
}
