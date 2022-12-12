package dev.canon.cue.module.modules.visual

import dev.canon.cue.event.events.render.*
import dev.canon.cue.module.Category
import dev.canon.cue.module.Module
import dev.canon.cue.module.ModuleInfo
import net.minecraftforge.client.event.RenderBlockOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent


@ModuleInfo(
    name = "NoRender",
    descriptions = "No",
    category = Category.VISUAL
)
class NoRender : Module() {

    @SubscribeEvent
    fun onRenderBossOverlay(event: BossOverlayEvent) {
        event.isCanceled = true
    }

    @SubscribeEvent
    fun onRenderMap(event: RenderMapEvent) {
        event.isCanceled = true
    }

    @SubscribeEvent
    fun onRenderOverlay(event: RenderOverlayEvent) {
        if (event.overlayType.equals(RenderBlockOverlayEvent.OverlayType.FIRE)) {
            event.isCanceled = true
        }
        if (event.overlayType.equals(RenderBlockOverlayEvent.OverlayType.WATER)) {
            event.isCanceled = true
        }
        if (event.overlayType.equals(RenderBlockOverlayEvent.OverlayType.BLOCK)) {
            event.isCanceled = true
        }
    }

    @SubscribeEvent
    fun onHurtCamera(event: HurtCameraEvent) {
        event.isCanceled = true
    }

    @SubscribeEvent
    fun onRenderItemActivation(event: RenderItemActivationEvent) {
        event.isCanceled = true
    }
}