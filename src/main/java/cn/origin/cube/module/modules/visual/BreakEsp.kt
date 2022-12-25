package cn.origin.cube.module.modules.visual

import cn.origin.cube.core.settings.FloatSetting
import cn.origin.cube.core.events.world.Render3DEvent
import cn.origin.cube.inject.client.IRenderGlobal
import cn.origin.cube.core.module.Category
import cn.origin.cube.core.module.Module
import cn.origin.cube.module.interfaces.ModuleInfo
import cn.origin.cube.module.interfaces.Para
import cn.origin.cube.module.modules.client.ClickGui
import cn.origin.cube.utils.render.Render3DUtil
import net.minecraft.client.renderer.DestroyBlockProgress
import net.minecraft.init.Blocks

@Para(para = Para.ParaMode.Test)
@ModuleInfo(name = "BreakEsp", descriptions = "BreakEsp", category = Category.VISUAL)
class BreakEsp: Module() {

    var range: FloatSetting = registerSetting("Range", 5f, 5f, 100f)

    override fun onRender3D(event: Render3DEvent?) {
        (mc.renderGlobal as IRenderGlobal).damagedBlocks.forEach { (pos: Int?, progress: DestroyBlockProgress?) ->
            if (progress != null) {
                val blockPos = progress.position
                if (mc.world.getBlockState(blockPos).block == Blocks.AIR) {
                    return@forEach
                }
                if (blockPos.getDistance(
                        mc.player.posX.toInt(),
                        mc.player.posY.toInt(),
                        mc.player.posZ.toInt()
                    ) <= range.value
                ) {
                    val bb = mc.world.getBlockState(blockPos)
                        .getSelectedBoundingBox(mc.world, blockPos)
                    Render3DUtil.drawBBBox(bb, ClickGui.getCurrentColor(), 160, 1f, true)
                }
            }
        }
    }
}