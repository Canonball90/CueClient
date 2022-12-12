package dev.canon.cue.module.modules.world

import dev.canon.cue.module.Category
import dev.canon.cue.module.Module
import dev.canon.cue.module.ModuleInfo
import dev.canon.cue.settings.FloatSetting
import dev.canon.cue.utils.Utils.nullCheck
import net.minecraft.client.renderer.entity.RenderPig
import net.minecraft.entity.passive.EntityPig

@ModuleInfo(
    name = "BigPOV",
    descriptions = "BigPOV",
    category = Category.WORLD
)
class BigPOV : Module() {
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