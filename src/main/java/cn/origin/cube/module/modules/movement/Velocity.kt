package cn.origin.cube.module.modules.movement

import cn.origin.cube.core.events.client.PacketEvent
import cn.origin.cube.core.events.player.EntityCollisionEvent
import cn.origin.cube.core.module.Category
import cn.origin.cube.core.module.Module
import cn.origin.cube.core.module.interfaces.Constant
import cn.origin.cube.core.module.interfaces.ModuleInfo
import cn.origin.cube.core.settings.BooleanSetting
import cn.origin.cube.core.settings.FloatSetting
import cn.origin.cube.inject.client.ISPacketExplosion
import net.minecraft.network.Packet
import net.minecraft.network.play.server.SPacketEntityVelocity
import net.minecraft.network.play.server.SPacketExplosion
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Constant(constant = false)
@ModuleInfo(name = "Velocity",
    descriptions = "Velocity",
    category = Category.FUNCTION)
class Velocity : Module() {

    var horiz: FloatSetting = registerSetting("Horizontal", 0.0f, 0.0f, 100.0f)
    var vert: FloatSetting = registerSetting("Vertical", 0.0f, 0.0f, 100.0f)
    var explo: BooleanSetting = registerSetting("Explosion", true)
    var velo: BooleanSetting = registerSetting("Velocity", true)

    @SubscribeEvent
    fun onPacketRecieve(e: PacketEvent.Receive) {
        if (explo.value) {
            if (e.getPacket<Packet<*>>() is SPacketExplosion) {
                if (horiz.value == 0.0f && vert.value == 0.0f) {
                    e.isCanceled = true
                } else {
                    val packet = e.getPacket<Packet<*>>() as SPacketExplosion
                    val motionX = (packet as ISPacketExplosion).motionX / 100
                    val motionY = (packet as ISPacketExplosion).motionY / 100
                    val motionZ = (packet as ISPacketExplosion).motionZ / 100
                    (packet as ISPacketExplosion).motionX = motionX * horiz.value
                    (packet as ISPacketExplosion).motionY = motionY * vert.value
                    (packet as ISPacketExplosion).motionZ = motionZ * horiz.value
                }
            }
        }
        if (velo.value) {
            if (e.getPacket<Packet<*>>() is SPacketEntityVelocity) {
                if (horiz.value == 0.0f && vert.value == 0.0f) {
                    e.isCanceled = true
                } else {
                    //ToDo this
                }
            }
        }
    }

    @SubscribeEvent
    fun onPushy(event: EntityCollisionEvent){
        event.x = event.x * 0
        event.y = 0.0
        event.z = event.z * 0
    }

    override fun getHudInfo(): String? {
        return "H%,"+horiz.value+",V%,"+vert.value+""
    }
}
