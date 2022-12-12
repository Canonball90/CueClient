package dev.canon.cue.guis.buttons.setting

import dev.canon.cue.Cue
import dev.canon.cue.guis.buttons.ModuleButton
import dev.canon.cue.guis.buttons.SettingButton
import dev.canon.cue.settings.BooleanSetting
import java.awt.Color

class BooleanSettingButton(
    width: Float,
    height: Float,
    value: BooleanSetting,
    father: ModuleButton
) : SettingButton<BooleanSetting>(width, height, value, father) {
    override fun drawButton(x: Float, y: Float, mouseX: Int, mouseY: Int) {
        dev.canon.cue.utils.render.Render2DUtil.drawRect(
            x,
            y,
            this.width,
            this.height,
            Color(15, 15, 15, 95).rgb
        )
        Cue.fontManager!!.CustomFont.drawStringWithShadow(
            value.name,
            x + 3,
            y + (height / 2) - (Cue.fontManager!!.CustomFont.height / 4),
            if (value.value as Boolean)
                if (dev.canon.cue.module.modules.client.ClickGui.INSTANCE.gay.value)
                    father.father.category.color.rgb
                else
                    dev.canon.cue.module.modules.client.ClickGui.getCurrentColor().rgb
            else Color.WHITE.rgb
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