package cn.origin.cube.module.modules.combat.Criticals

import cn.origin.cube.core.events.client.PacketEvent
import cn.origin.cube.core.events.player.MotionEvent
import cn.origin.cube.core.settings.BooleanSetting
import cn.origin.cube.core.settings.IntegerSetting
import cn.origin.cube.core.settings.ModeSetting
import cn.origin.cube.inject.client.ICPacketPlayer
import cn.origin.cube.inject.client.INetworkManager
import cn.origin.cube.module.Category
import cn.origin.cube.module.Module
import cn.origin.cube.module.interfaces.ModuleInfo
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.client.CPacketPlayer
import net.minecraft.network.play.client.CPacketUseEntity
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@ModuleInfo(name = "Criticals",
    descriptions = "Criticals",
    category = Category.COMBAT)
class Criticals: Module() {

    public val mode: ModeSetting<model> = registerSetting("Mode", model.PACKET)
    public val moveCancel: BooleanSetting = registerSetting("MoveCancel", false)
    public val packets: IntegerSetting = registerSetting("Packets", 2,0, 4)

    private var pauseTicks = 0

    override fun onDisable() {
        pauseTicks = 0
    }

    @SubscribeEvent
    fun onPacketSend(event: PacketEvent.Send) {
        if(fullNullCheck()) return
        if(mode.value == model.NEW){
            if(event.packet is CPacketUseEntity && event.packet.action == CPacketUseEntity.Action.ATTACK) {
                CritUtils.doCrit()
            }
        }
        if (event.packet is CPacketUseEntity) {

            if (event.packet.action != CPacketUseEntity.Action.ATTACK || event.packet.getEntityFromWorld(mc.world) !is EntityLivingBase) {
                return
            }

            if (mc.player.onGround && !mc.gameSettings.keyBindJump.isKeyDown) {

                if(mode.value.equals(model.PACKET)){
                    send(0.1)
                    send(0.0)
                }
                if(mode.value.equals(model.UPDATED_NCP)){
                    pauseTicks = 2

                    send(0.11)
                    send(0.1100013579)
                    send(0.0000013579)
                }
                if(mode.value.equals(model.MINIS)){
                    mc.player.motionY = 0.2
                }
            }
        } else if (event.packet is CPacketPlayer) {
            if(moveCancel.value) {
                if ((event.packet as ICPacketPlayer).isMoving && pauseTicks > 0) {
                    event.isCanceled
                }
            }
        }
    }

    @SubscribeEvent
    fun onMotion(event: MotionEvent) {
        if (pauseTicks-- > 0) {
            event.x = 0.0
            event.z = 0.0

            mc.player.motionX = 0.0
            mc.player.motionZ = 0.0
        }
    }

    private fun send(yOffset: Double) {
        (mc.player.connection.networkManager as INetworkManager).hookDispatchPacket(CPacketPlayer.Position(
            mc.player.posX, mc.player.posY + yOffset, mc.player.posZ, false
        ), null)
    }

    public enum class model{
        PACKET,UPDATED_NCP,MINIS,NEW
    }
}