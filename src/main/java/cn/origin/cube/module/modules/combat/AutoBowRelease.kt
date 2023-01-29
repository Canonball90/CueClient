package cn.origin.cube.module.modules.combat

import cn.origin.cube.core.module.Category
import cn.origin.cube.core.module.Module
import cn.origin.cube.core.module.interfaces.Constant
import cn.origin.cube.core.module.interfaces.ModuleInfo
import cn.origin.cube.core.settings.IntegerSetting
import net.minecraft.init.Items
import net.minecraft.network.play.client.CPacketPlayerDigging
import net.minecraft.util.math.BlockPos

@Constant(constant = false)
@ModuleInfo(name = "AutoBowRelease",
    descriptions = "AutoBowRelease",
    category = Category.COMBAT)
class AutoBowRelease: Module() {

    private val ticks: IntegerSetting = registerSetting("Ticks", 3, 0, 20)


    override fun onUpdate() {
        if (mc.player.getHeldItemMainhand()
                .getItem() === Items.BOW && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() > ticks.value
        ) {
            mc.player.connection.sendPacket(
                CPacketPlayerDigging(
                    CPacketPlayerDigging.Action.RELEASE_USE_ITEM,
                    BlockPos.ORIGIN,
                    mc.player.getHorizontalFacing()
                )
            )
            mc.player.stopActiveHand()
        }
    }
}