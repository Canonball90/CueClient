package dev.canon.cue.module.modules.function

import dev.canon.cue.module.Category
import dev.canon.cue.module.Module
import dev.canon.cue.module.ModuleInfo
import net.minecraft.init.Items


@ModuleInfo(
    name = "FastEXP",
    descriptions = "Fast exp asf",
    category = Category.FUNCTION
)
class FastEXP : Module() {

    override fun onUpdate() {
        if (mc.world == null || mc.player == null) return
        if (mc.player.inventory.getStackInSlot(mc.player.inventory.currentItem).item === Items.EXPERIENCE_BOTTLE) {
            mc.rightClickDelayTimer = 0
        }
    }
}