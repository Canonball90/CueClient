package cn.origin.cube.module.modules.function

import cn.origin.cube.event.events.client.PacketEvent
import cn.origin.cube.event.events.player.UpdateWalkingPlayerEvent
import cn.origin.cube.module.Category
import cn.origin.cube.module.Module
import cn.origin.cube.module.ModuleInfo
import cn.origin.cube.settings.FloatSetting
import cn.origin.cube.settings.ModeSetting
import net.minecraft.network.Packet
import net.minecraft.network.play.client.CPacketPlayer
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.concurrent.ThreadLocalRandom

@ModuleInfo(name = "NoFall",
        descriptions = "",
        category = Category.FUNCTION)
class NoFall: Module() {

    private var mode: ModeSetting<modeds> = registerSetting("Mode", modeds.Ncp)
    private var speed: FloatSetting = registerSetting("Speed", 5.0f, 1.0f, 10.0f).modeVisible(mode, modeds.Glide)
    private var fallDist: FloatSetting = registerSetting("Fall Distance", 5.0f, 1.0f, 10.0f).modeVisible(mode, modeds.OldFag)

    @SubscribeEvent
    fun onWalk(event: UpdateWalkingPlayerEvent?) {
        if (mode.value.equals(modeds.Ncp)) {
            mc.connection?.sendPacket(CPacketPlayer(ThreadLocalRandom.current().nextBoolean()))
        }
        if (mode.value.equals(modeds.anti)) {
            mc.connection?.sendPacket(CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1, mc.player.posZ, true))
        }
    }

    override fun onUpdate() {
        if(mode.value.equals(modeds.Glide)){
            mc.player.motionY /= speed.value
        }
        if(mode.value.equals(modeds.OldFag)){
            if (mc.player.fallDistance > fallDist.value) {
                mc.player.connection.sendPacket(CPacketPlayer.Position(mc.player.posX, 0.0, mc.player.posZ, false))
                mc.player.fallDistance = 0f
            }
        }
        super.onUpdate()
    }

    //ToDo this
    @SubscribeEvent
    fun onSend(event: PacketEvent.Send) {
        if (event.getPacket<Packet<*>>() is CPacketPlayer) return
//        if(mode.value.equals(modeds.Offground)){
//            (event.getPacket() as ICPacketPlayer).setOnGround(false)
//        }
//        if(mode.value.equals(modeds.OnGround)){
//            (event.getPacket() as ICPacketPlayer).setOnGround(true)
//        }
    }

    private fun shouldCancel() =
        !mc.player.isElytraFlying && !mc.player.capabilities.allowFlying && mc.player.fallDistance > 3

    enum class modeds {
        Ncp, Offground, anti, OnGround, Glide, OldFag
    }
}