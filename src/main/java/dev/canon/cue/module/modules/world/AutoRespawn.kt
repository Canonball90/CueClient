package dev.canon.cue.module.modules.world

import dev.canon.cue.module.Category
import dev.canon.cue.module.Module
import dev.canon.cue.module.ModuleInfo
import net.minecraft.client.gui.GuiGameOver

@ModuleInfo(
    name = "AutoRespawn",
    descriptions = "Anti Death Screen",
    category = Category.WORLD
)
class AutoRespawn : Module() {
    override fun onUpdate() {
        if (mc.currentScreen is GuiGameOver && mc.player.health >= 0.0f) {
            mc.player.respawnPlayer()
            mc.displayGuiScreen(null)
        }
    }
}