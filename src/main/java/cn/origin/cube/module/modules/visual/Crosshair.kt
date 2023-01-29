package cn.origin.cube.module.modules.visual

import cn.origin.cube.core.module.Category
import cn.origin.cube.core.module.Module
import cn.origin.cube.core.module.interfaces.Constant
import cn.origin.cube.core.module.interfaces.ModuleInfo
import cn.origin.cube.core.settings.BooleanSetting
import cn.origin.cube.core.settings.FloatSetting
import cn.origin.cube.module.modules.client.Colors
import cn.origin.cube.utils.render.Render2DUtil
import net.minecraft.client.gui.ScaledResolution
import net.minecraftforge.client.GuiIngameForge
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.awt.Color

@Constant(constant = false)
@ModuleInfo(name = "Crosshair", descriptions = "Crosshair", category = Category.VISUAL)
class Crosshair: Module() {

    var length: FloatSetting = registerSetting("Length", 10.0f, 0.0f, 25.0f)
    var partWidth: FloatSetting = registerSetting("Part Width", 2.5f, 0.0f, 25.0f)
    var gap: FloatSetting = registerSetting("Gap", 6.1f, 0.0f, 25.0f)
    var outlineWidth: FloatSetting = registerSetting("Outline Width", 1.0f, 0.0f, 5.0f)
    var dynamic: BooleanSetting = registerSetting("Dynamic", true)
    var attackIndicator: BooleanSetting = registerSetting("Attack Indicator", true)
    var fill: BooleanSetting = registerSetting("Fill", true)
    var outline: BooleanSetting = registerSetting("Outline", true)

    override fun onEnable() {
        GuiIngameForge.renderCrosshairs = false
    }

    override fun onDisable() {
        GuiIngameForge.renderCrosshairs = true
    }

    @SubscribeEvent
    fun onRender(event: RenderGameOverlayEvent.Post?) {
        val resolution = ScaledResolution(mc)
        val width = resolution.scaledWidth / 2.0f
        val height = resolution.scaledHeight / 2.0f
        if (fill.value) {
            Render2DUtil.drawRect1(
                width - gap.value - length.value - if (this.isMoving()) 2.0f else 0.0f,
                height - partWidth.value / 2.0f,
                width - gap.value - length.value - (if (this.isMoving()) 2.0f else 0.0f) + length.value,
                height - partWidth.value / 2.0f + partWidth.value,
                Colors.getGlobalColor().rgb
            )
            Render2DUtil.drawRect1(
                width + gap.value + if (this.isMoving()) 2 else 0,
                height - partWidth.value / 2.0f,
                width + gap.value + (if (this.isMoving()) 2 else 0) + length.value,
                height - partWidth.value / 2.0f + partWidth.value,
                Colors.getGlobalColor().rgb
            )
            Render2DUtil.drawRect1(
                width - partWidth.value / 2.0f,
                height - gap.value - length.value - if (this.isMoving()) 2 else 0,
                width - partWidth.value / 2.0f + partWidth.value,
                height - gap.value - length.value - (if (this.isMoving()) 2 else 0) + length.value,
                Colors.getGlobalColor().rgb
            )
            Render2DUtil.drawRect1(
                width - partWidth.value / 2.0f,
                height + gap.value + if (this.isMoving()) 2 else 0,
                width - partWidth.value / 2.0f + partWidth.value,
                height + gap.value + (if (this.isMoving()) 2 else 0) + length.value,
                Colors.getGlobalColor().rgb
            )
            if (attackIndicator.value && mc.player.getCooledAttackStrength(0.0f) < 1.0f) {
                Render2DUtil.drawRect1(
                    width - 10.0f,
                    height + gap.value + length.value + (if (this.isMoving()) 2 else 0) + 2.0f,
                    width - 10.0f + mc.player.getCooledAttackStrength(0.0f) * 20.0f,
                    height + gap.value + length.value + (if (this.isMoving()) 2 else 0) + 2.0f + 2.0f,
                    Colors.getGlobalColor().rgb
                )
            }
        }
        if (outline.value) {
            drawOutline(
                width - gap.value - length.value - if (this.isMoving()) 2.0f else 0.0f,
                height - partWidth.value / 2.0f,
                width - gap.value - length.value - (if (this.isMoving()) 2.0f else 0.0f) + length.value,
                height - partWidth.value / 2.0f + partWidth.value,
                outlineWidth.value,
                Color(0, 0, 0).rgb
            )
            drawOutline(
                width + gap.value + if (this.isMoving()) 2 else 0,
                height - partWidth.value / 2.0f,
                width + gap.value + (if (this.isMoving()) 2 else 0) + length.value,
                height - partWidth.value / 2.0f + partWidth.value,
                outlineWidth.value,
                Color(0, 0, 0).rgb
            )
            drawOutline(
                width - partWidth.value / 2.0f,
                height - gap.value - length.value - if (this.isMoving()) 2 else 0,
                width - partWidth.value / 2.0f + partWidth.value,
                height - gap.value - length.value - (if (this.isMoving()) 2 else 0) + length.value,
                outlineWidth.value,
                Color(0, 0, 0).rgb
            )
            drawOutline(
                width - partWidth.value / 2.0f,
                height + gap.value + if (this.isMoving()) 2 else 0,
                width - partWidth.value / 2.0f + partWidth.value,
                height + gap.value + (if (this.isMoving()) 2 else 0) + length.value,
                outlineWidth.value,
                Color(0, 0, 0).rgb
            )
            if (attackIndicator.value && mc.player.getCooledAttackStrength(0.0f) < 1.0f) {
                drawOutline(
                    width - 10.0f,
                    height + gap.value + length.value + (if (this.isMoving()) 2 else 0) + 2.0f,
                    width - 10.0f + mc.player.getCooledAttackStrength(0.0f) * 20.0f,
                    height + gap.value + length.value + (if (this.isMoving()) 2 else 0) + 2.0f + 2.0f,
                    outlineWidth.value,
                    Color(0, 0, 0).rgb
                )
            }
        }
    }

    fun isMoving(): Boolean {
        return (mc.player.isSneaking() || mc.player.moveStrafing != 0.0f || mc.player.moveForward != 0.0f || !mc.player.onGround) && dynamic.value
    }

    fun drawOutline(x: Float, y: Float, width: Float, height: Float, lineWidth: Float, color: Int) {
        Render2DUtil.drawRect1(x + lineWidth, y, x - lineWidth, y + lineWidth, color)
        Render2DUtil.drawRect1(x + lineWidth, y, width - lineWidth, y + lineWidth, color)
        Render2DUtil.drawRect1(x, y, x + lineWidth, height, color)
        Render2DUtil.drawRect1(width - lineWidth, y, width, height, color)
        Render2DUtil.drawRect1(x + lineWidth, height - lineWidth, width - lineWidth, height, color)
    }
}