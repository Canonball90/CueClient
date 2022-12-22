package cn.origin.cube.module.modules.function

import cn.origin.cube.core.events.client.PacketEvent
import cn.origin.cube.module.Category
import cn.origin.cube.module.Module
import cn.origin.cube.module.interfaces.ModuleInfo
import net.minecraft.init.Items
import net.minecraft.network.Packet
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@ModuleInfo(name = "BoatPlace", descriptions = "BoatPlace", category = Category.FUNCTION)
class BoatPlace: Module() {
    @SubscribeEvent
    fun onPacketSend(event: PacketEvent.Send) {
        if (event.getPacket<Packet<*>>() is CPacketPlayerTryUseItemOnBlock) {
            if (mc.player.heldItemMainhand.item === Items.BOAT) {
                event.isCanceled = true
            }
        }
    }
}