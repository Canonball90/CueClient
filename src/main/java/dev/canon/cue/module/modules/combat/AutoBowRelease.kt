package dev.canon.cue.module.modules.combat

import dev.canon.cue.module.Category
import dev.canon.cue.module.Module
import dev.canon.cue.module.ModuleInfo
import dev.canon.cue.settings.IntegerSetting
import net.minecraft.init.Items
import net.minecraft.network.play.client.CPacketPlayerDigging
import net.minecraft.util.math.BlockPos


@ModuleInfo(
    name = "AutoBowRelease",
    descriptions = "AutoBowRelease",
    category = Category.COMBAT
)
class AutoBowRelease : Module() {

    private val ticks: IntegerSetting = registerSetting("Ticks", 3, 0, 20)


    override fun onUpdate() {
        if (mc.player.heldItemMainhand
                .item === Items.BOW && mc.player.isHandActive && mc.player.itemInUseMaxCount > ticks.value
        ) {
            mc.player.connection.sendPacket(
                CPacketPlayerDigging(
                    CPacketPlayerDigging.Action.RELEASE_USE_ITEM,
                    BlockPos.ORIGIN,
                    mc.player.horizontalFacing
                )
            )
            mc.player.stopActiveHand()
        }
    }
}