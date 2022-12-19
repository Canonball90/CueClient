package cn.origin.cube.inject.client;

import cn.origin.cube.core.viaforge.ViaForge;
import net.minecraft.client.multiplayer.GuiConnecting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiConnecting.class)
public class MixinGuiConnecting {
    @Inject(method = "connect", at = @At("HEAD"))
    public void injectConnect(String ip, int port, CallbackInfo ci) {
        ViaForge.getInstance().setLastServer(ip + ":" + port);
    }
}
