package cn.origin.cube.module.modules.world

import cn.origin.cube.core.module.Category
import cn.origin.cube.core.module.Module
import cn.origin.cube.core.module.interfaces.Constant
import cn.origin.cube.core.module.interfaces.ModuleInfo
import cn.origin.cube.core.settings.FloatSetting
import cn.origin.cube.utils.Utils.nullCheck
import net.minecraft.client.renderer.entity.RenderPig
import net.minecraft.entity.passive.EntityPig

@Constant(constant = false)
@ModuleInfo(name = "BigPOV",
    descriptions = "BigPOV",
    category = Category.WORLD)
class BigPOV: Module() {
    var heightl: FloatSetting = registerSetting("Height", 5f, 1f, 15f)

    override fun onEnable() {
        if (nullCheck()) return
        mc.player.eyeHeight = heightl.value
        mc.getRenderManager().entityRenderMap[EntityPig::class.java] = RenderPig(mc.getRenderManager())
    }

    override fun onDisable() {
        mc.player.eyeHeight = mc.player.defaultEyeHeight
        mc.getRenderManager().entityRenderMap[EntityPig::class.java] = RenderPig(mc.getRenderManager())
    }
}