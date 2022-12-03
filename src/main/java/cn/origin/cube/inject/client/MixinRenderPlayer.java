package cn.origin.cube.inject.client;

import cn.origin.cube.module.modules.visual.NameTags;
import cn.origin.cube.module.modules.visual.ShaderChams;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {RenderPlayer.class})
public class MixinRenderPlayer {
    @Inject(method = {"renderEntityName"}, at = {@At(value = "HEAD")}, cancellable = true)
    public void renderEntityNameHook(AbstractClientPlayer entityIn, double x, double y, double z, String name, double distanceSq, CallbackInfo bigBlackMonke) {
        if (NameTags.getInstance().isEnabled()) {
            bigBlackMonke.cancel();
        }
        if(ShaderChams.getInstance().isEnabled()){
            bigBlackMonke.cancel();
        }
    }
}
