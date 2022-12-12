package dev.canon.cue.module.modules.world

import dev.canon.cue.module.Category
import dev.canon.cue.module.Module
import dev.canon.cue.module.ModuleInfo
import dev.canon.cue.settings.ModeSetting

@ModuleInfo(
    name = "Suicide",
    descriptions = "Auto Kill ur self",
    category = Category.WORLD
)
class Suicide : Module() {

    private var mode: ModeSetting<modeses> = registerSetting("Mode", modeses.NORMAL)

    override fun onEnable() {
        if (mode.value.equals(modeses.NORMAL)) {
            mc.player.sendChatMessage("/kill")
        }
        if (mode.value.equals(modeses.STRICT)) {
            mc.player.sendChatMessage(
                "I WANT TO FUCKING KILL MYSELF PLS KILL ME AT -->" +
                        mc.player.posX + " " +
                        mc.player.posY + " " +
                        mc.player.posZ
            )
        }
        disable()
        super.onEnable()
    }

    enum class modeses {
        STRICT, NORMAL
    }
}