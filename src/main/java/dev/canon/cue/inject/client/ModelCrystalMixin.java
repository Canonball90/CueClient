package dev.canon.cue.inject.client;

import dev.canon.cue.event.events.render.EventModelPlayerRender;
import dev.canon.cue.event.events.render.RenderEntityModelEvent;
import dev.canon.cue.event.events.render.RenderEntityModelEvent;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelEnderCrystal;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {ModelEnderCrystal.class}, priority = 94355)
public class ModelCrystalMixin {
    @Shadow
    public void render(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale) {
    }

    @Inject(method = {"render"}, at = {@At("HEAD")}, cancellable = true)
    private void renderPre(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale, final CallbackInfo ci) {
        final RenderEntityModelEvent modelrenderpre = new RenderEntityModelEvent(ModelEnderCrystal.class.cast(this), entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, 0);
        MinecraftForge.EVENT_BUS.post(modelrenderpre);
        if (modelrenderpre.isCanceled()) {
            ci.cancel();
        }
    }

    @Inject(method = {"render"}, at = {@At("RETURN")})
    private void renderPost(final Entity entityIn, final float limbSwing, final float limbSwingAmount, final float ageInTicks, final float netHeadYaw, final float headPitch, final float scale, final CallbackInfo ci) {
        final RenderEntityModelEvent modelrenderpost = new RenderEntityModelEvent(ModelEnderCrystal.class.cast(this), entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, 1);
        MinecraftForge.EVENT_BUS.post(modelrenderpost);
    }
}
