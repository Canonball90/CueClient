package cn.origin.cube.core.managers

import cn.origin.cube.Cube
import cn.origin.cube.core.events.player.DeathEvent
import cn.origin.cube.core.events.player.TotemPopListener
import cn.origin.cube.core.events.player.UpdateWalkingPlayerEvent
import cn.origin.cube.core.events.render.RenderOverlayEvent
import cn.origin.cube.core.events.world.Render3DEvent
import cn.origin.cube.guis.auth.AuthGui
import cn.origin.cube.utils.Utils
import cn.origin.cube.utils.Utils.nullCheck
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.client.event.*
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.Event
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent
import org.lwjgl.input.Keyboard


class EventManager {
    val mc: Minecraft = Minecraft.getMinecraft()
    val totemPopListener: TotemPopListener

    init {
        MinecraftForge.EVENT_BUS.register(this)
        totemPopListener = TotemPopListener()
        mc.displayGuiScreen(AuthGui())
        Cube.isOpenAuthGui = true
    }

    public fun onInit(){

    }

    @SubscribeEvent
    fun onKeyInput(event: InputEvent.KeyInputEvent) {
        if (event.isCanceled || !Keyboard.getEventKeyState() || Keyboard.getEventKey() <= 0) return
        for (module in Cube.moduleManager!!.allModuleList) {
            if (module.keyBind.value.keyCode <= 0) continue
            if (Keyboard.isKeyDown(module.keyBind.value.keyCode)) module.toggle()
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onTick(event: RenderTickEvent) {
        if (Utils.nullCheck()) return
        Cube.moduleManager!!.onUpdate()
        mc.world.playerEntities.stream().filter { player -> player != null && player.getHealth() <= 0.0f }
            .map { player: EntityPlayer? -> DeathEvent(player) }.forEach { event: Event? ->
                MinecraftForge.EVENT_BUS.post(
                    event
                )
            }
    }

    @SubscribeEvent
    fun onLogin(event: ClientConnectedToServerEvent) {
        if (Utils.nullCheck()) return
        Cube.moduleManager!!.onLogin()
    }

    @SubscribeEvent
    fun onLogout(event: ClientDisconnectionFromServerEvent) {
        if (Utils.nullCheck()) return
        Cube.moduleManager!!.onLogout()
    }


    @SubscribeEvent
    fun onRender2D(e: RenderGameOverlayEvent.Text) {
        if (e.type == RenderGameOverlayEvent.ElementType.TEXT) {
            Cube.moduleManager!!.onRender2D()
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
        val render3dEvent = Render3DEvent(event.partialTicks)
        Cube.moduleManager!!.onRender3D(render3dEvent)
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
        val renderOverlayEvent = RenderOverlayEvent(event.overlayType)
        MinecraftForge.EVENT_BUS.post(renderOverlayEvent)
        if (renderOverlayEvent.isCanceled) {
            event.isCanceled = true
        }
    }

    @SubscribeEvent
    fun onChat(event: ClientChatEvent) {
        if (event.message.startsWith(Cube.commandPrefix)) {
            Cube.commandManager.run(event.message)
            event.isCanceled = true;
            Minecraft.getMinecraft().ingameGUI.chatGUI.addToSentMessages(event.message);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onUpdateWalkingPlayer(updateWalkingPlayerEvent: UpdateWalkingPlayerEvent) {
        if (nullCheck()) {
            return
        }
        if (updateWalkingPlayerEvent.stage == 0) {
            Cube.rotationManager.updateRotations()
        }
        if (updateWalkingPlayerEvent.stage == 1) {
            Cube.rotationManager.restoreRotations()
        }
    }

    @SubscribeEvent
    fun onGuiOpen(event: GuiOpenEvent) {
        if (event.gui !is AuthGui && Cube.isOpenAuthGui && !Cube.allowToConfiguredAnotherClients) event.isCanceled =
            true
    }

}