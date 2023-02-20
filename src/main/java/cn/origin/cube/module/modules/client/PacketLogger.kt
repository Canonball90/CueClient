package cn.origin.cube.module.modules.client

import cn.origin.cube.core.settings.BooleanSetting
import cn.origin.cube.core.settings.DoubleSetting
import cn.origin.cube.core.events.client.PacketEvent
import cn.origin.cube.core.events.player.ProcessRightClickBlockEvent
import cn.origin.cube.core.events.player.TravelEvent
import cn.origin.cube.core.events.player.UpdateWalkingPlayerEvent
import cn.origin.cube.core.module.Category
import cn.origin.cube.core.module.Module
import cn.origin.cube.core.module.interfaces.Constant
import cn.origin.cube.core.module.interfaces.ModuleInfo
import cn.origin.cube.core.module.interfaces.Para
import cn.origin.cube.utils.Timer
import cn.origin.cube.utils.client.ChatUtil
import net.minecraft.network.Packet
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@Constant(constant = false)
@Para(para = Para.ParaMode.Test)
@ModuleInfo(name = "PacketLogger", descriptions = "L", category = Category.CLIENT)
class PacketLogger: Module() {

    private var incoming: BooleanSetting = registerSetting("Incoming", true)
    private var outgoing: BooleanSetting = registerSetting("OutGoing", false)
    var delay: BooleanSetting = registerSetting("Delay", true)
    private var delayS: DoubleSetting = registerSetting("Delay", 5.0, 0.0, 1000.0)
    private var travel: BooleanSetting = registerSetting("Travel", false)
    private var processClick: BooleanSetting = registerSetting("Travel", false)
    private var upadtewalk: BooleanSetting = registerSetting("Travel", false)

    var timer: Timer = Timer()

    @SubscribeEvent
    fun onPacketRecive(event: PacketEvent.Receive){
        if(fullNullCheck()) return
        if(incoming.value) {
            if(delay.value){
                if(timer.passed(delayS.value)){
                    timer.reset()
                    ChatUtil.sendMessage(event.getPacket<Packet<*>>().toString())
                }
            }else{
                ChatUtil.sendMessage(event.getPacket<Packet<*>>().toString())
            }
        }
    }

    @SubscribeEvent
    fun onTravel(event: TravelEvent){
        if(travel.value){
            ChatUtil.sendMessage(event.result.toString() + "[" + event.phase.toString() + "]")
        }
    }

    @SubscribeEvent
    fun onCick(event: ProcessRightClickBlockEvent){
        if(processClick.value){
            ChatUtil.sendMessage(event.pos.toString() + "[" + event.hand.toString() + "]")
        }
    }

    @SubscribeEvent
    fun Onwalk(event: UpdateWalkingPlayerEvent){
        if(travel.value){
            ChatUtil.sendMessage(event.result.toString() + "[" + event.phase.toString() + "]")
        }
    }

    @SubscribeEvent
    fun onPacketSend(event: PacketEvent.Send){
        if(fullNullCheck())return
        if(outgoing.value){
            if(delay.value){
                if(timer.passed(delayS.value)){
                    timer.reset()
                    ChatUtil.sendMessage(event.getPacket<Packet<*>>().toString())
                }
            }else{
                ChatUtil.sendMessage(event.getPacket<Packet<*>>().toString())
            }
        }
    }
}