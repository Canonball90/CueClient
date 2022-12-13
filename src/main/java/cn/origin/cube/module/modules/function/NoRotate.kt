package cn.origin.cube.module.modules.function

import cn.origin.cube.event.events.client.PacketEvent
import cn.origin.cube.module.Category
import cn.origin.cube.module.Module
import cn.origin.cube.module.ModuleInfo
import net.minecraft.network.Packet
import net.minecraft.network.play.server.SPacketPlayerPosLook
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@ModuleInfo(name = "NoRotate", descriptions = "Dangerous to use might desync you.", category = Category.FUNCTION)
class NoRotate:Module() {

    private var cancelPackets = true
    private var timerReset = false

    override fun onUpdate() {
        if (timerReset && !cancelPackets) {
            cancelPackets = true
            timerReset = false
        }
    }

    @SubscribeEvent
    fun onPacketReceive(event: PacketEvent.Receive) {
        if (fullNullCheck()) return
        if (event.stage == 0 && cancelPackets && event.getPacket<Packet<*>>() is SPacketPlayerPosLook) {
            val packet = event.getPacket<SPacketPlayerPosLook>()
            packet.yaw = mc.player.rotationYaw
            packet.pitch = mc.player.rotationPitch
        }
    }
}