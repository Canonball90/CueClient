package cn.origin.cube.guis.newGui.panel.component.button.setting

import cn.origin.cube.Cube
import cn.origin.cube.guis.newGui.panel.component.button.ModuleButton
import cn.origin.cube.guis.newGui.panel.component.button.SettingButton
import cn.origin.cube.module.modules.client.ClickGui
import cn.origin.cube.core.settings.BooleanSetting
import cn.origin.cube.module.modules.client.Colors
import cn.origin.cube.module.modules.client.NewClickGui
import cn.origin.cube.utils.render.Render2DUtil
import me.surge.animation.ColourAnimation
import me.surge.animation.Easing
import java.awt.Color

class BooleanSettingButton(
    width: Float,
    height: Float,
    value: BooleanSetting,
    father: ModuleButton
) : SettingButton<BooleanSetting>(width, height, value, father) {

    private val colourAnimation = ColourAnimation(Color.WHITE, Colors.getClickGuiColor(), 200f, false, Easing.LINEAR)

    override fun drawButton(x: Float, y: Float, mouseX: Int, mouseY: Int) {
        Render2DUtil.drawRect(x, y, this.width, this.height, Color(80, 80, 80).rgb)
        colourAnimation.state = value.value as Boolean
        Cube.fontManager!!.CustomFont.drawStringWithShadow(
            value.name,
            x + 3,
            y + (height / 2) - (Cube.fontManager!!.CustomFont.height / 4),
            colourAnimation.getColour().rgb
        )
        this.x = x
        this.y = y
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        if (!isHoveredButton(mouseX, mouseY) || !value.isVisible || !father.isShowSettings) {
            return
        }
        when (mouseButton) {
            0, 1 -> value.value = !(value.value as Boolean)
        }
    }

}