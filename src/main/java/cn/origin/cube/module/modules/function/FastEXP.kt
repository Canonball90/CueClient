package cn.origin.cube.module.modules.function

import cn.origin.cube.core.module.Category
import cn.origin.cube.core.module.Module
import cn.origin.cube.core.module.interfaces.ModuleInfo
import net.minecraft.init.Items


@ModuleInfo(name = "FastEXP",
    descriptions = "Fast exp asf",
    category = Category.FUNCTION)
class FastEXP: Module() {

    override fun onUpdate() {
        if (mc.world == null || mc.player == null) return
        if (mc.player.inventory.getStackInSlot(mc.player.inventory.currentItem).item === Items.EXPERIENCE_BOTTLE) {
            mc.rightClickDelayTimer = 0
        }
    }
}