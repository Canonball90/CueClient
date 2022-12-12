package dev.canon.cue.event

import dev.canon.cue.Cue
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraftforge.client.event.ClientChatEvent
import net.minecraftforge.client.event.RenderBlockOverlayEvent
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent
import org.lwjgl.input.Keyboard


class EventManager {
    val mc: Minecraft = Minecraft.getMinecraft()
    val totemPopListener: dev.canon.cue.event.events.player.TotemPopListener

    init {
        MinecraftForge.EVENT_BUS.register(this)
        totemPopListener = dev.canon.cue.event.events.player.TotemPopListener()
    }

    fun EventManager() {

    }

    @SubscribeEvent
    fun onKeyInput(event: InputEvent.KeyInputEvent) {
        if (event.isCanceled || !Keyboard.getEventKeyState() || Keyboard.getEventKey() <= 0) return
        for (module in Cue.moduleManager!!.allModuleList) {
            if (module.keyBind.value.keyCode <= 0) continue
            if (Keyboard.isKeyDown(module.keyBind.value.keyCode)) module.toggle()
        }
    }

    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        if (dev.canon.cue.utils.Utils.nullCheck()) return
        Cue.moduleManager!!.onUpdate()
    }

    @SubscribeEvent
    fun onLogin(event: ClientConnectedToServerEvent) {
        if (dev.canon.cue.utils.Utils.nullCheck()) return
        Cue.moduleManager!!.onLogin()
    }

    @SubscribeEvent
    fun onLogout(event: ClientDisconnectionFromServerEvent) {
        if (dev.canon.cue.utils.Utils.nullCheck()) return
        Cue.moduleManager!!.onLogout()
    }


    @SubscribeEvent
    fun onRender2D(e: RenderGameOverlayEvent.Text) {
        if (e.type == RenderGameOverlayEvent.ElementType.TEXT) {
            Cue.moduleManager!!.onRender2D()
        }
    }

    @SubscribeEvent
    fun onWorldRender(event: RenderWorldLastEvent) {
        if (event.isCanceled) return
        mc.profiler.startSection("CubeBase")
        GlStateManager.disableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.disableAlpha()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.shadeModel(7425)
        GlStateManager.disableDepth()
        GlStateManager.glLineWidth(1.0f)
        val render3dEvent = dev.canon.cue.event.events.world.Render3DEvent(event.partialTicks)
        Cue.moduleManager!!.onRender3D(render3dEvent)
        GlStateManager.glLineWidth(1.0f)
        GlStateManager.shadeModel(7424)
        GlStateManager.disableBlend()
        GlStateManager.enableAlpha()
        GlStateManager.enableTexture2D()
        GlStateManager.enableDepth()
        GlStateManager.enableCull()
        GlStateManager.enableCull()
        GlStateManager.depthMask(true)
        GlStateManager.enableTexture2D()
        GlStateManager.enableBlend()
        GlStateManager.enableDepth()
        mc.profiler.endSection()
    }

    @SubscribeEvent
    fun onRenderBlockOverlay(event: RenderBlockOverlayEvent) {
        val renderOverlayEvent =
            dev.canon.cue.event.events.render.RenderOverlayEvent(event.overlayType)
        MinecraftForge.EVENT_BUS.post(renderOverlayEvent)
        if (renderOverlayEvent.isCanceled) {
            event.isCanceled = true
        }
    }

    @SubscribeEvent
    fun onChat(event: ClientChatEvent) {
        if (event.message.startsWith(Cue.commandPrefix)) {
            Cue.commandManager.run(event.message)
            event.isCanceled = true
            Minecraft.getMinecraft().ingameGUI.chatGUI.addToSentMessages(event.message)
        }
    }
}