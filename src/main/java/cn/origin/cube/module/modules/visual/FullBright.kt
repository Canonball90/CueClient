package cn.origin.cube.module.modules.visual

import cn.origin.cube.core.module.Category
import cn.origin.cube.core.module.Module
import cn.origin.cube.core.module.interfaces.Constant
import cn.origin.cube.core.module.interfaces.ModuleInfo
import cn.origin.cube.core.settings.FloatSetting
import cn.origin.cube.core.settings.ModeSetting
import net.minecraft.init.MobEffects
import net.minecraft.potion.PotionEffect

@Constant(constant = false)
@ModuleInfo(name = "FullBright", descriptions = "Always light", category = Category.VISUAL)
class FullBright: Module() {

    var modeSetting: ModeSetting<*> = registerSetting("Mode", mode.GAMMA)
    var gamma: FloatSetting = registerSetting("Gamma", 800f, -10f, 1000f).modeOrVisible(modeSetting, mode.GAMMA, mode.BOTH)

    override fun onEnable() {
        if (modeSetting.value == mode.GAMMA || modeSetting.value == mode.BOTH) {
            mc.gameSettings.gammaSetting = gamma.value
        }
        if (modeSetting.value == mode.POTION || modeSetting.value == mode.BOTH) {
            mc.player.addPotionEffect(PotionEffect(MobEffects.NIGHT_VISION, 100))
        }
    }

    override fun onDisable() {
        mc.gameSettings.gammaSetting = 1f
        mc.player.removePotionEffect(MobEffects.NIGHT_VISION)
    }


    private enum class mode {
        GAMMA, POTION, BOTH
    }
}