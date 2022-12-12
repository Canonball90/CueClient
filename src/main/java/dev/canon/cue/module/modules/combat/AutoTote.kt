package dev.canon.cue.module.modules.combat

import dev.canon.cue.event.events.client.PacketEvent
import dev.canon.cue.module.Category
import dev.canon.cue.module.Module
import dev.canon.cue.module.ModuleInfo
import dev.canon.cue.utils.player.InventoryUtil
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.init.Items
import net.minecraft.inventory.ClickType
import net.minecraft.network.Packet
import net.minecraft.network.play.client.CPacketClickWindow
import net.minecraft.network.play.client.CPacketEntityAction
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

@ModuleInfo(
    name = "AutoTotem",
    descriptions = "A",
    category = Category.COMBAT
)
class AutoTote : Module() {

    override fun onUpdate() {
        if (mc.currentScreen is GuiContainer && mc.currentScreen !is GuiInventory) return
        val totemslot = InventoryUtil.getItemSlot(Items.TOTEM_OF_UNDYING)
        if (mc.player.heldItemOffhand.item !== Items.TOTEM_OF_UNDYING && totemslot != -1) {
            mc.playerController.windowClick(
                mc.player.inventoryContainer.windowId,
                totemslot,
                0,
                ClickType.PICKUP,
                mc.player
            )
            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 45, 0, ClickType.PICKUP, mc.player)
            mc.playerController.windowClick(
                mc.player.inventoryContainer.windowId,
                totemslot,
                0,
                ClickType.PICKUP,
                mc.player
            )
            mc.playerController.updateController()
        }
        super.onUpdate()
    }

    @SubscribeEvent
    fun onPacketSend(event: PacketEvent.Send) {
        if (event.getPacket<Packet<*>>() is CPacketClickWindow) {
            mc.player.connection.sendPacket(CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING))
        }
    }
}