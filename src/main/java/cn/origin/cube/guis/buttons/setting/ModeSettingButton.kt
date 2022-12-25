package cn.origin.cube.guis.buttons.setting

import cn.origin.cube.Cube
import cn.origin.cube.core.settings.ModeSetting
import cn.origin.cube.guis.buttons.ModuleButton
import cn.origin.cube.guis.buttons.SettingButton
import cn.origin.cube.module.modules.client.ClickGui
import cn.origin.cube.utils.render.Render2DUtil
import java.awt.Color

class ModeSettingButton(
    width: Float,
    height: Float,
    value: ModeSetting<*>,
    father: ModuleButton
) : SettingButton<ModeSetting<*>>(width, height, value, father) {

    var animation = false
    var distance = 0
    var offset = 0

    override fun drawButton(x: Float, y: Float, mouseX: Int, mouseY: Int) {
        if (animation && distance < father.width) distance += 2;
        else if (animation && distance == father.width.toInt()) animation = false;
        else if (!animation && distance > 0) distance -= 2;

        Render2DUtil.drawRect(x, y, this.width, this.height, Color(15, 15, 15, 95).rgb)
        Cube.fontManager!!.CustomFont.drawStringWithShadow(
            value.name,
            x + 3,
            y + (height / 2) - (Cube.fontManager!!.CustomFont.height / 4),
            Color.WHITE.rgb
        )
        Cube.fontManager!!.CustomFont.drawStringWithShadow(
            (value as ModeSetting<*>).valueAsString,
            x + width - 3 - Cube.fontManager!!.CustomFont.getStringWidth(value.valueAsString),
            y + (height / 2) - (Cube.fontManager!!.CustomFont.height / 4),
            if(ClickGui.INSTANCE.gay.value)
                father.father.category.color.rgb
                        else
            Color.WHITE.rgb
        )
//        Render2DUtil.drawRect(
//            father.x + if (animation) 0f else father.width - distance.toFloat(),
//            father.y + offset,
//            distance.toFloat(),
//            father.height,
//            father.father.category.color.rgb
//        )
        this.x = x
        this.y = y
    }

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        if (!this.isHoveredButton(mouseX, mouseY) || !value.isVisible || !father.isShowSettings) {
            return
        }

        //animation = true
        if (mouseButton == 0) {
            (this.value as ModeSetting<*>).forwardLoop()
        }
    }
}