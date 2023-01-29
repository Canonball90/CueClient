package cn.origin.cube.module.modules.world

import cn.origin.cube.core.module.Category
import cn.origin.cube.core.module.Module
import cn.origin.cube.core.module.interfaces.Constant
import cn.origin.cube.core.module.interfaces.ModuleInfo
import net.minecraft.client.gui.GuiGameOver

@Constant(constant = false)
@ModuleInfo(name = "AutoRespawn",
    descriptions = "Anti Death Screen",
    category = Category.WORLD)
class AutoRespawn: Module() {
    override fun onUpdate() {
        if (mc.currentScreen is GuiGameOver && mc.player.getHealth() >= 0.0f) {
            mc.player.respawnPlayer()
            mc.displayGuiScreen(null)
        }
    }
}