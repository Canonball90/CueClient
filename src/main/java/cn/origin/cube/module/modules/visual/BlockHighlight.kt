package cn.origin.cube.module.modules.visual

import cn.origin.cube.core.events.world.Render3DEvent
import cn.origin.cube.core.module.Category
import cn.origin.cube.core.module.Module
import cn.origin.cube.core.module.interfaces.Constant
import cn.origin.cube.core.module.interfaces.ModuleInfo
import cn.origin.cube.core.settings.BooleanSetting
import cn.origin.cube.core.settings.FloatSetting
import cn.origin.cube.core.settings.IntegerSetting
import cn.origin.cube.module.modules.client.Colors
import cn.origin.cube.utils.render.Render3DUtil
import net.minecraft.block.material.Material
import net.minecraft.client.Minecraft
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import java.awt.Color

@Constant(constant = false)
@ModuleInfo(name = "BlockHighlight",
    descriptions = "Render current block",
    category = Category.VISUAL)
class BlockHighlight: Module() {

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
        if (ray != null && ray.typeOfHit == RayTraceResult.Type.BLOCK && mc.world.getBlockState(ray.blockPos.also { blockpos = it }).material !== Material.AIR && mc.world.worldBorder.contains(blockpos)) {
            Render3DUtil.drawBlockBox(
                blockpos,
                Color(
                    Colors.getGlobalColor().red,
                    Colors.getGlobalColor().green,
                    Colors.getGlobalColor().blue,
                    if (full.value) alpha.value else 0
                ),
                outline.value,
                widthy.value
            )
        }
    }
}