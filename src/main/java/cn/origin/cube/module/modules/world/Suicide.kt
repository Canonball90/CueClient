package cn.origin.cube.module.modules.world

import cn.origin.cube.core.module.Category
import cn.origin.cube.core.module.Module
import cn.origin.cube.core.module.interfaces.Constant
import cn.origin.cube.core.module.interfaces.ModuleInfo
import cn.origin.cube.core.settings.ModeSetting

@Constant(constant = false)
@ModuleInfo(name = "Suicide", descriptions = "Auto Kill ur self", category = Category.WORLD)
class Suicide : Module() {

    private var mode: ModeSetting<modeses> = registerSetting("Mode", modeses.NORMAL)

    override fun onEnable() {
        if(mode.value.equals(modeses.NORMAL)){
            mc.player.sendChatMessage("/kill")
        }
        if(mode.value.equals(modeses.STRICT)){
            mc.player.sendChatMessage("I WANT TO FUCKING KILL MYSELF PLS KILL ME AT -->" +
                    mc.player.posX + " " +
                    mc.player.posY + " " +
                    mc.player.posZ)
        }
        disable()
        super.onEnable()
    }

    enum class modeses {
        STRICT, NORMAL
    }
}