package cn.origin.cube.module.modules.combat.Criticals

import cn.origin.cube.core.events.client.PacketEvent
import cn.origin.cube.core.events.player.MotionEvent
import cn.origin.cube.inject.client.ICPacketPlayer
import cn.origin.cube.inject.client.INetworkManager
import cn.origin.cube.core.module.Category
import cn.origin.cube.core.module.Module
import cn.origin.cube.core.module.interfaces.ModuleInfo
import cn.origin.cube.core.settings.*
import net.minecraft.entity.EntityLivingBase
import net.minecraft.network.play.client.CPacketPlayer
import net.minecraft.network.play.client.CPacketUseEntity
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@ModuleInfo(name = "Criticals",
    descriptions = "Criticals",
    category = Category.COMBAT)
class Criticals: Module() {

    val mode: ModeSetting<model> = registerSetting("Mode", model.PACKET)
    val moveCancel: BooleanSetting = registerSetting("MoveCancel", false)
    private val onlyWeapon: BooleanSetting = registerSetting("OnlyWeapon", false)
    val packets: IntegerSetting = registerSetting("Packets", 2,0, 4)
    private val jumpHeight: DoubleSetting = registerSetting("JumpHeight", 0.2,0.1, 1.0).modeVisible(mode, model.MINIS)

    private var pauseTicks = 0

    override fun onDisable() {
        pauseTicks = 0
    }

    @SubscribeEvent
    fun onPacketSend(event: PacketEvent.Send) {
        if(fullNullCheck()) return
        if(mode.value == model.NEW){
            if(event.packet is CPacketUseEntity && event.packet.action == CPacketUseEntity.Action.ATTACK && CritUtils.canCrit(onlyWeapon.value)) {
                CritUtils.doCrit()
            }
        }
        if (event.packet is CPacketUseEntity) {

            if (event.packet.action != CPacketUseEntity.Action.ATTACK || event.packet.getEntityFromWorld(mc.world) !is EntityLivingBase) {
                return
            }

            if (mc.player.onGround && !mc.gameSettings.keyBindJump.isKeyDown) {
                if(CritUtils.canCrit(onlyWeapon.value)) {
                    if (mode.value.equals(model.PACKET)) {
                        send(0.1)
                        send(0.0)
                    }
                    if (mode.value.equals(model.UPDATED_NCP)) {
                        pauseTicks = 2

                        send(0.11)
                        send(0.1100013579)
                        send(0.0000013579)
                    }
                    if (mode.value.equals(model.MINIS)) {
                        mc.player.motionY = jumpHeight.value
                    }
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

    enum class model{
        PACKET,UPDATED_NCP,MINIS,NEW
    }
}