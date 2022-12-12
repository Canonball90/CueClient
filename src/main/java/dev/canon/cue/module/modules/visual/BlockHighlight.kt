package dev.canon.cue.module.modules.visual

import dev.canon.cue.event.events.world.Render3DEvent
import dev.canon.cue.module.Category
import dev.canon.cue.module.Module
import dev.canon.cue.module.ModuleInfo
import dev.canon.cue.module.modules.client.ClickGui
import dev.canon.cue.settings.BooleanSetting
import dev.canon.cue.settings.FloatSetting
import dev.canon.cue.settings.IntegerSetting
import dev.canon.cue.utils.render.Render3DUtil
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import java.awt.Color

@ModuleInfo(
    name = "BlockHighlight",
    descriptions = "Render current block",
    category = Category.VISUAL
)
class BlockHighlight : Module() {

    private val outline: BooleanSetting = registerSetting("Outline", true)
    private val full: BooleanSetting = registerSetting("FullBlock", true)
    private val widthy: FloatSetting = registerSetting("OutlineWidth", 1.5f, 0.0f, 10.0f).booleanVisible(outline)

    private val alpha: IntegerSetting = registerSetting("Alpha", 55, 0, 255).booleanVisible(full)

    override fun onRender3D(event: Render3DEvent?) {
        if (fullNullCheck()) return
        var blockpos: BlockPos?
        val mc = Minecraft.getMinecraft()
        val ray = mc.objectMouseOver
        ray.blockPos.also { blockpos = it }
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK && mc.world.getBlockState(ray.blockPos.also {
                blockpos = it
            }).material !== Material.AIR && mc.world.worldBorder.contains(blockpos)) {
            Render3DUtil.drawBlockBox(
                blockpos,
                Color(
                    ClickGui.getCurrentColor().red,
                    ClickGui.getCurrentColor().green,
                    ClickGui.getCurrentColor().blue,
                    if (full.value) alpha.value else 0
                ),
                outline.value,
                widthy.value
            )
        }
    }
}