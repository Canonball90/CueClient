package cn.origin.cube.module.modules.combat.AutoTotem

import cn.origin.cube.core.events.client.PacketEvent
import cn.origin.cube.core.settings.BooleanSetting
import cn.origin.cube.core.settings.FloatSetting
import cn.origin.cube.core.module.Category
import cn.origin.cube.core.module.Module
import cn.origin.cube.module.interfaces.ModuleInfo
import cn.origin.cube.utils.player.InventoryUtil
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.gui.inventory.GuiInventory
import net.minecraft.init.Items
import net.minecraft.inventory.ClickType
import net.minecraft.network.Packet
import net.minecraft.network.play.client.CPacketClickWindow
import net.minecraft.network.play.client.CPacketEntityAction
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent

@ModuleInfo(name = "AutoTotem",
    descriptions = "A",
    category = Category.COMBAT)
class AutoTote : Module() {

    val stacked: BooleanSetting = registerSetting("Stacked", false)
    val delay: FloatSetting = registerSetting("Delay",  100F, 10F, 300F).booleanVisible(stacked)
    val delay2: FloatSetting = registerSetting("SecondDelay", 100F, 10F, 300F).booleanVisible(stacked)
    val switchCount: FloatSetting = registerSetting("SwitchCount",  6F, 3F, 63F).booleanVisible(stacked)
    val prepareCount: FloatSetting = registerSetting("PrepCount", 3F, 0F, 6F).booleanVisible(stacked)
    val minCount: FloatSetting = registerSetting("MinCount", 23F, 1F, 63F).booleanVisible(stacked)


    @SubscribeEvent
    fun onTick(event: ClientTickEvent?) {
        if (fullNullCheck()) return
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