package cn.origin.cube.module.modules.combat.killaura

import cn.origin.cube.core.module.Module
import cn.origin.cube.utils.player.InventoryUtil
import net.minecraft.init.Items
import net.minecraft.network.Packet
import net.minecraft.network.play.client.CPacketHeldItemChange
import net.minecraft.network.play.client.CPacketPlayerTryUseItem
import net.minecraft.util.EnumHand

object SwitchUtils {

    @JvmStatic
    var oldSlot = 0

    @JvmStatic
    fun oldSwitch(){
        oldSlot = Module.mc.player.inventory.currentItem
    }

    @JvmStatic
    fun silent(){
        val slot: Int = InventoryUtil.findItemInHotbar(Items.DIAMOND_SWORD)
        if (InventoryUtil.findItemInHotbar(Items.DIAMOND_SWORD) !== -1) {
            Module.mc.player.connection.sendPacket(CPacketHeldItemChange(slot) as Packet<*>)
            Module.mc.player.connection.sendPacket(CPacketPlayerTryUseItem(EnumHand.MAIN_HAND) as Packet<*>)
        }
    }
}